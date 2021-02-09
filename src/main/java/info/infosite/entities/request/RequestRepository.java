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
    public List<Request> getAllBetweenDates(@Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);

}