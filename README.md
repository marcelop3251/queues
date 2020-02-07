# Queues Manager

This library make it ease to configure broker of you choose.

# Table of contents

- Tecnologies
- Brokers
  - SQS
- Add your project

### Tecnologies
This library use kotlin and SQS in soon we going to use other brokers.


### Brokers

- SQS example

Environment variables that should be available
```
 AWS_ACCESS_KEY_ID=<accesskey>
 AWS_SECRET_ACCESS_KEY=<secretkey>
 SERVICE_ENDPOINT=<endpoint aws>
 SIGNING_REGION=<region>
 ```

1 - Create queue
```
    val sqs = SqsConfigQueue()
    sqs.createQueue("new-queue")
```

2 - Create Consumer
```
    val sqs = SqsConfigQueue()
    sqs.createConsumer("new-queue")
```

3 - Create Producer
```
    val sqs = SqsConfigQueue()
    sqs.createProducer("new-queue")
```

### Adding in you project

## Gradle

```
implementation 'com.github.marcelop3251:queues-manager:1.0.2'
```

## Maven

```
<dependency>
  <groupId>com.github.marcelop3251</groupId>
  <artifactId>queues-manager</artifactId>
  <version>1.0.2</version>
</dependency>
```


