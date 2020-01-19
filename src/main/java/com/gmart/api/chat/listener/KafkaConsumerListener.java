package com.gmart.api.chat.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.gmart.api.chat.entities.ChatMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaConsumerListener {

	@KafkaListener(topics = "KAFKA_TP",groupId = "group_json")
	public void listen( ChatMessage message) {
	   log.info("Received Messasge in group 'group_json': " + message);
	}
	
	@KafkaListener(topics = "KAFKA_TP",groupId = "private_group_a_b")
	public void listenOnChat( ChatMessage message) {
	   log.info("Received Messasge in 'group private-group-a-b': " + message);
	}
}
