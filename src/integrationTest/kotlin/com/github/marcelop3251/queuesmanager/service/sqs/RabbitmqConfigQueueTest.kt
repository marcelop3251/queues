package com.github.marcelop3251.queuesmanager.service.sqs

import com.github.marcelop3251.queuesmanager.config.EnvironmentConfigRMQ
import com.github.marcelop3251.queuesmanager.service.impl.RabbitmqConfigQueue
import com.natpryce.konfig.ConfigurationMap
import com.rabbitmq.jms.admin.RMQConnectionFactory
import com.rabbitmq.jms.client.message.RMQTextMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.junit.jupiter.Container
import javax.jms.Connection
import javax.jms.Session

const val PORT_RMQ = 5672

@Testcontainers
class RabbitmqConfigQueueTest :  BaseQueueTest() {

    companion object {
        @Container
        val rabbitmq = RabbitMQContainer().withExposedPorts(PORT_RMQ)
    }

    private val mutableMap = getPropertiesFromEnvironment()

    lateinit var environment: EnvironmentConfigRMQ

    lateinit var connection: Connection

    lateinit var session: Session

    @BeforeEach
    fun setup() {
        val port = rabbitmq.getMappedPort(PORT_RMQ)
        mutableMap["PORT_RMQ"] = port.toString()
        environment = EnvironmentConfigRMQ(ConfigurationMap(properties = mutableMap))
        connection = clientRabbitmq()
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    }

    @AfterEach
    fun unsetup() {
        session.close()
        connection.close()
    }

    @After
    fun unset() {
        rabbitmq.close()
    }


    @Test
    fun shouldCreateConsumerAndReceibeMessage() {
        val producer = session.createProducer(session.createQueue(QUEUE_NAME))
        val message = RMQTextMessage()

        message.text = "Hello world"
        producer.send(message)

        val rabbit = RabbitmqConfigQueue(environment)
        val consumer = rabbit.createConsumer(QUEUE_NAME)
        val receive = consumer.receive() as RMQTextMessage

        assertThat(receive).isNotNull
        assertThat(receive.text).isEqualTo(message.text)
    }

    @Test
    fun shouldCreateProducerAndSendMessage() {
        val rabbit = RabbitmqConfigQueue(environment)
        val producer = rabbit.createProducer(QUEUE_NAME)
        val message = RMQTextMessage()

        message.text = "Hello world"
        producer.send(message)
        assertThat(message.jmsMessageID).isNotNull()
    }

    fun clientRabbitmq(): Connection {
        val factory = RMQConnectionFactory()
        factory.port = rabbitmq.getMappedPort(PORT_RMQ)
        return  factory.createConnection()

    }
}