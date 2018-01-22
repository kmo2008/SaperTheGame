package pl.kmo2008.saperthegame.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.kmo2008.saperthegame.Entities.Rank;

import java.util.List;

@Repository
public interface Rankrepo extends JpaRepository<Rank, Long> {

    @Query(value = "SELECT * FROM Rank rank WHERE time < :time AND type = 0 ORDER BY time LIMIT 10", nativeQuery = true)
    public List<Rank> getTopOfEasy(@Param("time") Long time);

    @Query(value = "SELECT * FROM Rank rank WHERE time < :time AND type = 1 ORDER BY time LIMIT 10", nativeQuery = true)
    public List<Rank> getTopOfMedium(@Param("time") Long time);

    @Query(value = "SELECT * FROM Rank rank WHERE time < :time AND type = 2 ORDER BY time LIMIT 10", nativeQuery = true)
    public List<Rank> getTopOfHard(@Param("time") Long time);
}
