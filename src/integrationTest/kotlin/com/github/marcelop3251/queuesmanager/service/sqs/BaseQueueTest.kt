package com.github.marcelop3251.queuesmanager.service.sqs

import java.util.*

const val QUEUE_NAME = "queue"

open class BaseQueueTest {

    fun getPropertiesFromEnvironment(): MutableMap<String, String> {
        val resource = javaClass.classLoader.getResourceAsStream("application.properties")
        val properties = Properties()
        properties.load(resource)

        return mutableMapOf<String, String>().let {
            properties.forEach{(key, value) ->
                it[key.toString()] = value.toString()
            }
            it
        }
    }
}