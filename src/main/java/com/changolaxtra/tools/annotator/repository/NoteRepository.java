package com.changolaxtra.tools.annotator.repository;

import com.changolaxtra.tools.annotator.model.NoteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends MongoRepository<NoteDocument, UUID> {

  Optional<NoteDocument> findByDate(String date);

}
