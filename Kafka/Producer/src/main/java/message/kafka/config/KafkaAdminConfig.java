package message.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;

@Configuration
public class KafkaAdminConfig {

    public KafkaProperties properties;

    // @Autorwired
    public KafkaAdminConfig(KafkaProperties properties) {
        this.properties = properties;
    }

    // Get configs from application.yml
    @Bean
    public KafkaAdmin kafkaAdmin(){
        var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,properties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    // Creating new topics automatically
    @Bean
    public KafkaAdmin.NewTopics topics(){
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("json-topic").partitions(1).build()
        );
    }

}
