package org.example.pngbackend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.pngbackend.domain.ActivityType;
import org.example.pngbackend.domain.RoomIcon;

public class UpdateRoomRequest {

    @NotBlank(message = "Room name is required")
    @Size(max = 50, message = "Room name can be at most 50 characters")
    private String name;

    @Size(max = 300, message = "Description can be at most 300 characters")
    private String description;

    @NotNull(message = "Activity type is required")
    private ActivityType activityType;

    @NotNull(message = "Room icon is required")
    private RoomIcon icon;

    @Min(value = 2, message = "A room must have at least 2 players")
    @Max(value = 60, message = "A room can have at most 60 players")
    private int numberOfPlayers;

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

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public RoomIcon getIcon() {
        return icon;
    }

    public void setIcon(RoomIcon icon) {
        this.icon = icon;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}
