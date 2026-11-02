package org.example.pngbackend.controller;

import org.example.pngbackend.domain.ParticipantDomain;
import org.example.pngbackend.domain.RoomDomain;
import org.example.pngbackend.dto.CreateRoomRequest;
import org.example.pngbackend.dto.JoinRoomRequest;
import org.example.pngbackend.service.RoomService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomDomain> createRoom(
            @Valid @RequestBody CreateRoomRequest request
    ) {
        RoomDomain room = roomService.createRoom(
                request.getName(),
                request.getDescription(),
                request.getActivityType(),
                request.getIcon(),
                request.getNumberOfPlayers()
        );

        return ResponseEntity.ok(room);
    }

    @GetMapping("/{roomCode}")
    public ResponseEntity<RoomDomain> getRoom(
            @PathVariable String roomCode
    ) {
        RoomDomain room = roomService.getRoom(roomCode);

        return ResponseEntity.ok(room);
    }

    @PostMapping("/join")
    public ResponseEntity<ParticipantDomain> joinRoom(
            @Valid @RequestBody JoinRoomRequest request
    ) {
        ParticipantDomain participant = roomService.joinRoom(
                request.getRoomCode(),
                request.getName()
        );

        return ResponseEntity.ok(participant);
    }
}