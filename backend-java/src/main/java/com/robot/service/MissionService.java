
package com.robot.service;

import com.robot.model.Mission;
import com.robot.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;



@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

    public List<Mission> getAllMissions(){
        return missionRepository.findAll();

    }
    public Optional<Mission> getMissionById(Long id ){
        return missionRepository.findById(id);
    }

    @Transactional
     public Mission createMission(Mission mission){
        mission.setStatus(Mission.MissionStatus.PLANNED);
        mission.setCreatedAt(LocalDateTime.now());
        return missionRepository.save(mission);
     }

     @Transactional
     public Mission updateMission(Long id, Mission missionDetails){
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found "));

                mission.setName(missionDetails.getName());
                mission.setDescription(missionDetails.getDescription());
                mission.setDroneType(missionDetails.getDroneType());
                mission.setLocation(missionDetails.getLocation());
                mission.setUpdatedAt(LocalDateTime.now());
                return missionRepository.save(mission);

            
     }

        @Transactional 
        public void deleteMission(Long id ){
            missionRepository.deleteById(id);
        }

        public List<Mission> getMissionsByStatus(String status){
            Mission.MissionStatus missionStatus = Mission.MissionStatus.valueOf(status.toUpperCase());
            return missionRepository.findByStatus(missionStatus);
        }

        @Transactional
        public Mission startMission(Long id){
            Mission mission = missionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Mission not found "));

                    mission.setStatus(Mission.MissionStatus.ACTIVE);
                    mission.setStartTime(LocalDateTime.now());
                    mission.setUpdatedAt(LocalDateTime.now());
                    return missionRepository.save(mission);
        }

        @Transactional
        public Mission completeMission(Long id){
            Mission mission = missionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Mission not found "));

                    mission.setStatus(Mission.MissionStatus.COMPLETED);
                    mission.setEndTime(LocalDateTime.now());
                    mission.setUpdatedAt(LocalDateTime.now());


                    return missionRepository.save(mission);
        }

        public Map<String, Object> getMissionStats(){
            Map<String, Object> stats = new HashMap<>();

            List<Mission> allMissions = missionRepository.findAll();
            stats.put("totalMissions", allMissions.size());
            stats.put("activeMissions", missionRepository.countActiveMissions());

            long completed = allMissions.stream()
                    .filter(m -> m.getStatus() == Mission.MissionStatus.COMPLETED)
                    .count();

             stats.put("completedMissions", completed);
             
             long failed = allMissions.stream()
                    .filter(m -> m.getStatus() == Mission.MissionStatus.FAILED)
                    .count();
             stats.put("failedMissions", failed);
             return stats;
}
}