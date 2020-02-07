package config

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.getValue
import com.natpryce.konfig.stringType

data class EnvironmentConfig(
    private val configuration: Configuration = EnvironmentVariables()
) {

    val awsAcessKeyId = configuration[AWS_ACCESS_KEY_ID]
    val awsSecretAccessKey = configuration[AWS_SECRET_ACCESS_KEY]
    val serviceEndpoint = configuration[SERVICE_ENDPOINT]
    val signingRegion = configuration[SIGNING_REGION]

    companion object {
        private val AWS_ACCESS_KEY_ID by stringType
        private val AWS_SECRET_ACCESS_KEY by stringType
        private val SERVICE_ENDPOINT by stringType
        private val SIGNING_REGION by stringType
    }
}