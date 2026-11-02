package org.example.pngbackend.service;

import org.example.pngbackend.repository.JpaParticipantRepository;
import org.example.pngbackend.repository.JpaRoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class RoomServiceTests {

    @Mock
    private JpaRoomRepository roomRepository;

    @Mock
    private JpaParticipantRepository participantRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void createRoom_shouldRejectFewerThanTwoPlayers() {
        // Arrange
        int invalidNumberOfPlayers = 1;
        String expectedMessage =
                "400 BAD_REQUEST \"A room must have at least 2 players.\"";

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roomService.createRoom(invalidNumberOfPlayers)
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(
                "A room must have at least 2 players.",
                exception.getReason()
        );

        verifyNoInteractions(roomRepository, participantRepository);
    }
}