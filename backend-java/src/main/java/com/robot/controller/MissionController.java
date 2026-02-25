
package com.robot.controller;// På den linjen Klassen ligger i controller-pakken

import com.robot.model.Mission; // på den linjen import the Mission model
import com.robot.service.MissionService; //  på den linjen import the MissionService which contains the business logic for handling missions
import org.springframework.beans.factory.annotation.Autowired; // på den linjen import Autowired for dependency injection of MissionService into this controller
import org.springframework.http.ResponseEntity; // på den linjen import ResponseEntity for handling HTTP responses in a structured way
import org.springframework.web.bind.annotation.*; // Den linjen import necessary Spring Web annotations for defining REST endpoints and handling HTTP requests
import java.util.List;// Den linjen import List for handling lists of missions in responses
import java.util.Map; // Den linjen import Map for handling key-value pairs in responses, if needed

@RestController // på den linjen forteller Spring at denne klassen er en REST-kontroller som skal håndtere HTTP-forespørseler
@RequestMapping("/missions") // På den linjen definerer base-URL for alle endepunktene i denne kontrolleren, som vil være "/missions"
@CrossOrigin(origins = "http://localhost:3000") // på den linjen tillater CORS forespørsler fra frontend-applikasjonen som kjører på localhost:3000
public class MissionController {
    
    @Autowired // på den linjen bruker Spring sin avhengighetsinjeksjon for å injisere en instans av MissionService i denne kontrolleren, slik at vi kan bruke tjenestemetodene for å håndtere missionsdata
    private MissionService missionService; // på den linjen deklarerer en privat variabel for MissionService som skal brukes i denne kontrolleren for å utføre operasjoner relatert til missions
    
    @GetMapping // På den linjen definerer et GET-endepunkt for å hente alle missions, som vil være tilgjengelig på missions
    public ResponseEntity<List<Mission>> getAllMissions() { // på den linjen metode for å håndtere GET-forespørsler til "/missions" og returnere en liste av missions i form av en HTTP-respons
        return ResponseEntity.ok(missionService.getAllMissions()); // på den linjen kaller getAllMissions-metoden i MissionService for å hente alle missions og returnerer dem i en HTTP 200 Ok-respons
    }
    
    @GetMapping("/{id}") // Den linjen definerer et GET-endepunkt for å hente en spesifikk mission basert på ID, som vil være tilgjengelig på "/missions/{id}"
    public ResponseEntity<Mission> getMissionById(@PathVariable Long id) { // Den linjen metode for å håndtere GET-forespørsler til "/missions/{id}" og returnere en spesifikk mission basert på ID som er sendt i URL-en
        return missionService.getMissionById(id)// Den linjen kaller getMissionById-metoden i MissionService for å hente missionen med den spesifikke ID-en
                .map(ResponseEntity::ok) // Den linjen forteller hvis missionen finnes map den til en HTTP 200 Ok-respons
                .orElse(ResponseEntity.notFound().build());// På den linjen hvis missionen ikke finnes returner en HTTP 404 Not Found-respons
    }
    
    @PostMapping // Definerer et POST-endepunkt for å opprette en ny mission, som vil være tilgjengelig på "/missions"
    public ResponseEntity<Mission> createMission(@RequestBody Mission mission) { // på linjen 33 Metode for å håndtere POST-forespørsler til "/missions" og opprette en ny mission basert på dataene som er sendt i forespørselens kropp (request body)
        return ResponseEntity.ok(missionService.createMission(mission)); // Dem linjen kaller createMission-metoden i MissionService for å legge til den nye missionen og returnerer den opprettede missionen i en HTTP 200 OK-respons
    }
    
