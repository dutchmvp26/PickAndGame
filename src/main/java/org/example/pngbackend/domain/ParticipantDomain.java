package org.example.pngbackend.domain;

public class  ParticipantDomain {

    private final Long id;
    private final String name;
    private final Long roomId;

    public ParticipantDomain(
            Long id,
            String name,
            Long roomId
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Name is required."
            );
        }

        this.id = id;
        this.name = name;
        this.roomId = roomId;
    }

    public static ParticipantDomain join(
            String name,
            Long roomId
    ) {
        if (roomId == null) {
            throw new IllegalArgumentException(
                    "A participant must belong to a room."
            );
        }

        return new ParticipantDomain(null, name, roomId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getRoomId() {
        return roomId;
    }
}