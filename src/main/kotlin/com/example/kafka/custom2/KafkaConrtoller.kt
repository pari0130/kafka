package com.example.kafka.custom2

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/kafka")
class KafkaConrtoller(
    val runner: KafkaConsumerRunner
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PatchMapping("/start")
    fun consumerStart(@RequestParam("topic") topic: String) {
        logger.info("[test] start topic -> $topic")
        runner.start(topic)
    }

    @PatchMapping("/stop")
    fun consumerStop(@RequestParam("topic") topic: String) {
        logger.info("[test] stop topic -> $topic")
        runner.stop(topic)
    }
}