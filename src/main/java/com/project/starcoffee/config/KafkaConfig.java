package com.project.starcoffee.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public class KafkaConfig {

//    private final Environment env;
//
//    public KafkaConfig(Environment env) {
//        this.env = env;
//    }
//
//    public Map<String, Object> producerConfig() {
//       Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.producer.bootstrap-servers"));
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GenericJackson2JsonRedisSerializer.class);
//        return props;
//    }
//
//    public ProducerFactory<String, Object> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(this.producerConfig());
//    }
//
//    @Bean
//    public KafkaTemplate<String, Object> kafkaTemplate() {
//        return new KafkaTemplate<>(this.producerFactory());
//    }
}
