package service.sqs


import com.amazon.sqs.javamessaging.message.SQSTextMessage
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.natpryce.konfig.ConfigurationMap
import config.EnvironmentConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import service.impl.SqsConfigQueue

const val QUEUE_NAME = "queue"

@Testcontainers
class SqsConfigQueueTest : BaseQueueTest() {


    companion object {
        @Container
        val sqsContainer = LocalStackContainer().withServices(SQS)
    }

    private val mutableMap = getPropertiesFromEnvironment()

    lateinit var environment: EnvironmentConfig

    lateinit var clientSqs: AmazonSQS

    @BeforeEach
    fun setClientSqs() {
        val endpoint = sqsContainer.getEndpointConfiguration(SQS)
        mutableMap["SERVICE_ENDPOINT"] = endpoint.serviceEndpoint
        environment = EnvironmentConfig(ConfigurationMap(properties = mutableMap))
        clientSqs = clientAwsSqs()
        clientSqs.createQueue(QUEUE_NAME)

    }

    @AfterEach
    fun unset() {
        val queueUrl = clientSqs.getQueueUrl(QUEUE_NAME).queueUrl
        clientSqs.deleteQueue(queueUrl)
    }

    @Test
    fun shouldCreateQueue() {
        val queurUrl = SqsConfigQueue(environment = environment).createQueue("NEW_QUEUE")
        assertThat(queurUrl).isNotNull
        clientSqs.deleteQueue(queurUrl.queueUrl)
    }

    @Test
    fun shouldCreateConsumer() {
        val queurUrl = clientSqs.getQueueUrl(QUEUE_NAME)
        val sqsConfigQueue = SqsConfigQueue(environment = environment)
        val consumer = sqsConfigQueue.createConsumer(QUEUE_NAME)
        val messageBody = "Message Send"

        clientSqs.sendMessage(queurUrl.queueUrl, messageBody)

        val receive = consumer.receive() as SQSTextMessage
        consumer.close()
        assertThat(receive).isNotNull
    }

    @Test
    fun shouldCreateProducerAndSendMessage() {
        val sqsConfigQueue = SqsConfigQueue(environment = environment)
        val sqsProducer = sqsConfigQueue.createProducer(QUEUE_NAME)
        val message = SQSTextMessage("Message Send")
        sqsProducer.send(message)
        assertThat(message.jmsMessageID).isNotNull()
    }

    fun clientAwsSqs() = AmazonSQSClientBuilder
        .standard()
        .withEndpointConfiguration(sqsContainer.getEndpointConfiguration(SQS))
        .withCredentials(sqsContainer.defaultCredentialsProvider)
        .build()
}