package service.impl

import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.CreateQueueRequest
import com.amazonaws.services.sqs.model.CreateQueueResult
import service.ConfigQueue
import service.listener.ProcessMessageDefault
import util.QueueUtils
import util.QueueUtils.createConnection
import util.QueueUtils.createDestination
import util.QueueUtils.createSession
import java.lang.System.getenv
import javax.jms.ConnectionFactory


class SqsConfigQueue(
    val processMessageDefault: ProcessMessageDefault
) : ConfigQueue {

    override fun createQueue(nameQueue: String): CreateQueueResult {
        val defaultClient = amazonSQSBuilder()

        val createRequest = CreateQueueRequest(nameQueue)
            .addAttributesEntry("DelaySeconds", "60")
            .addAttributesEntry("MessageRetentionPeriod", "86400")

        return defaultClient.createQueue(createRequest)
    }

    override fun createConsumer(nameQueue: String) {
        val connection = createConnection(sqsConnectionFactory())
        val session = createSession(connection)
        val queue = createDestination(session, nameQueue)
        val consumer = session.createConsumer(queue)

        consumer.setMessageListener(processMessageDefault)

        connection.start()

    }

    override fun createProducer(nameQueue: String, message: String) {
        val connection = createConnection(sqsConnectionFactory())
        val session = createSession(connection)
        val queue = createDestination(session, nameQueue)
        val producer = session.createProducer(queue)

        producer.send(session.createTextMessage(message))
    }

    private fun sqsConnectionFactory(): ConnectionFactory {
        return SQSConnectionFactory(
            ProviderConfiguration(),
            amazonSQSBuilder()
        )
    }

    private fun amazonSQSBuilder(): AmazonSQS {
        val serviceEndpoint = getenv("service-endpoint")
        val signingRegion = getenv("signing-region")

        return AmazonSQSClientBuilder.standard()
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion)
            )
            .withCredentials(EnvironmentVariableCredentialsProvider())
            .build()
    }


}