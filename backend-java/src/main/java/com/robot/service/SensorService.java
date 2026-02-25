package com.robot.service;

import com.robot.model.Mission;
import com.robot.model.SensorData;
import com.robot.repository.MissionRepository;
import com.robot.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service 
public class SensorService {
    
    @Autowired
    private SensorDataRepository sensorDataRepository;  // RIKTIG navn

    @Autowired
    private MissionRepository missionRepository;

    // Hent alle sensordata
    public List<SensorData> getAllSensorData() {
        return sensorDataRepository.findAll();
    }

    // Hent sensordata for en spesifikk mission (KORRIGERT)
    public List<SensorData> getSensorDataByMissionId(Long missionId) {
        return sensorDataRepository.findByMissionId(missionId);  // Endret fra findByDroneId
    }

    // Legg til ny sensordata (KORRIGERT)
    @Transactional
    public SensorData addSensorData(SensorData sensorData) {
        // Hent mission basert på mission ID (ikke droneId)
        Mission mission = missionRepository.findById(sensorData.getMission().getId())
                .orElseThrow(() -> new RuntimeException("Mission not found with id: " + sensorData.getMission().getId()));
        
        sensorData.setMission(mission);
        sensorData.setTimestamp(LocalDateTime.now());
        
        return sensorDataRepository.save(sensorData);
    }

    // Legg til flere sensordata samtidig
    @Transactional
    public List<SensorData> addSensorDataBatch(List<SensorData> sensorDataList) {
        List<SensorData> savedData = new ArrayList<>();
        for (SensorData data : sensorDataList) {
            savedData.add(addSensorData(data));
        }
        return savedData;
    }

    // Hent sensordata fra siste time
    public List<SensorData> getRecentSensorData() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return sensorDataRepository.findByTimestampBetween(oneHourAgo, LocalDateTime.now());
    }

    // Hent statistikk per sensortype (KORRIGERT)
    public List<Map<String, Object>> getSensorTypesWithStats() {
        List<Map<String, Object>> results = new ArrayList<>();
        List<Object[]> averages = sensorDataRepository.getAverageBySensorType();  // Endret fra getAverageSensorType

        for (Object[] avg : averages) {
            Map<String, Object> map = new HashMap<>();
            map.put("sensorType", avg[0]);
            map.put("averageValue", avg[1]);
            results.add(map);
        }
        return results;
    }

    // Grupper sensordata etter type (KORRIGERT metode-navn)
    public Map<String, List<SensorData>> getSensorDataGroupedByType() {  // Endret fra getSensorDataGropedByType
        Map<String, List<SensorData>> groupedData = new HashMap<>();
        List<SensorData> allData = getAllSensorData();
        
        for (SensorData data : allData) {
            String sensorType = data.getSensorType().name();
            groupedData.computeIfAbsent(sensorType, k -> new ArrayList<>()).add(data);
        }
        return groupedData;
    }

    // Hent sensordata for mission (alternativ metode)
    public List<SensorData> getSensorDataByMission(Long missionId) { 
        return sensorDataRepository.findByMissionId(missionId);  // Endret fra sensorRepository
    }
}