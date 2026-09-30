package org.example.pngbackend.entity;

import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String roomCode;

    @Column(nullable = false)
    private int numberOfPlayers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Room() {
    }

    public Room(String roomCode, int numberOfPlayers) {
        this.roomCode = roomCode;
        this.numberOfPlayers = numberOfPlayers;
        this.status = RoomStatus.WAITING;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}