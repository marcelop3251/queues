package service.impl

import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.CreateQueueRequest
import com.amazonaws.services.sqs.model.CreateQueueResult
import config.EnvironmentConfig
import service.ConfigQueue
import util.QueueUtils.createConnection
import util.QueueUtils.createDestination
import util.QueueUtils.createSession
import javax.jms.ConnectionFactory
import javax.jms.MessageConsumer
import javax.jms.MessageProducer


class SqsConfigQueue(
    private val environment: EnvironmentConfig = EnvironmentConfig()
) : ConfigQueue {

    override fun createQueue(nameQueue: String): CreateQueueResult =
        amazonSQSBuilder().let {
            it.createQueue(CreateQueueRequest(nameQueue).addAttributesEntry("DelaySeconds","20"))
        }

    override fun createQueue(nameQueue: String, attributes: Map<String, String>): CreateQueueResult =
        amazonSQSBuilder().let {
            val createQueueRequest = CreateQueueRequest(nameQueue).withAttributes(attributes)
            it.createQueue(createQueueRequest)
        }


    override fun createConsumer(nameQueue: String): MessageConsumer =
        createConnection(sqsConnectionFactory()).let {
            it.start()
            val session = createSession(it)
            session.createConsumer(createDestination(session, nameQueue))
        }


    override fun createProducer(nameQueue: String): MessageProducer =
        createConnection(sqsConnectionFactory()).let {
            val session = createSession(it)
            session.createProducer(createDestination(session,nameQueue))
        }

    private fun sqsConnectionFactory(): ConnectionFactory {
        return SQSConnectionFactory(
            ProviderConfiguration(),
            amazonSQSBuilder()
        )
    }

    private fun amazonSQSBuilder(): AmazonSQS {
        val serviceEndpoint = environment.serviceEndpoint
        val signingRegion = environment.signingRegion

        val credentials = BasicAWSCredentials(environment.awsAcessKeyId, environment.awsSecretAccessKey)

        return AmazonSQSClientBuilder.standard()
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion))
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build()
    }
}