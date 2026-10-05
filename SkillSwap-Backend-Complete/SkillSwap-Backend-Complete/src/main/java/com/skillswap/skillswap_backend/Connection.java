package com.skillswap.skillswap_backend;

import jakarta.persistence.*;

@Entity
@Table(name = "connections")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long student1Id;

    @Column(nullable = false)
    private Long student2Id;

    public Connection() {}

    public Connection(Long student1Id, Long student2Id) {
        this.student1Id = student1Id;
        this.student2Id = student2Id;
    }

    public Long getId() { return id; }

    public Long getStudent1Id() { return student1Id; }
    public void setStudent1Id(Long student1Id) { this.student1Id = student1Id; }

    public Long getStudent2Id() { return student2Id; }
    public void setStudent2Id(Long student2Id) { this.student2Id = student2Id; }
}
