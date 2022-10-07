package com.example.kafka.custom4

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.kafka.listener.MessageListener
import org.springframework.stereotype.Component

@Component("CustomKafkaConsumer4")
class CustomKafkaConsumer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @CustomKafkaListener(id = "1", topic = "export-test-blue", groupId = "export")
    fun onMessage1(message: ConsumerRecord<String?, Any?>) {
        logger.info("Received onMessage1 Message : " +
                "topic=[${message.topic()}], " +
                "key=[${message.key()}], " +
                "message=[${message.value()}] with offset=[${message.offset()}]")
    }

    @CustomKafkaListener(id = "2", topic = "export-test-green", groupId = "export")
    fun onMessage2(message: ConsumerRecord<String?, Any?>) {
        logger.info("Received onMessage2 Message : " +
                "topic=[${message.topic()}], " +
                "key=[${message.key()}], " +
                "message=[${message.value()}] with offset=[${message.offset()}]")
    }
}