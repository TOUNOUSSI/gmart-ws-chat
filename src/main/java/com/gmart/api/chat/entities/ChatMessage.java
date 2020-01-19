package com.gmart.api.chat.entities;

import lombok.Data;

@Data
public class ChatMessage {
	private MessageType type;
    private String content;
    private String sender;
    private String receiver;
     public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
