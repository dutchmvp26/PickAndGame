package org.example.pngbackend.service;

import org.example.pngbackend.entity.Room;
import org.example.pngbackend.repository.ParticipantRepository;
import org.example.pngbackend.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
        // Arrange: prepare an invalid number of players and the expected message.
        int invalidNumberOfPlayers = 1;
        String expectedMessage = "A room must have at least 2 players";

        // Act: call createRoom with the invalid value.
        // assertThrows also verifies that the expected exception type is thrown.
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roomService.createRoom(invalidNumberOfPlayers)
        );

        // Assert: verify the exception message and confirm that validation
        // happened before either repository was accessed.
        assertEquals(expectedMessage, exception.getMessage());
        verifyNoInteractions(roomRepository, participantRepository);
    }