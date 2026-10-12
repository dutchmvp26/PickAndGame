package org.example.pngbackend.service;

import org.example.pngbackend.entity.Participant;
import org.example.pngbackend.entity.Room;
import org.example.pngbackend.repository.ParticipantRepository;
import org.example.pngbackend.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    public Room createRoom(int numberOfPlayers) {

        if (numberOfPlayers < 2) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A room must have at least 2 players."
            );
        }

        String roomCode = generateUniqueRoomCode();

        Room room = new Room(
                roomCode,
                numberOfPlayers
        );

        return roomRepository.save(room);
    }

    public Room getRoom(String roomCode) {

        return roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Room not found."
                        )
                );
    }

    public Participant joinRoom(
            String roomCode,
            String name
    ) {

        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Room not found."
                        )
                );

        List<Participant> participants =
                participantRepository.findByRoomId(room.getId());

        if (participants.size() >= room.getNumberOfPlayers()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Room is full."
            );
        }

        Participant participant =
                new Participant(name, room);

        return participantRepository.save(participant);
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

            int index =
                    random.nextInt(CHARACTERS.length());

            code.append(
                    CHARACTERS.charAt(index)
            );
        }

        return code.toString();
    }
}