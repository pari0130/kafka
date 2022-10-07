package com.example.kafka.custom4

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

@Configuration("CustomKafkaListenerProcessor")
class CustomKafkaListenerProcessor(
    private val kafkaListenerContainerFactory: ConcurrentKafkaListenerContainerFactory<String, String>,
    private val kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry,
    private val kafkaProperties: KafkaProperties,
    private val beanFactory: BeanFactory
) : InitializingBean {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun afterPropertiesSet() {
       start()
    }

    fun start() {
        val targetClass = AopUtils.getTargetClass(CustomKafkaConsumer())
        val annotatedMethods = findListenerMethod(targetClass)
        annotatedMethods.forEach { (method, ann) ->
            ann.forEach { listener ->
                val kafkaListenerEndpoint = createEndPoint(targetClass, method, listener)
                kafkaListenerEndpointRegistry.registerListenerContainer(kafkaListenerEndpoint, kafkaListenerContainerFactory, true)
            }
        }
    }

    private fun createEndPoint(bean: Class<*>, method: Method, kafkaListener: CustomKafkaListener): MethodKafkaListenerEndpoint<String, String> {
        logger.info("[TEST] zone -> ${System.getenv("ZONE")}")
        return try {
            val kafkaListenerEndpoint = MethodKafkaListenerEndpoint<String, String>()
            // val setTopic = kafkaListener.topic + "-" + System.getenv("ZONE")
            val setTopic = kafkaListener.topic + "-" + System.getenv("ZONE")
            kafkaListenerEndpoint.setId(setTopic)
            kafkaListenerEndpoint.setGroupId(kafkaListener.groupId)
            kafkaListenerEndpoint.setAutoStartup(kafkaListener.autoStartup)
            kafkaListenerEndpoint.setTopics(setTopic)
            kafkaListenerEndpoint.setMessageHandlerMethodFactory(DefaultMessageHandlerMethodFactory())
            kafkaListenerEndpoint.bean = beanFactory.getBean(ClassUtils.getUserClass(bean))
            kafkaListenerEndpoint.method = method
            kafkaListenerEndpoint
        } catch (e: NoSuchMethodException) {
            throw Exception(e);
        }
    }

    private fun findListenerMethod(clazz: Class<*>): MutableMap<Method, Set<CustomKafkaListener>> {
        val annotatedMethods = MethodIntrospector.selectMethods(clazz,
            MetadataLookup { method: Method ->
                val listenerMethods: Set<CustomKafkaListener> = findListenerAnnotations(
                    method
                )
                listenerMethods.ifEmpty { null }
            })
        return annotatedMethods
    }

    private fun findListenerAnnotations(method: Method): Set<CustomKafkaListener> {
        val listeners: MutableSet<CustomKafkaListener> = HashSet()
        val ann = AnnotatedElementUtils.findMergedAnnotation(method, CustomKafkaListener::class.java)
        if (ann != null) {
            listeners.add(ann)
        }
        return listeners
    }
}