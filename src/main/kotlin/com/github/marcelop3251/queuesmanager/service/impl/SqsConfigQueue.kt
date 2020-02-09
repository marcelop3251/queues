package com.github.marcelop3251.queuesmanager.service.impl

import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.github.marcelop3251.queuesmanager.config.EnvironmentConfigSqs
import com.github.marcelop3251.queuesmanager.service.ConfigQueue
import org.slf4j.LoggerFactory
import javax.jms.ConnectionFactory
import javax.jms.MessageConsumer
import javax.jms.MessageProducer


class SqsConfigQueue(
    private val environment: EnvironmentConfigSqs = EnvironmentConfigSqs()
) : BaseConfigQueue(), ConfigQueue {

    private val factory = amazonSqsFactory()

    override fun createConsumer(nameQueue: String): MessageConsumer =
        createConsumer(factory, nameQueue)

    override fun createProducer(nameQueue: String): MessageProducer =
        createProducer(factory, nameQueue)

    override fun getConnectionFactory(): ConnectionFactory = factory

    private fun amazonSqsFactory() =  SQSConnectionFactory(
        ProviderConfiguration(),
        amazonSQSBuilder()
    )

    private fun amazonSQSBuilder(): AmazonSQS {
        val serviceEndpoint = environment.serviceEndpoint
        val signingRegion = environment.signingRegion

        val credentials = BasicAWSCredentials(environment.awsAcessKeyId, environment.awsSecretAccessKey)

        return AmazonSQSClientBuilder.standard()
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion))
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build()
    }

    companion object {

        val logger = LoggerFactory.getLogger(this.javaClass)
    }
}
