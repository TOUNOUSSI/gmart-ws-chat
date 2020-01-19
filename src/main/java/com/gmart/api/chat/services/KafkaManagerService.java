package com.gmart.api.chat.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import com.gmart.api.chat.entities.ChatMessage;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaManagerService {

	String kafkaBootstrapServers;

	@Autowired
	KafkaTemplate<String, ChatMessage> kFKTemplateString;

	public Map<String, Object> kafkaConsumerSetup(String group_id) {
		Map<String, Object> configs = new HashMap<String, Object>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, group_id);
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return configs;
	}

	public void createKafkaTopic(final String topicName, final int partitions) {
		final short replicationFactor = 1;

		// Create admin client
		try (final AdminClient adminClient = KafkaAdminClient.create(buildDefaultClientConfig())) {
			try {
				// Define topic
				final NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);

				// Create topic, which is async call.
				final CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));

				// Since the call is Async, Lets wait for it to complete.
				createTopicsResult.values().get(topicName).get();
			} catch (InterruptedException | ExecutionException e) {
				if (!(e.getCause() instanceof TopicExistsException)) {
					throw new RuntimeException(e.getMessage(), e);
				}
				// TopicExistsException - Swallow this exception, just means the topic already
				// exists.
			}
		}
	}

	/**
	 * Internal helper method to build a default configuration.
	 */
	private Map<String, Object> buildDefaultClientConfig() {
		Map<String, Object> defaultClientConfig = new HashMap<String, Object>();
		defaultClientConfig.put("bootstrap.servers", "localhost:9292");
		defaultClientConfig.put("client.id", "test-consumer-id");
		return defaultClientConfig;
	}
	
	public void DefaultProducer()	{
	  
	    // init kafka admin client
	    Properties properties = new Properties();
	    properties.setProperty("bootstrap.servers","localhost:9292");
	    properties.setProperty("client.id", "producerAdmin");
	    properties.setProperty("metadata.max.age.ms", "3000");
	   AdminClient kafkaAdminClient = AdminClient.create(properties);
	   kafkaAdminClient.createTopics(Collections.singleton(new NewTopic("TST_ADM_KAFKA_TP", 1,(short) 1)));
	}
}
