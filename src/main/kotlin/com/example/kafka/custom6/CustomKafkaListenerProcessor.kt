package com.example.kafka.custom6

import com.example.kafka.custom2.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodIntrospector
import org.springframework.core.MethodIntrospector.MetadataLookup
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.config.MethodKafkaListenerEndpoint
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory
import org.springframework.util.ClassUtils
import java.lang.reflect.Method

@Configuration("CustomKafkaListenerProcessor6")
class CustomKafkaListenerProcessor(
    private val kafkaListenerContainerFactory: ConcurrentKafkaListenerContainerFactory<String, String>,
    private val kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry
): InitializingBean {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val topicList = listOf("export-test-1", "export-test-2", "export-test-3")

    override fun afterPropertiesSet() {
        topicList.forEach { topic ->
            //start(topic)
        }
    }

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
            kafkaListenerEndpoint.setId(topic)
            kafkaListenerEndpoint.setGroupId("export")
            kafkaListenerEndpoint.setAutoStartup(true)
            kafkaListenerEndpoint.setTopics(topic)
            kafkaListenerEndpoint.setMessageHandlerMethodFactory(DefaultMessageHandlerMethodFactory())
            kafkaListenerEndpoint.bean = CustomKafkaConsumer()
            kafkaListenerEndpoint.method = CustomKafkaConsumer::class.java.getMethod("onMessage", ConsumerRecord::class.java)
            kafkaListenerEndpoint
        } catch (e: NoSuchMethodException) {
            throw Exception(e);
        }
    }
}