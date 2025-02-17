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
    private Object data;

    public static Message buildOkMessage(String message){
        return Message.builder().message(message).status(ResponseStatus.OK).build();
    }

    public static Message buildOkMessage(String message, Object data){
        return Message.builder().message(message).status(ResponseStatus.OK).data(data).build();
    }
}