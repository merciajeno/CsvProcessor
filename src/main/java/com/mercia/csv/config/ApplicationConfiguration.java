package com.mercia.csv.config;

import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class ApplicationConfiguration {

	@Bean 
	WebClient webClient()
	{
		return WebClient.builder()
				.baseUrl("https://api.zippopotam.us/us").build();
	}
	
	@Bean
	Cache<String, String> cache()
	{
		return Caffeine.newBuilder()
	            .maximumSize(200)
	            .expireAfterWrite(10, TimeUnit.MINUTES)
	            .build();
	}
	
	@Bean
	public CSVFormat csvFormat()
	{
		return CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .build();
	}
}
