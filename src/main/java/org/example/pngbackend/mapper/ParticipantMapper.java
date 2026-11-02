package org.example.pngbackend.mapper;

import org.example.pngbackend.domain.ParticipantDomain;
import org.example.pngbackend.entity.Participant;
import org.example.pngbackend.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class ParticipantMapper {

    public Participant toEntity(ParticipantDomain domain, Room room) {
        Participant entity = new Participant();

        entity.setName(domain.getName());
        entity.setRoom(room);

        return entity;
    }

    public ParticipantDomain toDomain(Participant entity) {
        return new ParticipantDomain(
                entity.getId(),
                entity.getName(),
                entity.getRoom().getId()
        );
    }
}