package com.example.kafka.custom6

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.kafka.listener.MessageListener
import org.springframework.stereotype.Component

@Component("CustomKafkaConsumer6")
class CustomKafkaConsumer : MessageListener<String?, Any?> {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    override fun onMessage(message: ConsumerRecord<String?, Any?>) {
        logger.info(
            "Received CustomMessageListener1 Message : " +
                    "topic=[${message.topic()}], " +
                    "key=[${message.key()}], " +
                    "message=[${message.value()}] with offset=[${message.offset()}]"
        )
    }
}