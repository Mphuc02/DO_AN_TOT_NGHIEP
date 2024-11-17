package dev.chat.chatservice.rest;

import static dev.common.constant.ApiConstant.CHAT.*;
import dev.chat.chatservice.dto.request.CreateMessageRequest;
import dev.chat.chatservice.service.MessageService;
import dev.common.constant.AuthorizationConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController()
@RequestMapping(CHAT_URL)
@RequiredArgsConstructor
public class MessageRest {
    private final MessageService messageService;

    @GetMapping(RECEIVER_ID)
    public ResponseEntity<Object> getMessageWithOtherById(@PathVariable UUID id, @PageableDefault Pageable pageable){
        return ResponseEntity.ok(messageService.getWithOtherById(id, pageable));
    }

    @PostMapping()
    @PreAuthorize(AuthorizationConstant.USER)
    public ResponseEntity<Object> createMessage(@Validated @RequestBody CreateMessageRequest request){
        return ResponseEntity.ok(messageService.create(request));
    }
}