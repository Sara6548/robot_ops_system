
package com.robot.controller; 


import com.robot.model.SensorData;
import com.robot.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sensor")
@CrossOrigin(origins = "http://localhost:3000")

public class SensorController {
    @Autowired

    private SensorService sensorService;

    @GetMapping
    public ResponseEntity<List<SensorData>> getAllSensorData(){
        return ResponseEntity.ok(sensorService.getAllSensorData());

    }

    @GetMapping("/mission/{missionId}")
    public ResponseEntity<List<SensorData>> getSensorDataByMission(@PathVariable Long missionId) {
        return ResponseEntity.ok(sensorService.getSensorDataByMission(missionId));
    }

    @PostMapping
    public ResponseEntity<SensorData> addSensorData(@RequestBody SensorData sensorData){
        return ResponseEntity.ok(sensorService.addSensorData(sensorData));
}
   @PostMapping("/batch")
   public ResponseEntity<List<SensorData>> addSensorDataBatch(@RequestBody List<SensorData> sensorDataList) {
        return ResponseEntity.ok(sensorService.addSensorDataBatch(sensorDataList));


   }

   @GetMapping("/types")
   public ResponseEntity<List<Map<String, Object>>> getSensorTypes(){
        return ResponseEntity.ok(sensorService.getSensorTypesWithStats());

   }

   @GetMapping("/realtime")
   public ResponseEntity<List<SensorData>>getRecentSensorData(){
        return ResponseEntity.ok(sensorService.getRecentSensorData());

        
   }
}