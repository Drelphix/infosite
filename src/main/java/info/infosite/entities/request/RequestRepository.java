package info.infosite.entities.request;

import info.infosite.entities.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByStatus(Status status);
    List<Request> findAllByUser(User user);
    @Query(value = "from Request r where date BETWEEN :startDate AND :endDate")
    List<Request> findAllBetweenDates(@Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);
    List<Request> findAllByUserAndStatus(User user, Status status);
    @Query(value = "from Request r where user = :user AND date BETWEEN :startDate AND :endDate")
    List<Request> findAllByUserBetweenDates(@Param("user") User user, @Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);
    @Query(value = "from Request r where status = :status AND date BETWEEN :startDate AND :endDate")
    List<Request> findAllByStatusBetweenDates(@Param("status") Status status, @Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);

}