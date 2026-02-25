package com.robot.model; // Klassen ligger i model-pakken

import jakarta.persistence.*; // Importerer JPA-annotasjoner for database
import java.time.LocalDateTime; // Importerer dato og tid

@Entity // Forteller at denne klassen er en database-tabell
@Table(name = "sensor_data") // Navnet på tabellen i database er "sensor_data"
public class SensorData { // klassen representerer sensor data som er knyttet til en mission, og inneholder informasjon om typen sensor, verdien, enheten og tidspunktet for målingen
    
    @Id // Primærnøkkel for tabellen
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genererer unike ID-er automatisk
    private Long id; // ID for sensor data, som er unik for hver måling i databasen
    
    @ManyToOne // Mange sensor data kan tilhøre en mission
    @JoinColumn(name = "mission_id", nullable = false) // lager en kolonne "mission_id" i "sensor_data" tabellen som refererer til "missions" tabellen
    private Mission mission;
    // referanse til mission-objektet som sensor data tilhører, som representerer oppdraget eller konteksten for målingen
    
    @Enumerated(EnumType.STRING)
    // Lagre enum-verdien som en streng i databasen, for å representere typen sensor som har gjort målingen, f.eks. TEMPERATURE, HUMIDITY, GPS, LIDAR, CAMERA eller PROXIMITY
    private SensorType sensorType;
    // typen sensor som har gjort målingen, f.eks. TEMPERATURE, HUMIDITY, GPS, LIDAR, CAMERA eller PROXIMITY
    
    private String value;
    // verdien av målingen, som kan være en temperatur i Celsius, en fuktighet i prosent, GPS-koordinater, etc.
    private String unit;
    // enheten for målingen, som kan være "°C" for temperatur, "%" for fuktighet, "lat,long" for GPS, etc.
    private LocalDateTime timestamp = LocalDateTime.now();
    // tidspunktet for målingen, som standard settes til nåværende tid når en ny sensor data opprettes
    public enum SensorType {
        // Enum for å representere typen sensor som har gjort målingen, f.eks.
        TEMPERATURE, HUMIDITY, GPS, LIDAR, CAMERA, PROXIMITY
    }
    
    // for har starter  GETTERE OG SETTERE - standard metoder for å hente og sette verdier på feltene
    public Long getId() { return id; } // returnerer ID-en til sensor data
    public void setId(Long id) { this.id = id; }// setter ID-en til sensor data
    
    public Mission getMission() { return mission; } // returnerer mission-objektet som sensor data tilhører
    public void setMission(Mission mission) { this.mission = mission; }// setter mission-objektet som sensor data tilhører
    
    public SensorType getSensorType() { return sensorType; }// returnerer typen sensor som har gjort målingen
    public void setSensorType(SensorType sensorType) { this.sensorType = sensorType; } // setter typen sensor som har gjort målingen
    
    public String getValue() { return value; } // returnerer verdien av målingen
    public void setValue(String value) { this.value = value; } // setter verdien av målingen
    
    public String getUnit() { return unit; }// returnerer enheten for målingen
    public void setUnit(String unit) { this.unit = unit; } //  setter enheten for målingen
    
    public LocalDateTime getTimestamp() { return timestamp; } // returnerer tidspunktet for målingen
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }// setter tidspunktet for målingen
}
