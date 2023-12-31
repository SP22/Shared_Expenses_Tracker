package splitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import splitter.entity.Gift;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {
}
