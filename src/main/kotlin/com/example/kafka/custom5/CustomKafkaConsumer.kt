package com.example.kafka.custom5

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.listener.MessageListener
import org.springframework.stereotype.Component

@Component
class CustomKafkaConsumer {

    @CustomKafkaListener(id = "1", topic = "export-test-blue", groupId = "export")
    class CustomMessageListener1() : MessageListener<String?, Any?> {
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

    @CustomKafkaListener(id = "2", topic = "export-test-green", groupId = "export")
    class CustomMessageListener2() : MessageListener<String?, Any?> {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
        override fun onMessage(message: ConsumerRecord<String?, Any?>) {
            logger.info(
                "Received CustomMessageListener2 Message : " +
                        "topic=[${message.topic()}], " +
                        "key=[${message.key()}], " +
                        "message=[${message.value()}] with offset=[${message.offset()}]"
            )
        }
    }
}