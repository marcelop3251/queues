package com.github.marcelop3251.queuesmanager.config

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.EnvironmentVariables

object EnvironmentSingleton {

   val configuration: Configuration = EnvironmentVariables()
}