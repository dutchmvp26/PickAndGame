package org.example.pngbackend.service;

import org.example.pngbackend.domain.ActivityType;
import org.example.pngbackend.domain.ParticipantDomain;
import org.example.pngbackend.domain.RoomDomain;
import org.example.pngbackend.domain.RoomIcon;
import org.example.pngbackend.domain.RoomStatus;
import org.example.pngbackend.repository.ParticipantRepository;
import org.example.pngbackend.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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

    // ---------- helper ----------

    private RoomDomain buildRoom(RoomStatus status, int numberOfPlayers) {
        return new RoomDomain(
                1L,
                "ABC123",
                "Game night",
                "Bring snacks",
                ActivityType.VIDEO_GAMES,
                RoomIcon.CONTROLLER,
                numberOfPlayers,
                status,
                LocalDateTime.now()
        );
    }

    // ---------- createRoom ----------

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

    @Test
    void createRoom_shouldOnlyUseAllowedCharactersInRoomCode() {
        // Arrange
        when(roomRepository.existsByRoomCode(any())).thenReturn(false);
        when(roomRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RoomDomain room = roomService.createRoom(
                "Game night",
                "",
                ActivityType.SPORTS,
                RoomIcon.BALL,
                2
        );

        // Assert
        assertTrue(room.getRoomCode().matches("[A-Z0-9]{6}"));
    }

    @Test
    void createRoom_shouldGenerateNewCodeWhenCodeAlreadyExists() {
        // Arrange: first generated code is taken, second one is free
        when(roomRepository.existsByRoomCode(any())).thenReturn(true, false);
        when(roomRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RoomDomain room = roomService.createRoom(
                "Game night",
                "",
                ActivityType.BOARD_GAMES,
                RoomIcon.DICE,
                3
        );

        // Assert
        verify(roomRepository, times(2)).existsByRoomCode(any());
        assertEquals(6, room.getRoomCode().length());
    }

    // ---------- deleteRoom ----------

    @Test
    void deleteRoom_shouldDeleteParticipantsBeforeRoom() {
        // Arrange
        RoomDomain room = buildRoom(RoomStatus.WAITING, 4);
        when(roomRepository.findByRoomCode("ABC123")).thenReturn(Optional.of(room));

        // Act
        roomService.deleteRoom("ABC123");

        // Assert: participants must go first, otherwise the room can't be deleted
        InOrder order = inOrder(participantRepository, roomRepository);
        order.verify(participantRepository).deleteByRoomId(1L);
        order.verify(roomRepository).delete(1L);
    }

    @Test
    void deleteRoom_shouldNormalizeRoomCode() {
        // Arrange
        RoomDomain room = buildRoom(RoomStatus.WAITING, 4);
        when(roomRepository.findByRoomCode("ABC123")).thenReturn(Optional.of(room));

        // Act
        roomService.deleteRoom("  abc123  ");

        // Assert
        verify(roomRepository).findByRoomCode("ABC123");
        verify(roomRepository).delete(1L);
    }

    @Test
    void deleteRoom_shouldThrowNotFoundWhenRoomDoesNotExist() {
        // Arrange
        when(roomRepository.findByRoomCode("NOPE12")).thenReturn(Optional.empty());

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roomService.deleteRoom("NOPE12")
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Room(s) not found.", exception.getReason());
        verifyNoInteractions(participantRepository);
        verify(roomRepository, never()).delete(any());
    }

    // ---------- getRoom ----------

    @Test
    void getRoom_shouldReturnRoomWhenItExists() {
        // Arrange
        RoomDomain room = buildRoom(RoomStatus.WAITING, 4);
        when(roomRepository.findByRoomCode("ABC123")).thenReturn(Optional.of(room));

        // Act
        RoomDomain result = roomService.getRoom("ABC123");

        // Assert
        assertSame(room, result);
    }

    @Test
    void getRoom_shouldTrimAndUppercaseRoomCode() {
        // Arrange
        RoomDomain room = buildRoom(RoomStatus.WAITING, 4);
        when(roomRepository.findByRoomCode("ABC123")).thenReturn(Optional.of(room));

        // Act
        RoomDomain result = roomService.getRoom("  abc123 ");

        // Assert
        assertSame(room, result);
        verify(roomRepository).findByRoomCode("ABC123");
    }

    @Test
    void getRoom_shouldThrowNotFoundWhenRoomDoesNotExist() {
        // Arrange
        when(roomRepository.findByRoomCode("NOPE12")).thenReturn(Optional.empty());

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roomService.getRoom("NOPE12")
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Room(s) not found.", exception.getReason());
    }

    @Test
    void getRoom_shouldThrowNotFoundWhenRoomCodeIsNull() {
        // Arrange
        when(roomRepository.findByRoomCode(null)).thenReturn(Optional.empty());

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roomService.getRoom(null)
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    // ---------- getAllRooms ----------

    @Test
    void getAllRooms_shouldReturnAllRoomsFromRepository() {
        // Arrange
        List<RoomDomain> rooms = List.of(
                buildRoom(RoomStatus.WAITING, 4),
                buildRoom(RoomStatus.FINISHED, 6)
        );
        when(roomRepository.getAllRooms()).thenReturn(rooms);

        // Act
        List<RoomDomain> result = roomService.getAllRooms();

        // Assert
        assertEquals(2, result.size());
        assertSame(rooms, result);
    }

    @Test
    void getAllRooms_shouldReturnEmptyListWhenThereAreNoRooms() {
        // Arrange
        when(roomRepository.getAllRooms()).thenReturn(List.of());

        // Act
        List<RoomDomain> result = roomService.getAllRooms();

        // Assert
        assertTrue(result.isEmpty());
    }

    // ---------- joinRoom ----------

    @Test
    void joinRoom_shouldSaveParticipantWhenRoomHasSpace() {
        // Arrange
        RoomDomain room = buildRoom(RoomStatus.WAITING, 4);
        when(roomRepository.findByRoomCodeForUpdate("ABC123")).thenReturn(Optional.of(room));
        when(participantRepository.countByRoomId(1L)).thenReturn(2L);
        when(participantRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ParticipantDomain participant = roomService.joinRoom("ABC123", "Anna");

        // Assert
        assertEquals("Anna", participant.getName());
        assertEquals(1L, participant.getRoomId());
        verify(participantRepository).save(any());
    }

    @Test
    void joinRoom_shouldNormalizeRoomCode() {
        // Arrange
        RoomDomain room = buildRoom(RoomStatus.WAITING, 4);
        when(roomRepository.findByRoomCodeForUpdate("ABC123")).thenReturn(Optional.of(room));
        when(participantRepository.countByRoomId(1L)).thenReturn(0L);
        when(participantRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        roomService.joinRoom("  abc123 ", "Anna");

        // Assert
        verify(roomRepository).findByRoomCodeForUpdate("ABC123");
    }

    @Test
    void joinRoom_shouldAllowTheLastFreeSeat() {
        // Arrange: 3 of 4 seats taken
        RoomDomain room = buildRoom(RoomStatus.WAITING, 4);
        when(roomRepository.findByRoomCodeForUpdate("ABC123")).thenReturn(Optional.of(room));
        when(participantRepository.countByRoomId(1L)).thenReturn(3L);
        when(participantRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ParticipantDomain participant = roomService.joinRoom("ABC123", "Last Player");

        // Assert
        assertEquals("Last Player", participant.getName());
    }

    @Test
    void joinRoom_shouldThrowNotFoundWhenRoomDoesNotExist() {
        // Arrange
        when(roomRepository.findByRoomCodeForUpdate("NOPE12")).thenReturn(Optional.empty());

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roomService.joinRoom("NOPE12", "Anna")
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Room(s) not found.", exception.getReason());
        verifyNoInteractions(participantRepository);
    }

    @Test
    void joinRoom_shouldThrowConflictWhenRoomIsVoting() {
        // Arrange
        RoomDomain room = buildRoom(RoomStatus.VOTING, 4);
        when(roomRepository.findByRoomCodeForUpdate("ABC123")).thenReturn(Optional.of(room));

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roomService.joinRoom("ABC123", "Anna")
        );

        // Assert
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Room is no longer accepting players.", exception.getReason());
        verifyNoInteractions(participantRepository);
    }

    @Test
    void joinRoom_shouldThrowConflictWhenRoomIsFinished() {
        // Arrange
        RoomDomain room = buildRoom(RoomStatus.FINISHED, 4);
        when(roomRepository.findByRoomCodeForUpdate("ABC123")).thenReturn(Optional.of(room));

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roomService.joinRoom("ABC123", "Anna")
        );

        // Assert
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Room is no longer accepting players.", exception.getReason());
    }

    @Test
    void joinRoom_shouldThrowConflictWhenRoomIsFull() {
        // Arrange
        RoomDomain room = buildRoom(RoomStatus.WAITING, 4);
        when(roomRepository.findByRoomCodeForUpdate("ABC123")).thenReturn(Optional.of(room));
        when(participantRepository.countByRoomId(1L)).thenReturn(4L);

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roomService.joinRoom("ABC123", "Anna")
        );

        // Assert
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Room is full.", exception.getReason());
        verify(participantRepository, never()).save(any());
    }

    @Test
    void joinRoom_shouldRejectBlankName() {
        // Arrange
        RoomDomain room = buildRoom(RoomStatus.WAITING, 4);
        when(roomRepository.findByRoomCodeForUpdate("ABC123")).thenReturn(Optional.of(room));
        when(participantRepository.countByRoomId(1L)).thenReturn(0L);

        // Act + Assert: the domain rule "Name is required." kicks in
        assertThrows(
                IllegalArgumentException.class,
                () -> roomService.joinRoom("ABC123", "   ")
        );
        verify(participantRepository, never()).save(any());
    }
}
