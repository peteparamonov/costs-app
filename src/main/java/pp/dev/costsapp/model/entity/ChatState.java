package pp.dev.costsapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pp.dev.costsapp.model.dictionary.ChatStateEnum;

@Entity
@Table(name = "chat_state")
public class ChatState {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private ChatStateEnum state;
}
