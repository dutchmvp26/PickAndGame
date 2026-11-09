package org.example.pngbackend.repositoryimplementations;

import org.example.pngbackend.domain.RoomDomain;
import org.example.pngbackend.mapper.RoomMapper;
import org.example.pngbackend.repository.JpaRoomRepository;
import org.example.pngbackend.repository.RoomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

    private final JpaRoomRepository jpa;
    private final RoomMapper mapper;

    public RoomRepositoryImpl(JpaRoomRepository jpa, RoomMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public RoomDomain save(RoomDomain room) {
        return mapper.toDomain(jpa.save(mapper.toEntity(room)));
    }

    @Override
    public Optional<RoomDomain> findByRoomCode(String roomCode) {
        return jpa.findByRoomCode(roomCode).map(mapper::toDomain);
    }

    @Override
    public boolean existsByRoomCode(String roomCode) {
        return jpa.existsByRoomCode(roomCode);
    }

    @Override
    public List<RoomDomain> getAllRooms() {
        return jpa.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<RoomDomain> findByRoomCodeForUpdate(String roomCode) {
        return jpa.findByRoomCodeForUpdate(roomCode)
                .map(mapper::toDomain);
    }
}