
package com.robot.controller; // På den linjer Klassen ligger i controller-pakken

import com.robot.model.Detection; // På den linjer Importerer Detection-modellen som representerer deteksjonsdataene i systemet
import com.robot.service.DetectionService; // På den linjer Importerer DetectionService som inneholder forretningslogikken for håndtering av deteksjonsdata
import org.springframework.beans.factory.annotation.Autowired; // På den linjer Importerer Autowired for avhengighetsinjeksjon av Detectinsionservice i controlleren
import org.springframework.http.ResponseEntity; //På den linjer Importerer ResponseEntity for å håndtere HTTP-responser på en strukturert måte
import org.springframework.web.bind.annotation.*; // På den linjer Importerer nødvendige Spring Web-annotasjoner for å definere REST-endepunkter og håndtere HTTP-forespørsler
import java.util.List; //På den linjen Importerer List for å håndtere lister av deteksjoner i responser
import java.util.Map; //På den linjen  Importerer Map for å håndtere nøkkel-verdi par i responser, hvis nødvendig 



@RestController //på den linjer Forteller Spring at denne klassen er en REST-kontroller som skal håndtere HTTP-forespørseler
@RequestMapping("/detections") // på den Definerer base-URL for alle endepunktene i denne kontrolleren, som vil være "/detections"
@CrossOrigin(origins = "http://localhost:3000") // Tillater CORS forespørsler fra frontend-applikasjonen som kjører på localhost:3000
public class DetectionController {
    
    @Autowired //På den linjen  Bruker Spring sin avhengighetsinjeksjon for å injisere en instans av DetestionService i denne kontrolleren, slik at vi kan bruke tjenestemetodene for å håndtere deteksjonsdata 
    private DetectionService detectionService; // Deklarerer en privat variabel for DetectionService son skal brukes i denne kontrolleren for å utføre operasjoner relatert til deteksjoner 
    
    @GetMapping // På den linjen Definerer et GET-endepunkt for å hente alle deteksjoner, som vil være tilgjengelig på "/detections"
    public ResponseEntity<List<Detection>> getAllDetections() { // Metode for å håndtere GET-forespørsler til "/detections" og returnere en liste av deteksjoner i form av en HTTP-respons
        return ResponseEntity.ok(detectionService.getAllDetections()); // Kaller getAllDetections-metoden i DetectionService for å hente alle deteksjoner og returerer dem i en HTTP 200 Ok-respons
    }
    
    @GetMapping("/{id}") //Den linjen  Definerer et GET-endepunkt for å hente en spesifikk deteksjon basert på ID, som vil være tilgjengelig på "/detections/{id}"
    public ResponseEntity<Detection> getDetectionById(@PathVariable Long id) { //Den linjen Metode for å håndtere GET-forespørsler til "/detections/{id}" og returnere en spesifikk deteksjon basert på ID som er sendt i URL-en 
        return detectionService.getDetectionById(id) //Den linjen Kaller getDetectionById-metoden i DetectionService for å hente deteksjonen med den spesifikke ID-en
                .map(ResponseEntity::ok) // Den linjen forteller hvis deteksjonen finnes map den til en HTTP 200 Ok-respons
                .orElse(ResponseEntity.notFound().build()); // Den linjen Hvis deteksjonen ikke finnes returner en HTTP 404 Not Found-respons
    }
    
    @PostMapping // Definerer et POST-endepunkt for å opprette en ny deteksjon, som vil være tilgjengelig på "/detections"
    public ResponseEntity<Detection> createDetection(@RequestBody Detection detection) { // Metode for å håndtere POST-forespørsler til "/detections" og opprette en ny deteksjon basert på dataene som er sendt i forespørselens kropp (request body)
        return ResponseEntity.ok(detectionService.addDetection(detection)); // Den linjen Kaller addDetection-metoden i DetectionService for å legge til den nye deteksjonen og returnerer den opprettede deteksjonen i en HTTP 200 OK-respons 
    }
    
    @PutMapping("/{id}")// På den linjen definerer et PUT-endepunkt for å oppdatere en eksisterende deteksjon basert på ID, som vil være tilgjengelig på "/detections/{id}"
    public ResponseEntity<Detection> updateDetection(@PathVariable Long id, @RequestBody Detection detection) { // Metode for å håndtere PUT-forespørsler til "/detections/{id}" og oppdatere en eksisterende deteksjon basert på ID som er sendt i URL-en og dataene som er sendt i forespørselens kropp (request body)
        return ResponseEntity.ok(detectionService.updateDetection(id, detection)); // på den linjen  Kaller updateDetection-metoden i DetectionService for å oppdatere deteksjonen med den spesifikke ID-en og returnerer den oppdaterte deteksjonen i en HTTP 200 Ok-respons 
    }
    
    @DeleteMapping("/{id}") // Den linjen  Definerer et DELETE-endepunkt for å slette en eksisterende deteksjon basert på ID, som vil være tilgjengelig på "/detections/{id}"
    public ResponseEntity<Void> deleteDetection(@PathVariable Long id) { //På den linjen  Metode for å håndtere DELETE-forespørsler til "/detections/{id}" og slette en eksisterende deteksjon basert på ID som er sendt i URL-en 
        detectionService.deleteDetection(id); //Den linjen forteller  Kaller deleteDetection-metoden i DetectionService for å slette deteksjonen med den spesifikke ID-en 
        return ResponseEntity.ok().build();// Den linjen Returnerer en HTTP 200 Ok-respons uten innhold for å indikere at slettingen var vellykket 
    }
}