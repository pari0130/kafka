package com.example.kafka.custom5

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class StatConsumeEvent {

    private val logger = LoggerFactory.getLogger(this::class.java)

    //@KafkaListener(topics = ["export-test-green"], groupId = "export")
    fun consumeEvent(message: String) {

        logger.info(
            "message : $message"
        )

        try {
            //statEventHandler.control(JsonParser.parseString(message).asJsonObject)
        } catch (e: Exception) { // IllegalStateException or else
            logger.error("[EXPORT JOB EVENT] Stat consumeEvent message exception -> { message : $message }", e)
        }
    }
}