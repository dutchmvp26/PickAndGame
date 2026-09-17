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

    @Test
    void createRoom_shouldCreateRoom() {
        // Arrange: prepare the requested number of players.
        int numberOfPlayers = 4;

        // Simulate that the generated room code does not already exist.
        when(roomRepository.existsByRoomCode(anyString()))
                .thenReturn(false);

        // Simulate saving the room by returning the same Room object.
        when(roomRepository.save(any(Room.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act: create the room.
        Room result = roomService.createRoom(numberOfPlayers);

        // Assert: verify the returned room contains the correct values.
        assertEquals(numberOfPlayers, result.getNumberOfPlayers());
        assertTrue(
                result.getRoomCode().matches("[A-Z0-9]{6}"),
                "Room code should contain six uppercase letters or digits"
        );

        // Assert: verify the expected repository operations occurred.
        verify(roomRepository).existsByRoomCode(result.getRoomCode());
        verify(roomRepository).save(result);
    }

    @Test
    void getRoom_shouldReturnExistingRoom() {
        // Arrange: create a mock room and configure the repository to return it.
        String roomCode = "ABC123";
        Room existingRoom = mock(Room.class);

        when(roomRepository.findByRoomCode(roomCode))
                .thenReturn(Optional.of(existingRoom));

        // Act: retrieve the room using its code.
        Room result = roomService.getRoom(roomCode);

        // Assert: verify that the service returns the exact room supplied
        // by the repository and that the repository was queried correctly.
        assertSame(existingRoom, result);
        verify(roomRepository).findByRoomCode(roomCode);
    }
}