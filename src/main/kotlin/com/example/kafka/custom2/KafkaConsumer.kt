package com.example.kafka.custom2

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.listener.MessageListener

class KafkaConsumer: MessageListener<String?, Any?> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun onMessage(message: ConsumerRecord<String?, Any?>) {
        logger.info("Received ConsumerRecord Message : " +
                "topic=[${message.topic()}], " +
                "key=[${message.key()}], " +
                "message=[${message.value()}] with offset=[${message.offset()}]")
    }
}