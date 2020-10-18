    package info.infosite.database.auth;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    public interface UserRepository extends JpaRepository<User, Integer> {
        User findUserByUsername(String username);

        @Query("select u from User u where ChatId.chatId=:chat_id")
        User findUserByChatId(@Param("chat_id") Long ChatId);

        User findUserByUserKey(String userKey);
    }
