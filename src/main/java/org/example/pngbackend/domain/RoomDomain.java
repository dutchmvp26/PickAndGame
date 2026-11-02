package org.example.pngbackend.domain;

import java.time.LocalDateTime;

public class RoomDomain {

    private final Long id;
    private final String roomCode;
    private final int numberOfPlayers;
    private RoomStatus status;
    private final LocalDateTime createdAt;

    public RoomDomain(
            Long id,
            String roomCode,
            int numberOfPlayers,
            RoomStatus status,
            LocalDateTime createdAt
    ) {
        if (numberOfPlayers < 2) {
            throw new IllegalArgumentException(
                    "A room must have at least 2 players."
            );
        }

        this.id = id;
        this.roomCode = roomCode;
        this.numberOfPlayers = numberOfPlayers;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static RoomDomain create(
            String roomCode,
            int numberOfPlayers
    ) {
        return new RoomDomain(
                null,
                roomCode,
                numberOfPlayers,
                RoomStatus.WAITING,
                LocalDateTime.now()
        );
    }

    public void startVoting() {
        if (status != RoomStatus.WAITING) {
            throw new IllegalStateException(
                    "Voting can only start when the room is waiting."
            );
        }

        status = RoomStatus.VOTING;
    }

    public void finish() {
        if (status != RoomStatus.VOTING) {
            throw new IllegalStateException(
                    "Only a room in voting can be finished."
            );
        }

        status = RoomStatus.FINISHED;
    }

    public boolean isFull(int participantCount) {
        return participantCount >= numberOfPlayers;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}