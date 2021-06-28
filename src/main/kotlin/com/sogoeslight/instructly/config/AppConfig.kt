package com.sogoeslight.instructly.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.concurrent.TimeUnit


@Configuration
@EnableCaching
@EnableJpaAuditing
class AppConfig {

//    @Bean
//    fun utcDateTimeProvider() {
//        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
//    }

    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager("Instructor", "Student", "User", "Preferences", "Vehicle")
        .apply {
            isAllowNullValues = false
            setCaffeine(
                Caffeine.newBuilder()
                    .maximumSize(500)
                    .expireAfterAccess(5, TimeUnit.MINUTES)
                    .recordStats()
            )
        }
}