package message.consumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Objects;

@Configuration
public class DataConsumerFactoryConfig {

    public KafkaProperties properties;

    public DataConsumerFactoryConfig(KafkaProperties properties) {
        this.properties = properties;
    }

    // Same Deserialization as Producer Serialization
    @Bean
    public ConsumerFactory<String, Serializer> consumerFactory() {
        var configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    // Listener
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Serializer> kafkaContainerFactory(ConsumerFactory<String, Serializer> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Serializer>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
