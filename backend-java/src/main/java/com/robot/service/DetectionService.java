package com.robot.service;

import com.robot.model.Detection;
import com.robot.model.Mission;
import com.robot.repository.DetectionRepository;
import com.robot.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.scheduling.annotation.Async;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class DetectionService { // på den linjen som MissionService og SensorDataService, legg til @Service og class DetectionService
    
    private static final Logger logger = LoggerFactory.getLogger(DetectionService.class);
    // på den linjen over, legg til logger for å logge info, debug og error meldinger
    @Autowired
    private DetectionRepository detectionRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${detection.image.storage.path:/tmp/detections}")
    private String imageStoragePath;
    
    @Value("${detection.confidence.threshold:0.5}")
    private double confidenceThreshold;
    

    
    public List<Detection> getAllDetections() {
        logger.info("Henter alle deteksjoner");
        return detectionRepository.findAll();
    }
    
    public List<Detection> getDetectionsByMission(Long missionId) {
        logger.info("Henter deteksjoner for mission ID: {}", missionId);
        return detectionRepository.findByMissionId(missionId);
    }
    
    public List<Detection> getDetectionsByObjectClass(String objectClass) {
        logger.info("Henter deteksjoner for objektklasse: {}", objectClass);
        return detectionRepository.findByObjectClass(objectClass);
    }
    
    public Optional<Detection> getDetectionById(Long id) {
        logger.info("Henter deteksjon med ID: {}", id);
        return detectionRepository.findById(id);
    }
    
    @Transactional
    public Detection addDetection(Detection detection) {
        logger.info("Legger til ny deteksjon");
        
        if (detection.getMission() != null && detection.getMission().getId() != null) {
            Mission mission = missionRepository.findById(detection.getMission().getId())
                .orElseThrow(() -> new RuntimeException("Mission not found with id: " + detection.getMission().getId()));
            detection.setMission(mission);
        }
        
        if (detection.getTimestamp() == null) {
            detection.setTimestamp(LocalDateTime.now());
        }
        
        Detection savedDetection = detectionRepository.save(detection);
        logger.info("Deteksjon lagret med ID: {}", savedDetection.getId());
        
        return savedDetection;
    }
    
    @Transactional
    public List<Detection> addDetectionsBatch(List<Detection> detections) {
        logger.info("Legger til {} deteksjoner i batch", detections.size());
        
        List<Detection> savedDetections = new ArrayList<>();
        for (Detection detection : detections) {
            try {
                savedDetections.add(addDetection(detection));
            } catch (Exception e) {
                logger.error("Feil ved lagring av deteksjon: {}", e.getMessage());
            }
        }
        
        logger.info("{} deteksjoner lagret i batch", savedDetections.size());
        return savedDetections;
    }
    
    @Transactional
    public Detection updateDetection(Long id, Detection detectionDetails) {
        logger.info("Oppdaterer deteksjon med ID: {}", id);
        
        Detection detection = detectionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Detection not found with id: " + id));
        
        if (detectionDetails.getObjectClass() != null) {
            detection.setObjectClass(detectionDetails.getObjectClass());
        }
        if (detectionDetails.getConfidence() != null) {
            detection.setConfidence(detectionDetails.getConfidence());
        }
        if (detectionDetails.getX() != null) {
            detection.setX(detectionDetails.getX());
        }
        if (detectionDetails.getY() != null) {
            detection.setY(detectionDetails.getY());
        }
        if (detectionDetails.getWidth() != null) {
            detection.setWidth(detectionDetails.getWidth());
        }
        if (detectionDetails.getHeight() != null) {
            detection.setHeight(detectionDetails.getHeight());
        }
        if (detectionDetails.getImagePath() != null) {
            detection.setImagePath(detectionDetails.getImagePath());
        }
        
     
        
        Detection updatedDetection = detectionRepository.save(detection);
        logger.info("Deteksjon oppdatert: {}", updatedDetection.getId());
        
        return updatedDetection;
    }
    
    @Transactional
    public void deleteDetection(Long id) {
        logger.info("Sletter deteksjon med ID: {}", id);
        
        Detection detection = detectionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Detection not found with id: " + id));
        
        if (detection.getImagePath() != null) {
            try {
                Path imagePath = Paths.get(detection.getImagePath());
                Files.deleteIfExists(imagePath);
                logger.debug("Bildefil slettet: {}", detection.getImagePath());
            } catch (IOException e) {
                logger.error("Kunne ikke slette bildefil: {}", e.getMessage());
            }
        }
        
        detectionRepository.delete(detection);
        logger.info("Deteksjon slettet");
    }
    
 
    
    @Transactional
    public Detection processYoloDetection(Map<String, Object> yoloData) {
        logger.debug("Prosesserer YOLO deteksjon");
        
        Detection detection = new Detection();
        
        Object missionIdObj = yoloData.get("mission_id");
        if (missionIdObj != null) {
            Long missionId = Long.parseLong(missionIdObj.toString());
            Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found: " + missionId));
            detection.setMission(mission);
        }
        
        detection.setObjectClass((String) yoloData.get("class"));
        
        Object confidenceObj = yoloData.get("confidence");
        if (confidenceObj != null) {
            detection.setConfidence(Double.parseDouble(confidenceObj.toString()));
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> bbox = (Map<String, Object>) yoloData.get("bbox");
        if (bbox != null) {
            detection.setX(getIntegerValue(bbox.get("x")));
            detection.setY(getIntegerValue(bbox.get("y")));
            detection.setWidth(getIntegerValue(bbox.get("width")));
            detection.setHeight(getIntegerValue(bbox.get("height")));
        }
        
        
        
        detection.setTimestamp(LocalDateTime.now());
        
        if (detection.getConfidence() != null && detection.getConfidence() < confidenceThreshold) {
            logger.debug("Deteksjon under confidence threshold, ignorerer");
            return null;
        }
        
        Detection savedDetection = detectionRepository.save(detection);
        logger.info("YOLO deteksjon lagret: {} ({})", savedDetection.getObjectClass(), savedDetection.getConfidence());
        
        return savedDetection;
    }
    
    @Transactional
    public Detection processDetectionWithImage(MultipartFile file, Long missionId, 
                                              String objectClass, Double confidence, 
                                              String boundingBoxJson) {
        logger.info("Prosesserer deteksjon med bilde for mission: {}", missionId);
        
        try {
            String imagePath = saveImage(file, missionId);
            
            Detection detection = new Detection();
            
            Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found: " + missionId));
            detection.setMission(mission);
            
            detection.setObjectClass(objectClass);
            detection.setConfidence(confidence);
            detection.setImagePath(imagePath);
            
            if (boundingBoxJson != null && !boundingBoxJson.isEmpty()) {
                Map<String, Object> bbox = objectMapper.readValue(boundingBoxJson, new TypeReference<Map<String, Object>>() {});
                detection.setX(getIntegerValue(bbox.get("x")));
                detection.setY(getIntegerValue(bbox.get("y")));
                detection.setWidth(getIntegerValue(bbox.get("width")));
                detection.setHeight(getIntegerValue(bbox.get("height")));
            }
            
            detection.setTimestamp(LocalDateTime.now());
            
            Detection savedDetection = detectionRepository.save(detection);
            logger.info("Deteksjon med bilde lagret: {}", savedDetection.getId());
            
            return savedDetection;
            
        } catch (Exception e) {
            logger.error("Feil ved prosessering av deteksjon med bilde: {}", e.getMessage());
            throw new RuntimeException("Failed to process detection with image", e);
        }
    }
    
    private String saveImage(MultipartFile file, Long missionId) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String datePath = String.format("mission_%d/%d/%02d/%02d", 
            missionId, now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        
        Path directoryPath = Paths.get(imageStoragePath, datePath);
        Files.createDirectories(directoryPath);
        
        String timestamp = now.format(DateTimeFormatter.ofPattern("HHmmss_SSS"));
        String filename = String.format("detection_%s_%s.jpg", timestamp, UUID.randomUUID().toString().substring(0, 8));
        
        Path filePath = directoryPath.resolve(filename);
        Files.write(filePath, file.getBytes());
        
        return filePath.toString();
    }
    
   
    
    public Map<String, Object> getDetectionStats() {
        logger.info("Henter deteksjonsstatistikk");
        
        Map<String, Object> stats = new HashMap<>();
        
        long totalDetections = detectionRepository.count();
        stats.put("totalDetections", totalDetections);
        
        List<Object[]> detectionsByClass = detectionRepository.countDetectionsByObjectClass();
        List<Map<String, Object>> classStats = new ArrayList<>();
        for (Object[] row : detectionsByClass) {
            Map<String, Object> classStat = new HashMap<>();
            classStat.put("objectClass", row[0]);
            classStat.put("count", row[1]);
            classStats.add(classStat);
        }
        stats.put("detectionsByClass", classStats);
        
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Object[]> dailyDetections = detectionRepository.countDetectionsByDay(thirtyDaysAgo);
        List<Map<String, Object>> dailyStats = new ArrayList<>();
        for (Object[] row : dailyDetections) {
            Map<String, Object> dailyStat = new HashMap<>();
            dailyStat.put("date", row[0].toString());
            dailyStat.put("count", row[1]);
            dailyStats.add(dailyStat);
        }
        stats.put("dailyDetections", dailyStats);
        
        Double avgConfidence = detectionRepository.findAverageConfidence();
        stats.put("avgConfidence", avgConfidence != null ? avgConfidence : 0.0);
        
        return stats;
    }
    
    public Map<String, Object> getDetectionStatsByMission(Long missionId) {
        logger.info("Henter deteksjonsstatistikk for mission: {}", missionId);
        
        Map<String, Object> stats = new HashMap<>();
        
        long totalDetections = detectionRepository.countByMissionId(missionId);
        stats.put("totalDetections", totalDetections);
        
        List<Object[]> detectionsByClass = detectionRepository.countDetectionsByObjectClassForMission(missionId);
        List<Map<String, Object>> classStats = new ArrayList<>();
        for (Object[] row : detectionsByClass) {
            Map<String, Object> classStat = new HashMap<>();
            classStat.put("objectClass", row[0]);
            classStat.put("count", row[1]);
            classStat.put("avgConfidence", row[2]);
            classStats.add(classStat);
        }
        stats.put("detectionsByClass", classStats);
        
        Double avgConfidence = detectionRepository.findAverageConfidenceByMission(missionId);
        stats.put("avgConfidence", avgConfidence != null ? avgConfidence : 0.0);
        
        return stats;
    }
    
    public List<Detection> getRecentDetections(int limit) {
        logger.info("Henter {} siste deteksjoner", limit);
        return detectionRepository.findRecentDetections(limit);
    }
    
    public List<Detection> getDetectionsBetweenDates(LocalDateTime start, LocalDateTime end) {
        logger.info("Henter deteksjoner mellom {} og {}", start, end);
        return detectionRepository.findByTimestampBetween(start, end);
    }
    
    
    
    private Integer getIntegerValue(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Number) return ((Number) obj).intValue();
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private Double getDoubleValue(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Number) return ((Number) obj).doubleValue();
        try {
            return Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private String convertToJsonString(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            logger.error("Kunne ikke konvertere til JSON: {}", e.getMessage());
            return "{}";
        }
    }
    
    private LocalDateTime parseTimestamp(String timestamp) {
        try {
            if (timestamp.contains("T")) {
                return LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
            } else {
                return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            logger.warn("Kunne ikke parse timestamp: {}, bruker nåværende tid", timestamp);
            return LocalDateTime.now();
        }
    }
    
    
    
    public Map<String, Object> findDetectionPatterns(Long missionId) {
        logger.info("Analyserer deteksjonsmønstre for mission: {}", missionId);
        
        List<Detection> detections = getDetectionsByMission(missionId);
        
        Map<String, List<String>> timeGroups = new HashMap<>();
        
        for (Detection d : detections) {
            String timeKey = d.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
            timeGroups.computeIfAbsent(timeKey, k -> new ArrayList<>()).add(d.getObjectClass());
        }
        
        Map<String, Integer> correlations = new HashMap<>();
        for (List<String> objects : timeGroups.values()) {
            Set<String> uniqueObjects = new HashSet<>(objects);
            if (uniqueObjects.size() > 1) {
                String key = String.join("+", uniqueObjects);
                correlations.put(key, correlations.getOrDefault(key, 0) + 1);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalTimeGroups", timeGroups.size());
        result.put("patterns", correlations);
        
        return result;
    }
    
   
    public String exportDetectionsToCsv(Long missionId) {
        logger.info("Eksporterer deteksjoner til CSV for mission: {}", missionId);
        
        List<Detection> detections = getDetectionsByMission(missionId);
        
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Tid,Objektklasse,Confidence,Posisjon X,Posisjon Y,Bilde\n");
        
        for (Detection d : detections) {
            csv.append(String.format("%d,%s,%s,%.2f,%d,%d,%s\n",
                d.getId(),
                d.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                d.getObjectClass(),
                d.getConfidence(),
                d.getX() != null ? d.getX() : 0,
                d.getY() != null ? d.getY() : 0,
                d.getImagePath() != null ? d.getImagePath() : "N/A"
            ));
        }
        
        return csv.toString();
    }
    
    public String exportDetectionsToJson(Long missionId) {
        logger.info("Eksporterer deteksjoner til JSON for mission: {}", missionId);
        
        List<Detection> detections = getDetectionsByMission(missionId);
        
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(detections);
        } catch (Exception e) {
            logger.error("Feil ved eksportering til JSON: {}", e.getMessage());
            return "[]";
        }
    }
}