package org.example.pngbackend.service;

import org.example.pngbackend.domain.ActivityType;
import org.example.pngbackend.domain.ParticipantDomain;
import org.example.pngbackend.domain.RoomDomain;
import org.example.pngbackend.domain.RoomIcon;
import org.example.pngbackend.domain.RoomStatus;
import org.example.pngbackend.repository.ParticipantRepository;
import org.example.pngbackend.repository.RoomRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.List;

@Service
public class RoomService {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;

    private final SecureRandom random = new SecureRandom();

    public RoomService(
            RoomRepository roomRepository,
            ParticipantRepository participantRepository
    ) {
        this.roomRepository = roomRepository;
        this.participantRepository = participantRepository;
    }

    public RoomDomain createRoom(
            String name,
            String description,
            ActivityType activityType,
            RoomIcon icon,
            int numberOfPlayers
    ) {
        if (numberOfPlayers < 2) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A room must have at least 2 players."
            );
        }

        String roomCode = generateUniqueRoomCode();

        RoomDomain room = RoomDomain.create(
                roomCode,
                name,
                description,
                activityType,
                icon,
                numberOfPlayers
        );

        return roomRepository.save(room);
    }

    public RoomDomain getRoom(String roomCode) {
        return roomRepository.findByRoomCode(normalize(roomCode))
                .orElseThrow(this::roomNotFound);
    }

    public List<RoomDomain> getAllRooms() {
        return roomRepository.getAllRooms();
    }

    @Transactional
    public ParticipantDomain joinRoom(String roomCode, String name) {
        RoomDomain room = roomRepository.findByRoomCodeForUpdate(normalize(roomCode))
                .orElseThrow(this::roomNotFound);

        if (room.getStatus() != RoomStatus.WAITING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Room is no longer accepting players."
            );
        }

        long participantCount = participantRepository.countByRoomId(room.getId());

        if (room.isFull((int) participantCount)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Room is full."
            );
        }

        ParticipantDomain participant = ParticipantDomain.join(name, room.getId());

        return participantRepository.save(participant);
    }

    private ResponseStatusException roomNotFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Room(s) not found.");
    }

    private String normalize(String roomCode) {
        return roomCode == null ? null : roomCode.trim().toUpperCase();
    }

    private String generateUniqueRoomCode() {
        String code;

        do {
            code = generateRoomCode();
        } while (roomRepository.existsByRoomCode(code));

        return code;
    }

    private String generateRoomCode() {
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }
}
