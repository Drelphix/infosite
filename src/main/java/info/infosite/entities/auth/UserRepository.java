    package info.infosite.entities.auth;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.util.List;

    public interface UserRepository extends JpaRepository<User, Integer> {
        User findUserByUsername(String username);

        @Query("select u from User u left join Chat c on c.user=u.id where c.chatId=:id")
        User findUserByChat(@Param("id") Long chatId);

        User findUserByUserKey(String userKey);

        @Query("select u from User u left join Role r on u.role=r where r.role=:role")
        List<User> findUsersByGroup(@Param("role") String role);
    }
