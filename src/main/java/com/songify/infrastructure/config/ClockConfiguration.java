package com.songify.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;


// Just for example purposes, in real life you would want to inject a clock that can be mocked for testing purposes.
@Deprecated
@Configuration
class ClockConfiguration {

    @Bean
    Clock clock() {
        return Clock.system(ZoneId.ofOffset("UTC", ZoneOffset.UTC));
    }
}
