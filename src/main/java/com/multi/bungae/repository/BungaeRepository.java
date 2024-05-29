package com.multi.bungae.repository;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeStatus;
import com.multi.bungae.domain.UserVO;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BungaeRepository extends JpaRepository<Bungae, Long>, JpaSpecificationExecutor<Bungae> {
    
    @Query(value = "SELECT * FROM Bungae WHERE ST_Distance_Sphere(bungae_location, :location) <= :radius", nativeQuery = true)
    List<Bungae> findBungaeNearby(@Param("location") Point location, @Param("radius") double radius);

    List<Bungae> findAllByOrderByBungaeStartTimeAsc();

    List<Bungae> findAllByOrderByBungaeCreateTimeDesc();

    Optional<Bungae> findById(Long id);

    Optional<Bungae> findByBungaeIdAndBungaeStatusNot(Long bungadId, BungaeStatus bungaeStatus);

    @Modifying
    @Query("UPDATE Bungae b SET b.bungaeStatus = :newStatus WHERE b.bungaeStatus = :currentStatus AND b.bungaeStartTime < :time")
    void updateStatusToInProgress(@Param("currentStatus") BungaeStatus currentStatus, @Param("newStatus") BungaeStatus newStatus, @Param("time") LocalDateTime time);

    @Modifying
    @Query("UPDATE Bungae b SET b.bungaeStatus = :newStatus WHERE b.bungaeStatus = :currentStatus AND b.bungaeStartTime < :time")
    void updateStatusToEnded(@Param("currentStatus") BungaeStatus currentStatus, @Param("newStatus") BungaeStatus newStatus, @Param("time") LocalDateTime time);
}
