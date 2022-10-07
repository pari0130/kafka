package com.example.kafka.custom4

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomKafkaListener(

    val id: String = "",

    val topic: String = "",

    val groupId: String = "",

    val autoStartup: Boolean = true
)
