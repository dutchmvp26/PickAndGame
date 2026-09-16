package org.example.pngbackend.dto;

import jakarta.validation.constraints.Min;

public class CreateRoomRequest {

    @Min(value = 2, message = "A room must have at least 2 players")
    private int numberOfPlayers;

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}