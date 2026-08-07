package com.devprep.repository;

import com.devprep.entity.Notes;
import com.devprep.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotesRepository extends JpaRepository<Notes, Long> {

    List<Notes> findByTopic(Topic topic);

    Optional<Notes> findByTopicAndNotesId(Topic topic, Long id);
}
