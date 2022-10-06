//package com.example.kafka.custom
//
//import org.apache.kafka.clients.consumer.ConsumerRecord
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties
//import org.springframework.context.annotation.Configuration
//import org.springframework.kafka.config.KafkaListenerEndpoint
//import org.springframework.kafka.config.MethodKafkaListenerEndpoint
//import org.springframework.kafka.listener.MessageListener
//import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory
//
//@Configuration
//class KafkaEndpointConfiguration(
//    val kafkaProperties: KafkaProperties
//): KafkaCustomListener() {
//
//    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
//
//    override fun createListenerEndpoint(topic: String?): KafkaListenerEndpoint? {
//        val kafkaListenerEndpoint = createMethodListenerEndpoint(topic!!)
//        kafkaListenerEndpoint.bean = CustomMessageListener()
//        try {
//            kafkaListenerEndpoint.method = CustomMessageListener::class.java.getMethod(
//                "onMessage",
//                ConsumerRecord::class.java
//            )
//        } catch (e: NoSuchMethodException) {
//            logger.error("[KAFKA] endpoint error -> ", e)
//        }
//        return kafkaListenerEndpoint
//    }
//
//    private class CustomMessageListener() :
//        MessageListener<String?, Any?> {
//        private val log: Logger = LoggerFactory.getLogger(CustomMessageListener::class.java)
//
//        override fun onMessage(message: ConsumerRecord<String?, Any?>) {
//            log.info("Received ConsumerRecord Message : topic=[" + message.topic() + "], key=[" + message.key() + "], message=[" + message.value() + "] with offset=[" + message?.offset() + "]")
//        }
//    }
//}