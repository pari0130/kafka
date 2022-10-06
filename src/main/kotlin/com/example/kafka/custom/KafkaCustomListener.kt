//package com.example.kafka.custom
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties
//import org.springframework.kafka.config.KafkaListenerEndpoint
//import org.springframework.kafka.config.MethodKafkaListenerEndpoint
//import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory
//
//
//abstract class KafkaCustomListener {
//
//    @Autowired
//    private val kafkaProperties: KafkaProperties? = null
//
//    protected fun createMethodListenerEndpoint(topic: String): MethodKafkaListenerEndpoint<String, Any> {
//        val kafkaListenerEndpoint = MethodKafkaListenerEndpoint<String, Any>()
//        kafkaListenerEndpoint.setId(topic)
//        kafkaListenerEndpoint.setGroup(kafkaProperties!!.consumer.groupId)
//        kafkaListenerEndpoint.setAutoStartup(true)
//        kafkaListenerEndpoint.setTopics(topic)
//        kafkaListenerEndpoint.setMessageHandlerMethodFactory(DefaultMessageHandlerMethodFactory())
//        return kafkaListenerEndpoint
//    }
//
//    abstract fun createListenerEndpoint(topic: String?): KafkaListenerEndpoint?
//}