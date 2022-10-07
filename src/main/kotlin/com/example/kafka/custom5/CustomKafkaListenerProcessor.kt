package com.example.kafka.custom5

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.config.MethodKafkaListenerEndpoint
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory
import org.springframework.util.ClassUtils
import org.springframework.util.ObjectUtils


@Configuration("CustomKafkaListenerProcessor2")
class CustomKafkaListenerProcessor(
    private val kafkaListenerContainerFactory: ConcurrentKafkaListenerContainerFactory<String, String>,
    private val kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry,
    private val kafkaProperties: KafkaProperties,
    private val applicationContext: ApplicationContext,
    private val beanFactory: BeanFactory
) : InitializingBean {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun afterPropertiesSet() {
        logger.info("afterPropertiesSet")
        //start2()
    }

    fun start(){
        logger.info("start")
        val targetClass = AopUtils.getTargetClass(CustomKafkaConsumer())
        findListenerClazz(targetClass).forEach { listener ->
            createContainer(listener.first, listener.second, kafkaListenerContainerFactory)
        }
    }

    fun start2() {
        logger.info("start")
        val targetClass = AopUtils.getTargetClass(CustomKafkaConsumer())
        findListenerClazz(targetClass).forEach { listener ->
            val kafkaListenerEndpoint = createEndPoint(listener.first, listener.second)
            kafkaListenerEndpointRegistry.registerListenerContainer(kafkaListenerEndpoint, kafkaListenerContainerFactory, true)
        }
    }

    private fun createContainer(bean: Class<*>, kafkaListener: CustomKafkaListener, factory: ConcurrentKafkaListenerContainerFactory<String, String>) {
        val container = factory.createContainer(kafkaListener.topic)
        logger.info("check -> ${ClassUtils.getUserClass(bean)}")
        container.containerProperties.messageListener = ClassUtils.getUserClass(bean)
        container.containerProperties.setGroupId(kafkaListener.groupId)
        container.isAutoStartup =  kafkaListener.autoStartup
        container.setBeanName(kafkaListener.topic)
        container.start()
    }

    private fun createEndPoint(bean: Class<*>, kafkaListener: CustomKafkaListener): MethodKafkaListenerEndpoint<String, String> {
        logger.info("[TEST] zone -> ${System.getenv("ZONE")}")
        logger.info("[TEST] bean -> $bean // listener -> $kafkaListener")
        logger.info("check -> ${bean.name}")
//        logger.info("check - ${beanFactory.getBean("CustomMessageListener1", bean::class.java).getMethod("onMessage", ConsumerRecord::class.java)}")
        return try {
            val benaa = beanFactory.getBean(bean)
            val kafkaListenerEndpoint = MethodKafkaListenerEndpoint<String, String>()
            // val setTopic = kafkaListener.topic + "-" + System.getenv("ZONE")
            val setTopic = kafkaListener.topic
            kafkaListenerEndpoint.setId(setTopic)
            kafkaListenerEndpoint.setGroupId(kafkaListener.groupId)
            kafkaListenerEndpoint.setAutoStartup(kafkaListener.autoStartup)
            kafkaListenerEndpoint.setTopics(setTopic)
            kafkaListenerEndpoint.setMessageHandlerMethodFactory(DefaultMessageHandlerMethodFactory())
            kafkaListenerEndpoint.bean = beanFactory.getBean(ClassUtils.getUserClass(bean))
            logger.info("check -> $kafkaListenerEndpoint")
            kafkaListenerEndpoint.method = beanFactory.getBean(ClassUtils.getUserClass(bean)).javaClass.getMethod("onMessage", ConsumerRecord::class.java)
            kafkaListenerEndpoint
        } catch (e: NoSuchMethodException) {
            throw Exception(e);
        }
    }

    private fun findListenerClazz(clazz: Class<*>): ArrayList<Pair<Class<*>, CustomKafkaListener>> {
        val listeners: ArrayList<Pair<Class<*>, CustomKafkaListener>> = ArrayList()
        applicationContext.getBeanProvider(ClassUtils.getUserClass(clazz)).stream().forEach { parentClazz ->
            parentClazz.javaClass.declaredClasses.forEach { listenerClazz ->
                val annotation = listenerClazz.getAnnotation(CustomKafkaListener::class.java)
                logger.info("check 2 -> $annotation")
                logger.info("check 3 -> $listenerClazz")
                logger.info("check 4 -> ${listenerClazz.simpleName}")
                if (!ObjectUtils.isEmpty(listenerClazz) && !ObjectUtils.isEmpty(annotation)) {
                    listeners.add(Pair(listenerClazz, annotation))
                }
            }
        }
        return listeners
    }
}