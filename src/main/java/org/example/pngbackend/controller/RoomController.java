package org.example.pngbackend.controller;

import org.example.pngbackend.dto.CreateRoomRequest;
import org.example.pngbackend.dto.JoinRoomRequest;
import org.example.pngbackend.entity.Participant;
import org.example.pngbackend.entity.Room;
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
    public ResponseEntity<Room> createRoom(
            @Valid @RequestBody CreateRoomRequest request) {

        Room room = roomService.createRoom(
                request.getNumberOfPlayers()
        );

        return ResponseEntity.ok(room);
    }

    @GetMapping("/{roomCode}")
    public ResponseEntity<Room> getRoom(
            @PathVariable String roomCode) {

        return ResponseEntity.ok(
                roomService.getRoom(roomCode)
        );
    }

    @PostMapping("/join")
    public ResponseEntity<Participant> joinRoom(
            @Valid @RequestBody JoinRoomRequest request) {

        return ResponseEntity.ok(
                roomService.joinRoom(
                        request.getRoomCode(),
                        request.getName()
                )
        );
    }
}