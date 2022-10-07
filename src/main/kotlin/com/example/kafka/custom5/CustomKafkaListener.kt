package com.example.kafka.custom5

import org.springframework.stereotype.Component

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class CustomKafkaListener(

    val id: String = "",

    val topic: String = "",

    val groupId: String = "",

    val autoStartup: Boolean = true
)
