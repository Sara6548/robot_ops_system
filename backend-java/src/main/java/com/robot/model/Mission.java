package com.robot.model;  // klassen ligger i model-pakken

import jakarta.persistence.*; // Importerer JPA-annotasjoner for database
import java.time.LocalDateTime; // Importerer dato og tid
import java.util.List; // Importerer List for å håndtere lister av sensor data og deteksjoner

@Entity // Forteller at denne klassen er en database-tabell
@Table(name = "missions") // Navnet på tabellen i database er "missions"
public class Mission { // klassen representerer et oppdrag eller en mission som dronen skal utføre 
    
    @Id // Primærnøkkel for tabellen 
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genererer unike ID-er automatisk
    private Long id; // ID for missionen, som er unik for hver mission i databasen
    
    @Column(nullable = false) // Navnet på missionen kan ikke være null i databasen

    private String name; // navnet på missionen, som gir en beskrivelse av hva oppdraget handler om 
    
    private String description;// en mer detaljert beskrivelse av missionen, som kan inneholde informasjon om oppdragets mål 
    
    @Enumerated(EnumType.STRING) // Lagre enum-verdien som en streng i databasen
    private MissionStatus status = MissionStatus.PLANNED; // status for missionen, som kan være PLANNED, ACTIVE, COMPLETED eller FAILED, og standard settes til PLANNED
    
    private String droneType; // typen drone som skal brukes for missionen, f.eks. "DJI Mavic", "Parrot Anafi",etc.
    private String location; // stedet hvor missionen skal utføres, kan være en adresse, koordinater eller et område
    
    private LocalDateTime startTime; // tidspunktet for når missionen starter 
    private LocalDateTime endTime; // tidspunktet for når missionen skal være ferdig eller forventes å være ferdig
    
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL) // En mission kan ha mange sensor data, og når en mission slettes, slettes også tilhørende sensor data
    private List<SensorData> sensorData; 
    // En liste over sensor data som er knyttet til missionen
    
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    // En mission kan ha mange deteksjoner, og når en mission slettes, slettes også tilhørende deteksjoner
    private List<Detection> detections;
    // En liste over deteksjoner som er knyttet til missionen 
    
    private LocalDateTime createdAt = LocalDateTime.now();
    // tidspunktet for når missionen ble opprettet, som standard settes til nåværende tid når en ny mission opprettes 
    private LocalDateTime updatedAt = LocalDateTime.now();
    // tidspunktet for når missionen sist ble oppdatert, som standard settes til nåværende tid når en ny mission opprettes, og bør oppdateres hver gang missionen endres
    public enum MissionStatus {
        // Enum for å representere statusen til en mission, som kan være PLANNED, ACTIVE, COMPLETED eller FAILED
        PLANNED, ACTIVE, COMPLETED, FAILED
    }
    
    // === GETTERE OG SETTERE ===
    public Long getId() { return id; } // returnerer ID en til missionen
    
    public void setId(Long id) { this.id = id; } // setter ID en til missionen
    
    public String getName() { return name; }// returnerer navnet på missionen
    public void setName(String name) { this.name = name; }// setter navnet på missionen
    
    public String getDescription() { return description; } // returnerer beskrivelsen av missionen
    public void setDescription(String description) { this.description = description; } // setter beskrivelsen av missionen
    
    public MissionStatus getStatus() { return status; } // returnerer statusen til missionen
    public void setStatus(MissionStatus status) { this.status = status; } // setter statusen til missionen
    
    public String getDroneType() { return droneType; } // returnerer typen drone som skal brukes for missionen
    public void setDroneType(String droneType) { this.droneType = droneType; } // setter typen drone som skal brukes for missionen 
    
    public String getLocation() { return location; } // returnerer stedet hvor missionen skal utføres 
    public void setLocation(String location) { this.location = location; } // setter stedet hvor missionen skal utføres
    
    public LocalDateTime getStartTime() { return startTime; } // returnerer tidspunktet for når missionen starter 
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }// setter tidspunktet for når missionen starter
    
    public LocalDateTime getEndTime() { return endTime; } // returnerer tidspunktet for når missionen slutter
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; } // setter tidspunktet for når missionen slutter

    public List<SensorData> getSensorData() { return sensorData; } // returnerer listen over sensor data som er knyttet til missionen
    public void setSensorData(List<SensorData> sensorData) { this.sensorData = sensorData; } // setter listen over sensor data som er knyttet til missionen 
    
    public List<Detection> getDetections() { return detections; } // returnerer listen over deteksjoner som er knyttet til missionen

    public void setDetections(List<Detection> detections) { this.detections = detections; }// setter listen over deteksjoner som er knyttet til missionen 
    
    public LocalDateTime getCreatedAt() { return createdAt; } // returnerer tidspunktet for når missionen ble opprettet
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; } // setter tidspunktet for når missionen ble opprettet
    
    public LocalDateTime getUpdatedAt() { return updatedAt; } // returnerer tidspunktet for når missionen sist ble oppdatert
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; } // setter tidspunktet for når missionen sist ble oppdatert
}
