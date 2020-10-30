package info.infosite.entities.gentable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TableRepository extends JpaRepository<Tab,Integer> {
    @Query("From Tab where subMenu.idSubMenu = :id")
    List<Tab> findTableBySubMenuId(int id);
}
