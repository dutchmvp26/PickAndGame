package org.example.pngbackend.mapper;

import org.example.pngbackend.domain.ActivityType;
import org.example.pngbackend.domain.RoomDomain;
import org.example.pngbackend.domain.RoomIcon;
import org.example.pngbackend.domain.RoomStatus;
import org.example.pngbackend.entity.ActivityTypeEntity;
import org.example.pngbackend.entity.Room;
import org.example.pngbackend.entity.RoomIconEntity;
import org.example.pngbackend.entity.RoomStatusEntity;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RoomMapperTests {

    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 11, 12, 18, 0);

    private final RoomMapper mapper = new RoomMapper();

    @Test
    void toEntity_shouldCopyAllFields() {
        RoomDomain domain = new RoomDomain(
                1L, "ABC123", "Game night", "Bring snacks",
                ActivityType.SPORTS, RoomIcon.BALL, 6, RoomStatus.VOTING, CREATED_AT
        );

        Room entity = mapper.toEntity(domain);

        assertNull(entity.getId()); // the database assigns the id
        assertEquals("ABC123", entity.getRoomCode());
        assertEquals("Game night", entity.getName());
        assertEquals("Bring snacks", entity.getDescription());
        assertEquals(ActivityTypeEntity.SPORTS, entity.getActivityType());
        assertEquals(RoomIconEntity.BALL, entity.getIcon());
        assertEquals(6, entity.getNumberOfPlayers());
        assertEquals(RoomStatusEntity.VOTING, entity.getStatus());
        assertEquals(CREATED_AT, entity.getCreatedAt());
    }

    @Test
    void toDomain_shouldCopyAllFields() {
        Room entity = new Room();
        ReflectionTestUtils.setField(entity, "id", 5L);
        entity.setRoomCode("XYZ789");
        entity.setName("Board night");
        entity.setDescription("Catan");
        entity.setActivityType(ActivityTypeEntity.BOARD_GAMES);
        entity.setIcon(RoomIconEntity.DICE);
        entity.setNumberOfPlayers(4);
        entity.setStatus(RoomStatusEntity.FINISHED);
        entity.setCreatedAt(CREATED_AT);

        RoomDomain domain = mapper.toDomain(entity);

        assertEquals(5L, domain.getId());
        assertEquals("XYZ789", domain.getRoomCode());
        assertEquals("Board night", domain.getName());
        assertEquals("Catan", domain.getDescription());
        assertEquals(ActivityType.BOARD_GAMES, domain.getActivityType());
        assertEquals(RoomIcon.DICE, domain.getIcon());
        assertEquals(4, domain.getNumberOfPlayers());
        assertEquals(RoomStatus.FINISHED, domain.getStatus());
        assertEquals(CREATED_AT, domain.getCreatedAt());
    }

    @Test
    void roundTrip_shouldKeepValues() {
        RoomDomain original = RoomDomain.create(
                "ABC123", "Game night", "Bring snacks",
                ActivityType.OTHER, RoomIcon.ROCKET, 3
        );

        RoomDomain result = mapper.toDomain(mapper.toEntity(original));

        assertEquals(original.getRoomCode(), result.getRoomCode());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getActivityType(), result.getActivityType());
        assertEquals(original.getIcon(), result.getIcon());
        assertEquals(original.getStatus(), result.getStatus());
    }
}
