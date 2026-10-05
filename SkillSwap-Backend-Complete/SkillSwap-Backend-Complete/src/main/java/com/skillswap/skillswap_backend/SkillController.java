package com.skillswap.skillswap_backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "*")
public class SkillController {

    private final SkillRepository skillRepository;
    private final StudentRepository studentRepository;

    public SkillController(SkillRepository skillRepository, StudentRepository studentRepository) {
        this.skillRepository = skillRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping
    public ResponseEntity<?> addSkill(@RequestBody Skill skill) {
        if (skill.getStudentId() == null || skill.getName() == null || skill.getName().isBlank()) {
            return ResponseEntity.badRequest().body("Student and skill name are required.");
        }

        if (!studentRepository.existsById(skill.getStudentId())) {
            return ResponseEntity.badRequest().body("Student not found.");
        }

        if (!"learn".equalsIgnoreCase(skill.getType()) && !"teach".equalsIgnoreCase(skill.getType())) {
            return ResponseEntity.badRequest().body("Skill type must be learn or teach.");
        }

        skill.setName(skill.getName().trim());
        skill.setType(skill.getType().toLowerCase());

        return ResponseEntity.ok(skillRepository.save(skill));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<Skill>> getStudentSkills(@PathVariable Long studentId) {
        return ResponseEntity.ok(skillRepository.findByStudentId(studentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSkill(@PathVariable Long id) {
        if (!skillRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        skillRepository.deleteById(id);
        return ResponseEntity.ok("Skill deleted successfully.");
    }
}
