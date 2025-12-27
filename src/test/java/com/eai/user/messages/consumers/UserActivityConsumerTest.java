package com.eai.user.messages.consumers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.eai.user.dto.UserDTO;
import com.eai.user.entities.UserStatusEnum;
import com.eai.user.messaging.config.KafkaConsumerConfig;
import com.eai.user.messaging.consumer.UserActivityConsumer;

@EmbeddedKafka
@SpringBootTest(properties = { "bootstrap.servers=${spring.embedded.kafka.brokers}", "listen.event.topic:true" })
@ContextConfiguration(classes = { KafkaConsumerConfig.class, KafkaConsumerConfig.class })
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class UserActivityConsumerTest {

    private Producer<String, String> producer;
    private final String TOPIC_NAME = "network.eai.userActivity";

    @SuppressWarnings("removal")
    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    
    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
   
    @Captor
    ArgumentCaptor<UserDTO> userDtoArgumentCaptor;

    @SuppressWarnings("removal")
    @SpyBean
    private UserActivityConsumer userActivityConsumer;


    @BeforeAll
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer())
                .createProducer();
        for (MessageListenerContainer container : kafkaListenerEndpointRegistry.getAllListenerContainers()) {
            ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

    @Test
    public void tesUserActivityConsumer() {
        String message = readFile("src/test/resources/json/consumers/user-activity.json");
        producer.send(new ProducerRecord<String, String>(TOPIC_NAME, message));
        producer.flush();
        verify(userActivityConsumer, timeout(5000).times(1)).consumeUserActivity(userDtoArgumentCaptor.capture());
        assertNotNull(userDtoArgumentCaptor);
        UserDTO userDTO = userDtoArgumentCaptor.getValue();
        assertEquals("kooo", userDTO.getName());
        assertEquals("koko@mail.com", userDTO.getEmail());
    }

    private String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
        }
        return contentBuilder.toString();
    }
}
