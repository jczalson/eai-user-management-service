package com.eai.user.messaging.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.eai.user.dto.UserDTO;
import com.eai.user.messaging.config.deserializer.UserDtoDeserializer;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
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

    public Map<String, Object> getConfigForConsumer(Class<?> className, String groupId) {
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
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, className);

        return config;
    }

    @Bean
    @ConditionalOnMissingBean(name = "kafkaUserActivityListenerContainerFactory")
    public ConsumerFactory<String, UserDTO> consumerFactoryForUserActivity(){
       return  new DefaultKafkaConsumerFactory<>(getConfigForConsumer(UserDtoDeserializer.class, "GROUPIDUserActivity"));
    }

    @Bean(name = "kafkaUserActivityListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String,UserDTO> concurrentKafkaListenerContainerFactoryUserActivity(){
        ConcurrentKafkaListenerContainerFactory<String,UserDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryForUserActivity());
        return factory;
    }
}
