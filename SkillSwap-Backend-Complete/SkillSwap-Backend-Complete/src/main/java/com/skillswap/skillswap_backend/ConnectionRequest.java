package com.skillswap.skillswap_backend;

import jakarta.persistence.*;

@Entity
@Table(name = "connection_requests")
public class ConnectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long requesterId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private String status;

    public ConnectionRequest() {}

    public ConnectionRequest(Long requesterId, Long receiverId, String status) {
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.status = status;
    }

    public Long getId() { return id; }

    public Long getRequesterId() { return requesterId; }
    public void setRequesterId(Long requesterId) { this.requesterId = requesterId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
