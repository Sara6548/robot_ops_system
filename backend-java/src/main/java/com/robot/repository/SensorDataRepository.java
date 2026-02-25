package com.robot.repository;

import com.robot.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    
    List<SensorData> findByMissionId(Long missionId);
    
    List<SensorData> findBySensorType(SensorData.SensorType sensorType);
    
    @Query("SELECT s FROM SensorData s WHERE s.timestamp BETWEEN :start AND :end")
    List<SensorData> findByTimestampBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT s.sensorType, AVG(CAST(s.value AS double)) FROM SensorData s GROUP BY s.sensorType")
    List<Object[]> getAverageBySensorType();
    
   
    @Query(value = "SELECT * FROM sensor_data WHERE mission_id = :missionId ORDER BY timestamp DESC LIMIT :limit", nativeQuery = true)
    List<SensorData> findRecentByMissionId(@Param("missionId") Long missionId, @Param("limit") int limit);
    
    
    @Query("SELECT s FROM SensorData s WHERE s.mission.id = :missionId ORDER BY s.timestamp DESC")
    List<SensorData> findAllByMissionIdOrderByTimestampDesc(@Param("missionId") Long missionId);
    

    @Query("SELECT s FROM SensorData s WHERE s.mission.id = :missionId AND s.timestamp >= :since ORDER BY s.timestamp DESC")
    List<SensorData> findRecentSince(@Param("missionId") Long missionId, @Param("since") LocalDateTime since);
}