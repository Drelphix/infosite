package info.infosite.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangesRepository extends JpaRepository<Change,Integer> {
}
