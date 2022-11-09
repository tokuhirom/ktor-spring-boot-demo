package com.example.springbootdemo

import kweb.Kweb
import kweb.h1
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.info.GitProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.context.support.GenericApplicationContext
import org.springframework.stereotype.Component
import java.time.ZoneId

@ConstructorBinding
@ConfigurationProperties("kweb")
data class KwebProperties(
    val port: Int = 8080
)

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(KwebProperties::class)
class KwebConfiguration {
    @Bean(
        // On the spring's shutdown process, close the kweb server.
        destroyMethod = "close"
    )
    fun kweb(
        // Use configurationProperty
        kwebProperties: KwebProperties,
        // This is just a example to load the bean, initialized by spring.
        buildProperties: BuildProperties,
    ): Kweb {
        return Kweb(port = kwebProperties.port) {
            doc.body {
                h1().text(
                    "Hello World! ${buildProperties.group}:${buildProperties.artifact}:${buildProperties.name}"
                            + "${buildProperties.version} at ${buildProperties.time.atZone(ZoneId.systemDefault())}"
                )
            }
        }
    }
}

@SpringBootApplication
class SpringbootdemoApplication

fun main(args: Array<String>) {
    val apps = runApplication<SpringbootdemoApplication>(*args)
}
