package org.example.pngbackend.repository;

import org.example.pngbackend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
// NOT IN USE RIGHT NOW :D
//    boolean existsByParticipantIdAndGameIdAndRoomId(
//            Long participantId,
//            Long gameId,
//            Long roomId
//    );
//
//    List<Vote> findByRoomId(Long roomId);
//
//    long countByRoomIdAndGameId(
//            Long roomId,
//            Long gameId
//    );
}