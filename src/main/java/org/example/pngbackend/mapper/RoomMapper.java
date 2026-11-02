package org.example.pngbackend.mapper;

import org.example.pngbackend.domain.ActivityType;
import org.example.pngbackend.domain.RoomDomain;
import org.example.pngbackend.domain.RoomIcon;
import org.example.pngbackend.domain.RoomStatus;
import org.example.pngbackend.entity.ActivityTypeEntity;
import org.example.pngbackend.entity.Room;
import org.example.pngbackend.entity.RoomIconEntity;
import org.example.pngbackend.entity.RoomStatusEntity;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public Room toEntity(RoomDomain domain) {
        Room entity = new Room();

        entity.setRoomCode(domain.getRoomCode());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setActivityType(
                ActivityTypeEntity.valueOf(domain.getActivityType().name())
        );
        entity.setIcon(
                RoomIconEntity.valueOf(domain.getIcon().name())
        );
        entity.setNumberOfPlayers(domain.getNumberOfPlayers());
        entity.setStatus(
                RoomStatusEntity.valueOf(domain.getStatus().name())
        );
        entity.setCreatedAt(domain.getCreatedAt());

        return entity;
    }

    public RoomDomain toDomain(Room entity) {
        return new RoomDomain(
                entity.getId(),
                entity.getRoomCode(),
                entity.getName(),
                entity.getDescription(),
                ActivityType.valueOf(entity.getActivityType().name()),
                RoomIcon.valueOf(entity.getIcon().name()),
                entity.getNumberOfPlayers(),
                RoomStatus.valueOf(entity.getStatus().name()),
                entity.getCreatedAt()
        );
    }
}
