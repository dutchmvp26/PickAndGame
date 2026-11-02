package org.example.pngbackend.service;

import org.example.pngbackend.domain.ActivityType;
import org.example.pngbackend.domain.RoomDomain;
import org.example.pngbackend.domain.RoomIcon;
import org.example.pngbackend.domain.RoomStatus;
import org.example.pngbackend.repository.ParticipantRepository;
import org.example.pngbackend.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTests {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void createRoom_shouldRejectFewerThanTwoPlayers() {
        // Arrange
        int invalidNumberOfPlayers = 1;

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roomService.createRoom(
                        "Game night",
                        "",
                        ActivityType.VIDEO_GAMES,
                        RoomIcon.CONTROLLER,
                        invalidNumberOfPlayers
                )
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(
                "A room must have at least 2 players.",
                exception.getReason()
        );

        verifyNoInteractions(roomRepository, participantRepository);
    }

    @Test
    void createRoom_shouldSaveAllRoomDetails() {
        // Arrange
        when(roomRepository.existsByRoomCode(any())).thenReturn(false);
        when(roomRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RoomDomain room = roomService.createRoom(
                "Friday game night",
                "FIFA and Mario Kart",
                ActivityType.VIDEO_GAMES,
                RoomIcon.CONTROLLER,
                4
        );

        // Assert
        assertEquals("Friday game night", room.getName());
        assertEquals("FIFA and Mario Kart", room.getDescription());
        assertEquals(ActivityType.VIDEO_GAMES, room.getActivityType());
        assertEquals(RoomIcon.CONTROLLER, room.getIcon());
        assertEquals(4, room.getNumberOfPlayers());
        assertEquals(RoomStatus.WAITING, room.getStatus());
        assertEquals(6, room.getRoomCode().length());
    }
}
