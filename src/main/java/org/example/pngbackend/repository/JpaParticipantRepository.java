package org.example.pngbackend.repository;

import org.example.pngbackend.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findByRoom_Id(Long roomId);

    long countByRoom_Id(Long roomId);
}
