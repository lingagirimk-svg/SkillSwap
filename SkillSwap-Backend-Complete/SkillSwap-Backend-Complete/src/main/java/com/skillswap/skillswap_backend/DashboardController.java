package com.skillswap.skillswap_backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;
    private final ConnectionRepository connectionRepository;
    private final ConnectionRequestRepository requestRepository;

    public DashboardController(
            StudentRepository studentRepository,
            SkillRepository skillRepository,
            ConnectionRepository connectionRepository,
            ConnectionRequestRepository requestRepository) {
        this.studentRepository = studentRepository;
        this.skillRepository = skillRepository;
        this.connectionRepository = connectionRepository;
        this.requestRepository = requestRepository;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getDashboard(@PathVariable Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            return ResponseEntity.notFound().build();
        }

        var skills = skillRepository.findByStudentId(studentId);

        long learning = skills.stream()
                .filter(s -> "learn".equalsIgnoreCase(s.getType()))
                .count();

        long teaching = skills.stream()
                .filter(s -> "teach".equalsIgnoreCase(s.getType()))
                .count();

        long connections =
                connectionRepository.findByStudent1IdOrStudent2Id(studentId, studentId).size();

        long pendingRequests =
                requestRepository.findByReceiverId(studentId).stream()
                        .filter(r -> "PENDING".equalsIgnoreCase(r.getStatus()))
                        .count();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("studentId", studentId);
        data.put("learningSkills", learning);
        data.put("teachingSkills", teaching);
        data.put("connectionCount", connections);
        data.put("pendingRequests", pendingRequests);

        return ResponseEntity.ok(data);
    }
}
