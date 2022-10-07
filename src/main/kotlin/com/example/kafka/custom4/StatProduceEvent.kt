package com.example.kafka.custom4

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class StatProduceEvent(
    private val kafkaTemplate: KafkaTemplate<String, String>? = null
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val zone = System.getenv("ZONE")

    fun produceEvent(topic: String, message: String) {
        try {
            logger.info("check -> topic : $topic, message : $message")
            kafkaTemplate!!.send("$topic-$zone", message)
        } catch (e: Exception) { // JsonIOException
            logger.error("[EVENT] produceEvent message exception -> { message : $message }", e)
        }
    }
}