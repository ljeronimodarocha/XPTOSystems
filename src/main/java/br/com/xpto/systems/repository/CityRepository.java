package br.com.xpto.systems.repository;

import br.com.xpto.systems.entity.City;
import br.com.xpto.systems.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional
public interface CityRepository extends JpaRepository<City, Integer> {
    @Query("select c from City c where c.capital = true order by c.name")
    Optional<List<City>> returnCityThatAreCapitalsOrderByName();

    @Query(value = "Select uf, count(uf) as value from city group by uf having count(uf)=(Select max(A.CNT) from (Select count(uf) as CNT from city group by (uf)) as A)"
            , nativeQuery = true)
    Map<String, BigInteger> returnStatNameHigherCity();

    @Query(value = "Select uf, count(uf) as value from city group by uf having count(uf)=(Select min(A.CNT) from (Select count(uf) as CNT from city group by (uf)) as A)"
            , nativeQuery = true)
    Map<String, BigInteger> returnStatNameMinorCity();

    @Query(value = "select uf, count(uf) as value from city  group by uf", nativeQuery = true)
    Optional<List<Map<String, BigInteger>>> returnTotalCitiesByStates();

    @Query("select c from City c where c.ibgeId = ?1")
    Optional<City> findByIbgeId(Integer ibge);

    @Query("select c from City c where c.uf=?1 order by name asc")
    Optional<List<City>> findCitiesByUF(State state);

    void deleteByIbgeId(Integer ibgeID);

}
