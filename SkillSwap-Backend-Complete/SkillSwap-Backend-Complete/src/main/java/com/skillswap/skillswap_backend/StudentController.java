package com.skillswap.skillswap_backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        if (student.getName() == null || student.getName().isBlank()
                || student.getEmail() == null || student.getEmail().isBlank()
                || student.getPassword() == null || student.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        if (studentRepository.findByEmail(student.getEmail().trim()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered.");
        }

        student.setName(student.getName().trim());
        student.setEmail(student.getEmail().trim());

        return ResponseEntity.ok(studentRepository.save(student));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Student student) {
        Student existing = studentRepository.findByEmail(student.getEmail().trim()).orElse(null);

        if (existing == null) {
            return ResponseEntity.badRequest().body("Email not registered.");
        }

        if (!existing.getPassword().equals(student.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect password.");
        }

        return ResponseEntity.ok(existing);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        return studentRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> getStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id,
            @RequestBody Student updatedStudent) {

        Student existing = studentRepository.findById(id).orElse(null);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedStudent.getName() == null || updatedStudent.getName().isBlank()) {
            return ResponseEntity.badRequest().body("Name is required.");
        }

        existing.setName(updatedStudent.getName().trim());

        return ResponseEntity.ok(studentRepository.save(existing));
    }
}
