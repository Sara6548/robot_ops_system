package com.robot.repository;

import com.robot.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository// på den linjen Forteller Spring at dette er et repository som skal håndtere databaseoperasjoner for Mission-entity
public interface MissionRepository extends JpaRepository<Mission, Long> {// på den linjen Utvider JpaRepository for å få tilgang til grunnleggende CRUD-operasjoner for Mission-entity, og spesifiserer Long som typen for ID-en
    
    List<Mission> findByStatus(Mission.MissionStatus status);// på den linjen Definerer en metode for å finne alle missions basert på status, som vil bli implementert av Spring Data JPA automatisk
    
    List<Mission> findByDroneType(String droneType); //på den linjen Definerer en metode for å finne alle missions basert på droneType, som vil bli implementert av Spring Data JPA automatisk
    
     @Query("SELECT m FROM Mission m WHERE m.startTime >= CURRENT_DATE")// på den linjen Definerer en tilpasset JPQL-spørring for å finne alle missions som har en startTime som er i dag eller senere
   // @Query("SELECT m FROM Mission m WHERE m.startTime >= CURRENT_DATE")// på den linjen Definerer en tilpasset JPQL-spørring for å finne alle missions som har en startTime som er i dag eller senere
    List<Mission> findTodayMissions();// 
    
    @Query("SELECT COUNT(m) FROM Mission m WHERE m.status = 'ACTIVE'")// på den linjen Definerer en tilpasset JPQL-spørring for å telle antall missions som har statusen ACTIVE
    Long countActiveMissions();
    
    List<Mission> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);// på den linjen Definerer en metode for å finne alle missions som har en startTime mellom to spesifiserte datoer, som vil bli implementert av Spring Data JPA automatisk
    
    List<Mission> findByLocationContaining(String location); // på 
    
    @Query("SELECT m.status, COUNT(m) FROM Mission m GROUP BY m.status")
    List<Object[]> countMissionsByStatus();
    
    @Query(value = "SELECT * FROM missions ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<Mission> findRecentMissions(@Param("limit") int limit);
    
    @Query("SELECT DISTINCT m FROM Mission m JOIN m.detections d")
    List<Mission> findMissionsWithDetections();
    
    @Query("SELECT m FROM Mission m WHERE m.detections IS EMPTY")
    List<Mission> findMissionsWithoutDetections();
    
    @Query("SELECT m FROM Mission m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Mission> searchMissions(@Param("searchTerm") String searchTerm);
    
  
    @Query("SELECT m FROM Mission m WHERE m.startTime >= CURRENT_DATE")
    List<Mission> findUpcomingMissions();// på  den linjen 
}
