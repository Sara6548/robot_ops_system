package com.robot.model; // Klassen ligger i model-pakken

import jakarta.persistence.*; // Importerer JPA-annotasjoner for database

import java.time.LocalDateTime; // Importerer dato og tid

@Entity // Forteller at denne klassen er en database-tabell 
@Table(name = "detections") // Navent på tabellen i database  er "detections"
public class Detection {
    
    @Id // Primærnøkkel for tabellen
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //   Genererer unike ID-er automatisk
    private Long id;// ID for deteksjonen, som er unik for hver deteksjon i databasen
    
    @ManyToOne // Mange deteksjoner kan tilhøre en mission
    @JoinColumn(name = "mission_id", nullable = false)
    //  lager en kolonne "mission_id" i "detections" tabellen som refererer til "missions" tabellen
    private Mission mission;// referanse til mission-objektet som deteksjonen tilhører, som representerer oppdraget eller konteksten for deteksjonen
    
    
    private String objectClass;
    // klassen av objektet som er detektert, f.eks. "person", "bil", "sykkel"

    private Double confidence;
    // hvor sikker modellen er på at det den har detektert faktisk er det den tror det er (f.eks. 0.85 for 85% sikkerhet)
    
    private Integer x;
    // x-koordinaten for deteksjonen i bildet, som representerer det horisontale startpunktet for detekteringsboksen
    private Integer y;
    // y-koordinaten for deteksjonen i bildet, som representerer det vertikale startpunktet for detekteringsboksen
    private Integer width;
    // bredden på deteksjonen i bildet, som representerer hvor bred detekteringsboksen er i forhold til x-koordinaten
    private Integer height;
    // høyden på deteksjoner i bildet, som representerer hvor høy detekteringsboksen er  i forhold til y-koordonaten
    
    private String imagePath;
    // hvor bildet av deteksjonen er lagret, kan være en filsti eller URL
    
    private LocalDateTime timestamp = LocalDateTime.now();
    // tidspunktet for deteksjonen, som standard settes til nåværende tid når en ny deteksjon opprettes 
    //  GETTERE OG SETTERE - standard metoder for å hente og sette verdier på feltene
    public Long getId() { 
        return id; // returnerer ID-en til deteksjonen

     }
    public void setId(Long id) { 
        this.id = id; // setter ID-en til deteksjonen
    }
    
    public Mission getMission() {
         return mission; // returnerer mission-objektet som deteksjonen tilhører
        }
    public void setMission(Mission mission) { 
        this.mission = mission; // setter mission-objektet 
     }
    
    public String getObjectClass() {
         return objectClass; // returnerer klassen av  objektet som er detektert 
        }
    public void setObjectClass(String objectClass) { 
        this.objectClass = objectClass; // setter objektklassen
     }
    
    public Double getConfidence() {
         return confidence; // returnerer confidence
        }
    public void setConfidence(Double confidence) {
         this.confidence = confidence; } // setter confidence
    
    public Integer getX() { 
        return x; // returnerer x-koordinaten
     }
    public void setX(Integer x) { 
        this.x = x; // setter x-koordinaten
    }
    
    public Integer getY() {
         return y;  // returnerer y-koordinaten
        }
    public void setY(Integer y) { 
        this.y = y; // setter y-koordinaten
    }
    
    public Integer getWidth() {
         return width; // returnerer bredden på deteksjonen i bildet
        }
    public void setWidth(Integer width) {
         this.width = width;// setter bredden på deteksjonen i bildet
         }
    
    public Integer getHeight() { 
        return height; // returnerer høyden på deteksjonen i bildet

     }
    public void setHeight(Integer height) {
         this.height = height; // setter høyden på deteksjonen i bildet
         }
    
    public String getImagePath() { 
        return imagePath; // returnerer stien til bildet av deteksjonen 
     }
    public void setImagePath(String imagePath) { 
        this.imagePath = imagePath; // setter stien til bildet av deteksjonen
     }
    
    public LocalDateTime getTimestamp() { 
        return timestamp; // returnerer tidspunktet for deteksjonen
     }
    public void setTimestamp(LocalDateTime timestamp) { 
        this.timestamp = timestamp; // setter tidspunktet for deteksjonen 
     }
}
