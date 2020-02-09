package com.github.marcelop3251.queuesmanager.config

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.getValue
import com.natpryce.konfig.intType
import com.natpryce.konfig.stringType

class EnvironmentConfigRMQ(
    configuration: Configuration = EnvironmentSingleton.configuration
) {

    val host = configuration[HOST_RMQ]
    val username = configuration[USER_NAME_RMQ]
    val password = configuration[PASSOWORD_RMQ]
    val virtualHost = configuration[VIRTUAL_HOST_RMQ]
    val port = configuration[PORT_RMQ]

    companion object {
        private val HOST_RMQ by stringType
        private val USER_NAME_RMQ by stringType
        private val PASSOWORD_RMQ by stringType
        private val VIRTUAL_HOST_RMQ by stringType
        private val PORT_RMQ by intType
    }
}