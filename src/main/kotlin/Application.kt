
import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.CreateQueueRequest
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import service.ConfigQueue
import service.impl.SqsConfigQueue
import service.listener.ProcessMessageDefault
import javax.jms.MessageListener
import javax.jms.Session
import javax.jms.TextMessage


object Application {

    @JvmStatic
    fun main(args: Array<String>) {

        val sqs = SqsConfigQueue(ProcessMessageDefault())

        sqs.createQueue("marcelo-queue")
        for( i in 1..1000) {
            sqs.createProducer("marcelo-queue", "message number $i")
        }
        sqs.createConsumer("marcelo-queue")

    }
}