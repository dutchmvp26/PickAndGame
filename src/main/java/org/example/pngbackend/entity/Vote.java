package org.example.pngbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "votes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_participant_game_room",
                        columnNames = {
                                "participant_id",
                                "game_id",
                                "room_id"
                        }
                )
        }
)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    protected Vote() {
    }

    public Vote(
            Participant participant,
            Game game,
            Room room
    ) {
        this.participant = participant;
        this.game = game;
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public Participant getParticipant() {
        return participant;
    }

    public Game getGame() {
        return game;
    }

    public Room getRoom() {
        return room;
    }
}