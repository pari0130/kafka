//package com.example.kafka.custom
//
//import org.apache.kafka.clients.consumer.ConsumerConfig
//import org.apache.kafka.common.serialization.StringDeserializer
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.BeanFactory
//import org.springframework.beans.factory.InitializingBean
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.ComponentScan
//import org.springframework.context.annotation.Configuration
//import org.springframework.kafka.annotation.EnableKafka
//import org.springframework.kafka.config.*
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory
//import org.springframework.kafka.core.DefaultKafkaProducerFactory
//import org.springframework.kafka.core.KafkaTemplate
//import org.springframework.kafka.listener.ContainerProperties
//
//@Configuration
//class KafkaConfiguration(
//    val beanFactory: BeanFactory,
//    val kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry,
//    val kafkaListenerContainerFactory: KafkaListenerContainerFactory<*>
//    //,scheduleTriggerService: ScheduleTriggerService
//) : InitializingBean {
//    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
//
//    override fun afterPropertiesSet() {
//        registryKafkaListener()
//    }
//
//    fun registryKafkaListener() {
//        logger.info("check -> 1")
//        val kafkaListener: KafkaCustomListener = beanFactory.getBean(KafkaEndpointConfiguration::class.java)
//        kafkaListenerEndpointRegistry.registerListenerContainer(
//            kafkaListener.createListenerEndpoint("export-test-blue")!!, kafkaListenerContainerFactory, false
//        )
//    }
//}
//
//
////
//////
//////@EnableKafka
//////@Configuration
//////class KafkaConfiguration(private val kafkaProperties: KafkaProperties, val beanFactory: BeanFactory){
//////
//////    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
//////
//////    @Bean
//////    fun consumerFactory(): MutableMap<String, Any> {
//////        val props: MutableMap<String, Any> = HashMap()
//////        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaProperties.consumer.bootstrapServers
//////        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false
//////        props[ConsumerConfig.GROUP_ID_CONFIG] = kafkaProperties.consumer.groupId
//////        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
//////        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
//////        props[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = 100
//////        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"
//////        return props
//////    }
//////
//////    @Bean(name = [KafkaListenerConfigUtils.KAFKA_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME])
//////    fun defaultKafkaListenerEndpointRegistry(): KafkaListenerEndpointRegistry {
//////        return object : KafkaListenerEndpointRegistry() {
//////            override fun registerListenerContainer(
//////                endpoint: KafkaListenerEndpoint,
//////                factory: KafkaListenerContainerFactory<*>
//////            ) {
//////                super.registerListenerContainer(endpoint, factory)
//////            }
//////        }
//////    }
//////
//////    @Bean
//////    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
//////        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
//////        val kafkaListener: KafkaEndpointConfiguration = beanFactory.getBean(KafkaEndpointConfiguration::class.java)
//////        factory.createListenerContainer(kafkaListener.createListenerEndpoint("export-test-d"))
//////        factory.consumerFactory = DefaultKafkaConsumerFactory(consumerFactory())
//////        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
//////        return factory
//////    }
//////}