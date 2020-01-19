package com.gmart.api.chat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.chat.entities.ChatMessage;
import com.gmart.api.chat.services.KafkaManagerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200", "https://gmart-front.herokuapp.com" }, maxAge = 5000)
@Slf4j
public class KafkaController {

	private static final String TOPIC = "KAFKA_TP";

	@Autowired
	private KafkaManagerService kafkaService;
	@Autowired
	KafkaTemplate<String, ChatMessage> kFKTemplateChatMessage;

	@Autowired
	KafkaTemplate<String, String> kFKTemplateString;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public String handleException(Throwable exception) {
		return exception.getMessage();
	}

	@PostMapping(value = "/kafkapush")
	@ResponseBody
	public String kafkaPush(@RequestBody ChatMessage data) {
		kFKTemplateChatMessage.send(TOPIC, data);
		return "Published successfully : [" + data.toString() + "]";

	}

	@MessageMapping("/chat.sendMessage")
	public void sendMessage(@Payload ChatMessage chatMessage, StompHeaderAccessor headerAccessor) {
        //		  System.out.println("/topic/public-"+chatMessage.getReceiver());
		// log.info(headerAccessor.toString());

		// this.messagingTemplate.convertAndSend(
		// "/topic/public-"+chatMessage.getSender()+"-"+chatMessage.getReceiver(),
		// chatMessage);
		this.messagingTemplate.convertAndSend(
				"/topic/public-" + chatMessage.getReceiver() + "-" + chatMessage.getSender(), chatMessage);

		// kafkaService.kafkaNewTopicGroupGenerator("KAFKA_TP","private-group-"+chatMessage.getSender()+"-"+chatMessage.getReceiver());
		kFKTemplateChatMessage.send("KAFKA_TP", chatMessage);

		log.info("Creation ended");

	}

}
