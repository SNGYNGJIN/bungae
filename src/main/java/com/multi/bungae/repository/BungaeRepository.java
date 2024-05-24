package com.multi.bungae.repository;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeStatus;
import com.multi.bungae.domain.UserVO;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BungaeRepository extends JpaRepository<Bungae, Long> {

    /**
     * 근처에 있는 번개모임 찾는 쿼리
     */
    @Query(value = "SELECT * FROM Bungae WHERE ST_Distance_Sphere(bungae_location, :location) <= :radius", nativeQuery = true)
    List<Bungae> findBungaeNearby(@Param("location") Point location, @Param("radius") double radius);

    List<Bungae> findAllByOrderByBungaeStartTimeAsc();

    List<Bungae> findAllByOrderByBungaeCreateTimeDesc();

    Optional<Bungae> findById(Long id);

    Optional<Bungae> findByBungaeIdAndBungaeStatusNot(Long bungadId, BungaeStatus bungaeStatus);

}
