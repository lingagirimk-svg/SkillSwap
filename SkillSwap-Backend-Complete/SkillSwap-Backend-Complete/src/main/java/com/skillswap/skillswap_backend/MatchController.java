package com.skillswap.skillswap_backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;

    public MatchController(StudentRepository studentRepository,
                           SkillRepository skillRepository) {
        this.studentRepository = studentRepository;
        this.skillRepository = skillRepository;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getMatches(@PathVariable Long studentId) {
        Student me = studentRepository.findById(studentId).orElse(null);

        if (me == null) {
            return ResponseEntity.notFound().build();
        }

        List<Skill> mySkills = skillRepository.findByStudentId(studentId);

        Set<String> myLearn = mySkills.stream()
                .filter(s -> "learn".equalsIgnoreCase(s.getType()))
                .map(s -> normalize(s.getName()))
                .collect(Collectors.toSet());

        Set<String> myTeach = mySkills.stream()
                .filter(s -> "teach".equalsIgnoreCase(s.getType()))
                .map(s -> normalize(s.getName()))
                .collect(Collectors.toSet());

        int totalPreferences = myLearn.size() + myTeach.size();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Student candidate : studentRepository.findAll()) {
            if (candidate.getId().equals(studentId)) continue;

            List<Skill> candidateSkills =
                    skillRepository.findByStudentId(candidate.getId());

            Set<String> candidateTeach = candidateSkills.stream()
                    .filter(s -> "teach".equalsIgnoreCase(s.getType()))
                    .map(s -> normalize(s.getName()))
                    .collect(Collectors.toSet());

            Set<String> candidateLearn = candidateSkills.stream()
                    .filter(s -> "learn".equalsIgnoreCase(s.getType()))
                    .map(s -> normalize(s.getName()))
                    .collect(Collectors.toSet());

            Set<String> learningMatches = new HashSet<>(myLearn);
            learningMatches.retainAll(candidateTeach);

            Set<String> teachingMatches = new HashSet<>(myTeach);
            teachingMatches.retainAll(candidateLearn);

            Set<String> allMatches = new HashSet<>(learningMatches);
            allMatches.addAll(teachingMatches);

            int percentage = totalPreferences == 0
                    ? 0
                    : Math.min(100, (int) Math.round(
                            (allMatches.size() * 100.0) / totalPreferences));

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", candidate.getId());
            item.put("name", candidate.getName());
            item.put("email", candidate.getEmail());
            item.put("matchPercentage", percentage);
            item.put("matchingSkills", allMatches);
            item.put("learningMatches", learningMatches);
            item.put("teachingMatches", teachingMatches);

            result.add(item);
        }

        result.sort((a, b) ->
                Integer.compare(
                        (Integer) b.get("matchPercentage"),
                        (Integer) a.get("matchPercentage")));

        return ResponseEntity.ok(result);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
