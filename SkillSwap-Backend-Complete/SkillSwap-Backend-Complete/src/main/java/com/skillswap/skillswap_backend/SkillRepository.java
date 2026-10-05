package com.skillswap.skillswap_backend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByStudentId(Long studentId);
    List<Skill> findByStudentIdAndType(Long studentId, String type);
}
