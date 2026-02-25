package com.robot.repository;

import com.robot.model.Detection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository// på den linjen Forteller Spring at dette er et repository som skal håndtere databaseoperasjoner for Detection-entity
public interface DetectionRepository extends JpaRepository<Detection, Long> {
    // på den linjen Utvider JpaRepository for å få tilgang til grunnleggende CRUD-operasjoner for Detection-entity, og spesifiserer Long som typen for ID-en
    
    List<Detection> findByMissionId(Long missionId);
    // på den linjen Definerer en metode for å finne alle deteksjoner knyttet til en spesifikk mission basert på missionId, som vil bli implementert av Spring Data JPA automatisk
    
    List<Detection> findByObjectClass(String objectClass);
    // på den linjen Definerer en metode for å finne alle deteksjoner av en spesifikk objektklasse, som vil bli implementert av Spring Data JPA automatisk
    
    List<Detection> findByConfidenceGreaterThan(Double confidence);
    // på den linjen Definerer en metode for å finne alle deteksjoner med en confidence-verdi større enn en spesifisert terskel, som vil bli implementert av Spring Data JPA automatisk
    
    List<Detection> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    // på den linjen Definerer en metode for å finne alle deteksjoner som har en timestamp mellom to spesifiserte datoer, som vil bli implementert av Spring Data JPA automatisk
    
    List<Detection> findByMissionIdOrderByTimestampDesc(Long missionId);
    // på den linjen Definerer en metode for å finne alle deteksjoner knyttet til en spesifikk mission og sortere dem etter timestamp i synkende rekkefølge, som vil bli implementert av Spring Data JPA automatisk
    
     @Query("SELECT d FROM Detection d WHERE d.mission.id = :missionId AND d.confidence > :threshold ORDER BY d.confidence DESC")
     // på den linjen Definerer en tilpasset JPQL-spørring for å finne deteksjoner knyttet til en spesifikk mission som har en confidence-verdi større enn en spesifisert terskel, og sortere dem etter confidence i synkende rekkefølge
    
   // @Query("SELECT d.objectClass, COUNT(d) FROM Detection d GROUP BY d.objectClass")
    // på den linjen Definerer en tilpasset JPQL-spørring for å telle antall deteksjoner for hver objektklasse og gruppere resultatene etter objektklasse
    List<Object[]> countDetectionsByObjectClass();// på den linjen Metode for å utføre spørringen og returnere resultatet som en liste av objekter, hvor hvert objekt er et array som inneholder objektklassen og antallet deteksjoner for den klassen
    
    @Query("SELECT d.objectClass, COUNT(d), AVG(d.confidence) FROM Detection d WHERE d.mission.id = :missionId GROUP BY d.objectClass")// på den linjen Definerer en tilpasset JPQL-spørring for å telle antall deteksjoner og beregne gjennomsnittlig confidence for hver objektklasse innenfor en spesifikk mission, og gruppere resultatene etter objektklasse
    List<Object[]> countDetectionsByObjectClassForMission(@Param("missionId") Long missionId);// på den linjen Metode for å utføre spørringen og returnere resultatet som en liste av objekter, hvor hvert objekt er et array som inneholder objektklassen, antallet deteksjoner og gjennomsnittlig confidence for den klassen innenfor den spesifikke missionen
    
    @Query("SELECT AVG(d.confidence) FROM Detection d")// på den linjen Definerer en tilpasset JPQL-spørring for å beregne gjennomsnittlig confidence-verdi for alle deteksjoner i databasen
    Double findAverageConfidence();// på den linjen Metode for å utføre spørringen og returnere gjennomsnittlig confidence-verdi som en Double
    
    @Query("SELECT AVG(d.confidence) FROM Detection d WHERE d.mission.id = :missionId")// på den linjen Definerer en tilpasset JPQL-spørring for å beregne gjennomsnittlig confidence-verdi for deteksjoner knyttet til en spesifikk mission
    Double findAverageConfidenceByMission(@Param("missionId") Long missionId);// på den linjen Metode for å utføre spørringen og returnere gjennomsnittlig confidence-verdi for den spesifikke missionen som en Double
    
    @Query("SELECT d FROM Detection d WHERE d.confidence > :threshold ORDER BY d.confidence DESC")// på den linjen Definerer en tilpasset JPQL-spørring for å finne deteksjoner som har en confidence-verdi større enn en spesifisert terskel, og sortere dem etter confidence i synkende rekkefølge
    List<Detection> findHighConfidenceDetections(@Param("threshold") Double threshold);// på den linjen Metode for å utføre spørringen og returnere resultatet som en liste av deteksjoner som oppfyller kriteriene
    
    @Query(value = "SELECT * FROM detections ORDER BY timestamp DESC LIMIT :limit", nativeQuery = true)// på den linjen Definerer en tilpasset SQL-spørring for å finne de nyeste deteksjonene basert på timestamp, og begrense antallet resultater som returneres
    List<Detection> findRecentDetections(@Param("limit") int limit);// på den linjen Metode for å utføre spørringen og returnere resultatet som en liste av deteksjoner, hvor antallet deteksjoner som returneres er begrenset av parameteren "limit"
    
     @Query("SELECT d FROM Detection d WHERE d.mission.id = :missionId AND d.imagePath IS NOT NULL")// på den linjen Definerer en tilpasset JPQL-spørring for å finne deteksjoner knyttet til en spesifikk mission som har et bilde tilknyttet (dvs. imagePath er ikke null)
    
    List<Detection> findByMissionIdAndImagePathIsNotNull(Long missionId);// på den linjen Metode for å utføre spørringen og returnere resultatet som en liste av deteksjoner som oppfyller kriteriene

    
    @Query("SELECT DATE(d.timestamp), COUNT(d) FROM Detection d WHERE d.timestamp >= :startDate GROUP BY DATE(d.timestamp)")// på den linjen Definerer en tilpasset JPQL-spørring for å telle antall deteksjoner per dag, og gruppere resultatene etter dato, med en filter for å inkludere kun deteksjoner som har en timestamp etter en spesifisert startdato
    List<Object[]> countDetectionsByDay(@Param("startDate") LocalDateTime startDate);// på den linjen Metode for å utføre spørringen og returnere resultatet som en liste av objekter, hvor hvert objekt er et array som inneholder datoen og antallet deteksjoner for den datoen
    

    
    @Query("SELECT d FROM Detection d WHERE d.mission.id = :missionId ORDER BY d.timestamp DESC LIMIT 1")// på den linjen Definerer en tilpasset JPQL-spørring for å finne den nyeste deteksjonen knyttet til en spesifikk mission basert på timestamp, og begrense resultatet til kun én deteksjon
    Detection findTopByMissionIdOrderByTimestampDesc(@Param("missionId") Long missionId);// på den linjen Metode for å utføre spørringen og returnere den nyeste deteksjonen for den spesifikke missionen
    
    @Query("SELECT COUNT(d) FROM Detection d WHERE d.mission.id = :missionId")// på den linjen Definerer en tilpasset JPQL-spørring for å telle antall deteksjoner knyttet til en spesifikk mission
    long countByMissionId(@Param("missionId") Long missionId);// på den linjen Metode for å utføre spørringen og returnere antallet deteksjoner for den spesifikke missionen som en long-verdi
    
    List<Detection> findByTimestampBefore(LocalDateTime timestamp);// på den linjen Definerer en metode for å finne alle deteksjoner som har en timestamp før en spesifisert dato, som vil bli implementert av Spring Data JPA automatisk
}