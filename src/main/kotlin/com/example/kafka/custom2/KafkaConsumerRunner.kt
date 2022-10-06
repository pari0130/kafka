package com.example.kafka.custom2

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.config.MethodKafkaListenerEndpoint
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory
import java.util.*
import kotlin.random.Random

@Configuration
class KafkaConsumerRunner(
    private val kafkaListenerContainerFactory: ConcurrentKafkaListenerContainerFactory<String, String>,
    private val kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun start(topic: String) {
        val kafkaListenerEndpoint = createEndPoint(topic)
        kafkaListenerEndpointRegistry.registerListenerContainer(kafkaListenerEndpoint, kafkaListenerContainerFactory, true)
    }

    fun stop(topic: String) {
        kafkaListenerEndpointRegistry.getListenerContainer(topic)!!.stop()
    }

    private fun createEndPoint(topic: String): MethodKafkaListenerEndpoint<String, String> {
        logger.info("[TEST] zone -> ${System.getenv("ZONE")}")
        return try {
            val kafkaListenerEndpoint = MethodKafkaListenerEndpoint<String, String>()
            val setTopic = topic + "-" + System.getenv("ZONE")
            kafkaListenerEndpoint.setId(setTopic)
            kafkaListenerEndpoint.setGroupId("export")
            kafkaListenerEndpoint.setAutoStartup(true)
            kafkaListenerEndpoint.setTopics(setTopic)
            kafkaListenerEndpoint.setMessageHandlerMethodFactory(DefaultMessageHandlerMethodFactory())
            kafkaListenerEndpoint.bean = KafkaConsumer()
            kafkaListenerEndpoint.method = KafkaConsumer::class.java.getMethod("onMessage", ConsumerRecord::class.java)
            kafkaListenerEndpoint
        } catch (e: NoSuchMethodException) {
            throw Exception(e);
        }
    }
}