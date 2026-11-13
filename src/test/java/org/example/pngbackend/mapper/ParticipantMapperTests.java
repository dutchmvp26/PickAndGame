package org.example.pngbackend.mapper;

import org.example.pngbackend.domain.ParticipantDomain;
import org.example.pngbackend.entity.Participant;
import org.example.pngbackend.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ParticipantMapperTests {

    private final ParticipantMapper mapper = new ParticipantMapper();

    @Test
    void toEntity_shouldCopyNameAndAttachRoom() {
        Room room = new Room();
        ParticipantDomain domain = new ParticipantDomain(null, "Anna", 1L);

        Participant entity = mapper.toEntity(domain, room);

        assertNull(entity.getId());
        assertEquals("Anna", entity.getName());
        assertSame(room, entity.getRoom());
    }

    @Test
    void toDomain_shouldCopyIdNameAndRoomId() {
        Room room = new Room();
        ReflectionTestUtils.setField(room, "id", 1L);

        Participant entity = new Participant();
        ReflectionTestUtils.setField(entity, "id", 10L);
        entity.setName("Anna");
        entity.setRoom(room);

        ParticipantDomain domain = mapper.toDomain(entity);

        assertEquals(10L, domain.getId());
        assertEquals("Anna", domain.getName());
        assertEquals(1L, domain.getRoomId());
    }
}
