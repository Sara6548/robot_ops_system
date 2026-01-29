
CREATE DATABASE IF NOT EXISTS robot_system;
USE robot_system;

CREATE TABLE  missions (COMMENT
 mission_id INT AUTO_INCREMENT PRIMARY KEY,
 mission_name VARCHAR(100) NOT NULL;
 mission_description TEXT,
 status ENUM('pending','active', 'completed', 'failed') DEFAULT 'PENDING',
drop_type VARCHAR(50),
location VARCHAR(100),
start_time DATETIME,
end_time DATETIME,
create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP


);


CREATE TABLE sensor_data (
    senor_id INT AUTO_INCREMENT PEIMARY KEY,
    mission_id INT;
    sensor_type ENUM('temperature', 'humidity', 'gps', 'lidar', 'camera', 'proximity'),
    VALUE VARCHAR(255),
    unit VARCHAR(20),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mission_id) REFERENCES missions(mission_id) ON DELETE CASCADE,
    INDEX odx_mission_id (mission_id, timestamp)
);
