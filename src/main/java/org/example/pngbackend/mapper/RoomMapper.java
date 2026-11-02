    package org.example.pngbackend.mapper;

    import org.example.pngbackend.domain.RoomDomain;
    import org.example.pngbackend.domain.RoomStatus;
    import org.example.pngbackend.entity.Room;
    import org.example.pngbackend.entity.RoomStatusEntity;
    import org.springframework.stereotype.Component;

    @Component
    public class RoomMapper {

        public Room toEntity(RoomDomain domain) {
            Room entity = new Room();

            entity.setRoomCode(domain.getRoomCode());
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
                    entity.getNumberOfPlayers(),
                    RoomStatus.valueOf(entity.getStatus().name()),
                    entity.getCreatedAt()
            );
        }
    }