package info.infosite.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatIdRepository extends JpaRepository<Chat, Integer> {
    Chat findChatByChatId(Long chatId);
}
