package com.skillswap.skillswap_backend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findByStudent1IdOrStudent2Id(Long student1Id, Long student2Id);
    boolean existsByStudent1IdAndStudent2Id(Long student1Id, Long student2Id);
    boolean existsByStudent1IdAndStudent2IdOrStudent1IdAndStudent2Id(
            Long student1Id, Long student2Id,
            Long student2IdReverse, Long student1IdReverse);
}
