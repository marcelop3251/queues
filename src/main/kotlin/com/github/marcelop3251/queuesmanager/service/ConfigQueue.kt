package com.github.marcelop3251.queuesmanager.service

import javax.jms.ConnectionFactory
import javax.jms.MessageConsumer
import javax.jms.MessageProducer

interface ConfigQueue {


    /**
     * Create a MessageConsumer from name of queue
     * @param nameQueue
     * @return MessageConsumer
     */
    fun createConsumer(nameQueue: String): MessageConsumer

    /**
     *  Create a MessageProducer from name of queue
     *  @param nameQueue
     *  @Return MessageProducer
     */
    fun createProducer(nameQueue: String): MessageProducer

    /**
     * Return a connection factory for config advanced
     * @return ConnectionFactory
     */
    fun getConnectionFactory(): ConnectionFactory
}