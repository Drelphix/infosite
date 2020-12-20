package info.infosite.entities.computer;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface ComputerRepository extends JpaRepository<Computer,Long> {
    Computer findByName(String name);
}
