package org.example.pngbackend.repository;

import org.example.pngbackend.domain.ParticipantDomain;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {

    ParticipantDomain save(ParticipantDomain participant);

    Optional<ParticipantDomain> findById(Long id);

    List<ParticipantDomain> findByRoomId(Long roomId);

    long countByRoomId(Long roomId);
}
