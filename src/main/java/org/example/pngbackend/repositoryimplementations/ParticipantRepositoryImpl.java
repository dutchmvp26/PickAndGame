package org.example.pngbackend.repositoryimplementations;

import org.example.pngbackend.domain.ParticipantDomain;
import org.example.pngbackend.entity.Participant;
import org.example.pngbackend.entity.Room;
import org.example.pngbackend.mapper.ParticipantMapper;
import org.example.pngbackend.repository.ParticipantRepository;
import org.example.pngbackend.repository.JpaParticipantRepository;
import org.example.pngbackend.repository.JpaRoomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParticipantRepositoryImpl implements ParticipantRepository {

    private final JpaParticipantRepository jpa;
    private final JpaRoomRepository jpaRooms;
    private final ParticipantMapper mapper;

    public ParticipantRepositoryImpl(
            JpaParticipantRepository jpa,
            JpaRoomRepository jpaRooms,
            ParticipantMapper mapper
    ) {
        this.jpa = jpa;
        this.jpaRooms = jpaRooms;
        this.mapper = mapper;
    }

    @Override
    public ParticipantDomain save(ParticipantDomain participant) {

        Room room = jpaRooms.getReferenceById(participant.getRoomId());

        Participant saved = jpa.save(mapper.toEntity(participant, room));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ParticipantDomain> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<ParticipantDomain> findByRoomId(Long roomId) {
        return jpa.findByRoom_Id(roomId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public long countByRoomId(Long roomId) {
        return jpa.countByRoom_Id(roomId);
    }
}
