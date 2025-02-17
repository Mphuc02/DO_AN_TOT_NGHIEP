package dev.chat.chatservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_relation_ship")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RelationShip {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID doctorId;

    private UUID patientId;
    private LocalDateTime lastContact;
    private LocalDateTime firstContact;
    private String lastMessage;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "relationShip")
    private List<Message> messages;

    public void messageIsImage(){
        this.lastMessage = "Hình ảnh";
    }
}