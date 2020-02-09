package com.github.marcelop3251.queuesmanager.service.impl


import com.github.marcelop3251.queuesmanager.config.EnvironmentConfigRMQ
import com.github.marcelop3251.queuesmanager.service.ConfigQueue
import com.rabbitmq.jms.admin.RMQConnectionFactory
import javax.jms.ConnectionFactory
import javax.jms.MessageConsumer
import javax.jms.MessageProducer

class RabbitmqConfigQueue(
    private val environment: EnvironmentConfigRMQ = EnvironmentConfigRMQ()
) : BaseConfigQueue(), ConfigQueue {

    private val factory = rabbitConnectionFactory()

    override fun createConsumer(nameQueue: String): MessageConsumer =
        createConsumer(factory, nameQueue)

    override fun createProducer(nameQueue: String): MessageProducer =
        createProducer(factory, nameQueue)

    override fun getConnectionFactory(): ConnectionFactory = factory

    private fun rabbitConnectionFactory(): ConnectionFactory =
        RMQConnectionFactory().let {
            with(it) {
                host = environment.host
                username = environment.username
                password = environment.password
                virtualHost = environment.virtualHost
                port = environment.port
            }
            it
        }
}