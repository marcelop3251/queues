# Queues Manager

This library make it ease to configure broker of you choose. 
By time this library is work only with queue and at soon we going to create the implementation for work with Topic.
Whether you need of config advanced, so you can retrive a connection and make a cast for the client of broker.

# Table of contents

- Tecnologies
- Brokers
  - SQS
  - RabbitMQ
- Add at your project

### Tecnologies
This library use kotlin and SQS, RabbitMQ in soon we going to use other brokers.


### Brokers

- SQS example

Environment variables that should be available
```
 AWS_ACCESS_KEY_ID=<accesskey>
 AWS_SECRET_ACCESS_KEY=<secretkey>
 SERVICE_ENDPOINT=<endpoint aws>
 SIGNING_REGION=<region>
 ```

1 - Create Consumer
```
    val broker = SqsConfigQueue()
    broker.createConsumer("new-queue")
```

2 - Create Producer
```
    val broker = SqsConfigQueue()
    sqs.createProducer("new-queue")
```


- RabbitMQ example

Environment variables that should be available
```
HOST_RMQ=<host>
USER_NAME_RMQ=<user>
PASSOWORD_RMQ=<user>
VIRTUAL_HOST_RMQ=<virtual-host>
PORT_RMQ=<port>
 ```

1 - Create Consumer
```
    val broker = RabbitmqConfigQueue()
    broker.createConsumer("new-queue")
```

2 - Create Producer
```
    val broker = SqsConfigQueue()
    broker.createProducer("new-queue")
```

### Adding at you project

## Gradle

```
implementation 'com.github.marcelop3251:queues-manager:1.2.0'
```

## Maven

```
<dependency>
  <groupId>com.github.marcelop3251</groupId>
  <artifactId>queues-manager</artifactId>
  <version>1.2.0</version>
</dependency>
```