    @PutMapping("/{id}")//På linjen 37 definerer et PUT-endepunkt for å oppdatere en eksisterende mission basert på ID, som vil være tilgjengelig på "/missions/{id}"
    public ResponseEntity<Mission> updateMission(@PathVariable Long id, @RequestBody Mission mission) {// på linjen 38 Metode for å håndtere PUT-forespørsler til "/missions/{id}" og oppdatere en eksisterende mission basert på ID som er sendt i URL-en og dataene som er sendt i forespørselens kropp (request body)
        return ResponseEntity.ok(missionService.updateMission(id, mission)); // på den linjen  Kaller updateMission-metoden i MissionService for å oppdatere missionen med den spesifikke ID-en og returnerer den oppdaterte missionen i en HTTP 200 Ok-respons
    }
    
    @DeleteMapping("/{id}") // På den linjen definerer et DELETE-endepunkt for å slette en eksisterende mission basert på ID, som vil være tilgjengelig på "/missions/{id}"
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {// på den linjen  Metode for å håndtere DELETE-forespørsler til "/missions/{id}" og slette en eksisterende mission basert på ID som er sendt i URL-en
        missionService.deleteMission(id); 
        // på den linjen  Kaller deleteMission-metoden i MissionService for å slette missionen med den spesifikke ID-en
        return ResponseEntity.ok().build();
        // på den linjen Returnerer en HTTP 200 Ok-respons uten innhold for å indikere at slettingen var vellykket
    }
    
    @GetMapping("/status/{status}") 
    // på den linjen definerer et GET-endepunkt for å hente missions basert på status, som vil være tilgjengelig på "/missions/status/{status}"
    
    public ResponseEntity<List<Mission>> getMissionsByStatus(@PathVariable String status) {
        // på den linjen  Metode for å håndtere GET-forespørsler til "/missions/status/{status}" og returnere en liste av missions basert på status som er sendt i URL-en
        return ResponseEntity.ok(missionService.getMissionsByStatus(status));
        // på den linjen Kaller getMissionsByStatus-metoden i MissionService for å hente missions med den spesifikke statusen og returnerer dem i en HTTP 200 Ok-respons
    }
    
    @GetMapping("/stats") // Den linjen definerer et GET-endepunkt for å hente statistikk om missions, som vil være tilgjengelig på "/missions/stats"
    public ResponseEntity<Map<String, Object>> getMissionStats() {
        // på den linjen Metode for å håndtere GET-forespørsler til "/missions/stats" og returnere statistikk om missions i form av en nøkkel-verdi map
        return ResponseEntity.ok(missionService.getMissionStats());
        // på den linjen Kaller getMissionStats-metoden i MissionService for å hente statistikk om missions og returnerer den i en HTTP 200 Ok-respons
    }
    
    @PostMapping("/{id}/start")// På den linjen definerer et POST-endepunkt for å starte en mission basert på ID, som vil være tilgjengelig på "/missions/{id}/start"
    public ResponseEntity<Mission> startMission(@PathVariable Long id) {
        // på den linjen  Metode for å håndtere POST-forespørsler til "/missions/{id}/start" og starte en mission basert på ID som er sendt i URL-en
        return ResponseEntity.ok(missionService.startMission(id));
        // på den linjen Kaller startMission-metoden i MissionService for å starte missionen med den spesifikke ID-en og returnerer den oppdaterte missionen i en HTTP 200 Ok-respons
    }
    
    @PostMapping("/{id}/complete")// På den linjen definerer et POST-endepunkt for å fullføre en mission basert på ID, som vil være tilgjengelig på "/missions/{id}/complete"
    public ResponseEntity<Mission> completeMission(@PathVariable Long id) {
        // Den linjen  Metode for å håndtere POST-forespørsler til "/missions/{id}/complete" og fullføre en mission basert på ID som er sendt i URL-en
        return ResponseEntity.ok(missionService.completeMission(id));
        // Den linjen Kaller completeMission-metoden i MissionService for å fullføre missionen med den spesifikke ID-en og returnerer den oppdaterte missionen i en HTTP 200 Ok-respons
    }
}