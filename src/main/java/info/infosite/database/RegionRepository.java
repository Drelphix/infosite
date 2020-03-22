package info.infosite.database;

import net.bytebuddy.TypeCache;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.SortedSet;

public interface RegionRepository extends JpaRepository<Region,Integer> {
}
