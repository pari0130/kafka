package com.example.kafka.custom4

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.kafka.listener.MessageListener
import org.springframework.stereotype.Component

@Component("CustomKafkaConsumer4")
class CustomKafkaConsumer {

    @Autowired
    lateinit var statProduceEvent: StatProduceEvent

    private val logger = LoggerFactory.getLogger(this::class.java)

    @CustomKafkaListener(id = "1", topic = "export-test", groupId = "export")
    fun onMessage1(message: ConsumerRecord<String?, Any?>) {
        logger.info("Received onMessage1 Message : " +
                "topic=[${message.topic()}], " +
                "key=[${message.key()}], " +
                "message=[${message.value()}] with offset=[${message.offset()}]")

        statProduceEvent.produceEvent("export-test-3", message.value().toString())
    }

    @CustomKafkaListener(id = "2", topic = "export-test-3", groupId = "export")
    fun onMessage2(message: ConsumerRecord<String?, Any?>) {
        logger.info("Received onMessage2 Message : " +
                "topic=[${message.topic()}], " +
                "key=[${message.key()}], " +
                "message=[${message.value()}] with offset=[${message.offset()}]")
    }
}