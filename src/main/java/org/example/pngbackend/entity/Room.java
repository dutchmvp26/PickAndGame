package org.example.pngbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String roomCode;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 300)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityTypeEntity activityType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomIconEntity icon;

    @Column(nullable = false)
    private int numberOfPlayers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatusEntity status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Room() {
    }

    public Long getId() {
        return id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActivityTypeEntity getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityTypeEntity activityType) {
        this.activityType = activityType;
    }

    public RoomIconEntity getIcon() {
        return icon;
    }

    public void setIcon(RoomIconEntity icon) {
        this.icon = icon;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public RoomStatusEntity getStatus() {
        return status;
    }

    public void setStatus(RoomStatusEntity status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
