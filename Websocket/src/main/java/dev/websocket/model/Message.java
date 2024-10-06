package dev.websocket.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Message {
    private String message;
    private ResponseStatus status;

    public static Message buildOkMessage(String message){
        return Message.builder().message(message).status(ResponseStatus.OK).build();
    }
}