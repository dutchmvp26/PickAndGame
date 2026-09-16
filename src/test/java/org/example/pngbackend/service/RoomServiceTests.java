package org.example.pngbackend.service;

import org.example.pngbackend.repository.ParticipantRepository;
import org.example.pngbackend.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.example.pngbackend.entity.Room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTests {
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private RoomService roomService;


    @Test
    void createRoom_shouldRejectFewerThanTwoPlayers() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roomService.createRoom(1)
        );

        assertEquals(
                "A room must have at least 2 players",
                exception.getMessage()
        );

        verifyNoInteractions(roomRepository, participantRepository);
    }

    @Test
    void createRoom_shouldCreateRoom() {
        when(roomRepository.existsByRoomCode(anyString()))
                .thenReturn(false);

        when(roomRepository.save(any(Room.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Room result = roomService.createRoom(4);

        assertEquals(4, result.getNumberOfPlayers());
        assertTrue(result.getRoomCode().matches("[A-Z0-9]{6}"));

        verify(roomRepository).existsByRoomCode(result.getRoomCode());
        verify(roomRepository).save(result);
    }

    @Test
    void getRoom_shouldReturnExistingRoom() {
        Room room = mock(Room.class);

        when(roomRepository.findByRoomCode("ABC123"))
                .thenReturn(Optional.of(room));

        Room result = roomService.getRoom("ABC123");

        assertSame(room, result);
        verify(roomRepository).findByRoomCode("ABC123");
    }
}


