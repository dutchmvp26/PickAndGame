package org.example.pngbackend.repository;

import org.example.pngbackend.domain.RoomDomain;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    RoomDomain save(RoomDomain room);
    Optional<RoomDomain> findByRoomCode(String roomCode);
    boolean existsByRoomCode(String roomCode);
    Optional<RoomDomain> findByRoomCodeForUpdate(String roomCode);
    List<RoomDomain> getAllRooms();
}