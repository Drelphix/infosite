    package info.infosite.database.auth;

    import org.springframework.data.jpa.repository.JpaRepository;

    public interface UserRepository extends JpaRepository<User, Integer> {
        User findUserByUsername(String username);

        User findUserByUserKey(String userKey);
    }
