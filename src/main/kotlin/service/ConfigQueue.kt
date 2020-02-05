package service

import com.amazonaws.services.sqs.model.CreateQueueResult

interface ConfigQueue {

    fun createQueue(nameQueue: String): CreateQueueResult

    fun createConsumer(nameQueue: String)

    fun createProducer(nameQueue: String, message: String)
}