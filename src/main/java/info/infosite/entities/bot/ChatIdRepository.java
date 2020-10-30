package info.infosite.entities.bot;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatIdRepository extends JpaRepository<Chat, Integer> {
    Chat findChatByChatId(Long chatId);
}
