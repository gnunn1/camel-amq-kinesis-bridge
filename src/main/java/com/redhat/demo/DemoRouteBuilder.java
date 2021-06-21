package com.redhat.demo;

import java.util.UUID;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.kinesis.KinesisConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class DemoRouteBuilder extends RouteBuilder {

    @Value("${spring.artemis.queueName}")
    private String queueName;

    @Value("${kinesis.stream}")
    private String streamName;

    @Value("${kinesis.accessID}")
    private String accessID;

    @Value("${kinesis.accessKey}")
    private String accessKey;

    @Value("${kinesis.region}")
    private String region;

    public void configure() throws Exception {

        from("timer:mytimer?period=5000").routeId("generate-amq")
            .bean(TransactionGenerator.class, "generateTransaction")
            .to(getAMQProducerEndpoint());

        from(getAMQConsumerEndpoint()).routeId("bridge-amq-kinesis")
            .log("Received a message from AMQ - ${body} - sending to Kinesis")
            .setHeader(KinesisConstants.PARTITION_KEY, constant(UUID.randomUUID().toString()))
            .to(getKinesisEndpoint());

        from(getKinesisEndpoint()).routeId("read-kinesis stream")
            .log("Received a message from Kinesis - ${body}");
    }

    private String getKinesisEndpoint() {
        return String.format("aws-kinesis:%s?accessKey=%s&secretKey=%s&region=%s",streamName,accessID,accessKey,region);
    }

    private String getAMQConsumerEndpoint() {
        return String.format("jms:queue:%s?concurrentConsumers=1", queueName);
    }

    private String getAMQProducerEndpoint() {
        return String.format("jms:queue:%s", queueName);
    }
}