package com.skillswap.skillswap_backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class ConnectionRequestController {

    private final ConnectionRequestRepository requestRepository;
    private final ConnectionRepository connectionRepository;
    private final StudentRepository studentRepository;

    public ConnectionRequestController(
            ConnectionRequestRepository requestRepository,
            ConnectionRepository connectionRepository,
            StudentRepository studentRepository) {
        this.requestRepository = requestRepository;
        this.connectionRepository = connectionRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping
    public ResponseEntity<?> sendRequest(@RequestBody ConnectionRequest request) {
        if (request.getRequesterId() == null || request.getReceiverId() == null) {
            return ResponseEntity.badRequest().body("Requester and receiver are required.");
        }

        if (request.getRequesterId().equals(request.getReceiverId())) {
            return ResponseEntity.badRequest().body("You cannot connect with yourself.");
        }

        if (!studentRepository.existsById(request.getRequesterId())
                || !studentRepository.existsById(request.getReceiverId())) {
            return ResponseEntity.badRequest().body("Student not found.");
        }

        if (connectionRepository
                .existsByStudent1IdAndStudent2IdOrStudent1IdAndStudent2Id(
                        request.getRequesterId(), request.getReceiverId(),
                        request.getReceiverId(), request.getRequesterId())) {
            return ResponseEntity.badRequest().body("Already connected.");
        }

        var existing = requestRepository
                .findByRequesterIdAndReceiverId(
                        request.getRequesterId(), request.getReceiverId());

        if (existing.isPresent()) {
            ConnectionRequest old = existing.get();
            if ("PENDING".equals(old.getStatus())) {
                return ResponseEntity.badRequest().body("Request already sent.");
            }
            if ("REJECTED".equals(old.getStatus())) {
                old.setStatus("PENDING");
                return ResponseEntity.ok(requestRepository.save(old));
            }
        }

        request.setStatus("PENDING");
        return ResponseEntity.ok(requestRepository.save(request));
    }

    @GetMapping("/sent/{studentId}")
    public ResponseEntity<List<ConnectionRequest>> sent(@PathVariable Long studentId) {
        return ResponseEntity.ok(requestRepository.findByRequesterId(studentId));
    }

    @GetMapping("/received/{studentId}")
    public ResponseEntity<List<ConnectionRequest>> received(@PathVariable Long studentId) {
        return ResponseEntity.ok(requestRepository.findByReceiverId(studentId));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        ConnectionRequest request = requestRepository.findById(id).orElse(null);

        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        request.setStatus("ACCEPTED");
        requestRepository.save(request);

        boolean alreadyConnected =
                connectionRepository
                        .existsByStudent1IdAndStudent2IdOrStudent1IdAndStudent2Id(
                                request.getRequesterId(), request.getReceiverId(),
                                request.getReceiverId(), request.getRequesterId());

        if (!alreadyConnected) {
            connectionRepository.save(
                    new Connection(request.getRequesterId(), request.getReceiverId()));
        }

        return ResponseEntity.ok(request);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        ConnectionRequest request = requestRepository.findById(id).orElse(null);

        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        request.setStatus("REJECTED");
        return ResponseEntity.ok(requestRepository.save(request));
    }
}
