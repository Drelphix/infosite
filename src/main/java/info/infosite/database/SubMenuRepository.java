package info.infosite.database;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface SubMenuRepository extends JpaRepository<SubMenu, Integer> {
}
