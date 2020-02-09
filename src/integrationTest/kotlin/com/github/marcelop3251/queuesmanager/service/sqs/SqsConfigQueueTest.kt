package com.github.marcelop3251.queuesmanager.service.sqs


import com.amazon.sqs.javamessaging.message.SQSTextMessage
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.natpryce.konfig.ConfigurationMap
import com.github.marcelop3251.queuesmanager.config.EnvironmentConfigSqs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import com.github.marcelop3251.queuesmanager.service.impl.SqsConfigQueue
import org.junit.After


@Testcontainers
class SqsConfigQueueTest : BaseQueueTest() {

    companion object {
        @Container
        val sqsContainer = LocalStackContainer().withServices(SQS)
    }

    private val mutableMap = getPropertiesFromEnvironment()

    lateinit var environment: EnvironmentConfigSqs

    lateinit var clientSqs: AmazonSQS

    @BeforeEach
    fun setup() {
        val endpoint = sqsContainer.getEndpointConfiguration(SQS)
        mutableMap["SERVICE_ENDPOINT"] = endpoint.serviceEndpoint
        environment = EnvironmentConfigSqs(ConfigurationMap(properties = mutableMap))
        clientSqs = clientAwsSqs()
        clientSqs.createQueue(QUEUE_NAME)
    }

    @AfterEach
    fun unsetup() {
        val queueUrl = clientSqs.getQueueUrl(QUEUE_NAME).queueUrl
        clientSqs.deleteQueue(queueUrl)
    }

    @After
    fun unset(){
        sqsContainer.close()
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
        sqsProducer.close()
    }

    fun clientAwsSqs() = AmazonSQSClientBuilder
        .standard()
        .withEndpointConfiguration(sqsContainer.getEndpointConfiguration(SQS))
        .withCredentials(sqsContainer.defaultCredentialsProvider)
        .build()
}