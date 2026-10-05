package com.skillswap.skillswap_backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/connections")
@CrossOrigin(origins = "*")
public class ConnectionController {

    private final ConnectionRepository connectionRepository;
    private final StudentRepository studentRepository;

    public ConnectionController(ConnectionRepository connectionRepository,
                                StudentRepository studentRepository) {
        this.connectionRepository = connectionRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getConnections(@PathVariable Long studentId) {
        List<Connection> connections =
                connectionRepository.findByStudent1IdOrStudent2Id(studentId, studentId);

        var result = connections.stream().map(connection -> {
            Long otherId = connection.getStudent1Id().equals(studentId)
                    ? connection.getStudent2Id()
                    : connection.getStudent1Id();

            Student other = studentRepository.findById(otherId).orElse(null);

            return new ConnectionResponse(
                    connection.getId(),
                    otherId,
                    other != null ? other.getName() : "Unknown",
                    other != null ? other.getEmail() : ""
            );
        }).toList();

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConnection(@PathVariable Long id) {
        if (!connectionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        connectionRepository.deleteById(id);
        return ResponseEntity.ok("Connection removed.");
    }

    public record ConnectionResponse(
            Long connectionId,
            Long studentId,
            String name,
            String email
    ) {}
}
