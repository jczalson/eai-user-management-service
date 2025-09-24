package com.eai.user.messaging.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.eai.user.dto.UserDTO;

@Configuration
public class KafkaProducerConfig {

    private static final String SECURITY_PROTOCOL = "security.protocol";
    private static final String SASL_MECHANISM = "sasl.mechanism";
    private static final String SASL_JAAS_CONFIG = "sasl.jaas.config";
    private static final int CONSTANT_10000 = 10000;
    private static final int CONSTANT_180000 = 180000;
    private static final int CONSTANT_3000 = 3000;
    private static final int CONSTANT_3 = 3;

    @Value("${bootstrap.servers}")
    private String bootstrapServer = null;

    @Value("${security.protocol}")
    private String securityProtocol = null;

    @Value("${sasl.jaas.config}")
    private String jaasConfig = null;

    @Value("${sasl.mechanism}")
    private String saslMeccanism = null;

    public Map<String, Object> getConfigForProducer() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        if (!"NA".equals(securityProtocol)) {
            config.put(SECURITY_PROTOCOL, securityProtocol);
        }
        if (!"NA".equals(saslMeccanism)) {
            config.put(SASL_MECHANISM, saslMeccanism);
        }
        if (!"NA".equals(jaasConfig)) {
            config.put(SASL_JAAS_CONFIG, jaasConfig);
        }
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // Properties related to Kafka retries --->
        // After this time interval retry will be done to produce
        config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, CONSTANT_3000);

        // No of retires in case of transient exception
        config.put(ProducerConfig.RETRIES_CONFIG, CONSTANT_3);
        return config;
    }

    @Bean
    public ProducerFactory<String, UserDTO> producerUserActivityFactory() {
        return new DefaultKafkaProducerFactory<String, UserDTO>(getConfigForProducer());
    }

    @Bean
    public KafkaTemplate<String, UserDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerUserActivityFactory());
    }
}
