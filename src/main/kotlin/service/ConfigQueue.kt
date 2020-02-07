package service

import com.amazonaws.services.sqs.model.CreateQueueResult
import javax.jms.MessageConsumer
import javax.jms.MessageProducer

interface ConfigQueue {

    /**
     * The name of queue that will be created
     */
    fun createQueue(nameQueue: String): CreateQueueResult

    /**
     * The name of queue that will be created with the attritutes
     */
    fun createQueue(nameQueue: String, attributes: Map<String, String>): CreateQueueResult

    /**
     * The name of queue that will be listening
     */
    fun createConsumer(nameQueue: String): MessageConsumer


    /**
     *  The name of queue that will be send message
     */
    fun createProducer(nameQueue: String): MessageProducer
}