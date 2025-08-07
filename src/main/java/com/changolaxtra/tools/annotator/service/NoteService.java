package com.changolaxtra.tools.annotator.service;

import com.changolaxtra.tools.annotator.dto.NoteDto;
import com.changolaxtra.tools.annotator.model.NoteDocument;
import com.changolaxtra.tools.annotator.repository.NoteRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;


@Service
public class NoteService {

  private static final Logger logger = LoggerFactory.getLogger(NoteService.class);

  private final NoteRepository noteRepository;

  public NoteService(final NoteRepository noteRepository) {
    this.noteRepository = noteRepository;
  }

  @Nullable
  public UUID save(@NotNull final NoteDto noteDto) {
    logger.info("Saving NoteDto :{}", noteDto.getDate());
    final NoteDocument document = new NoteDocument(UUID.randomUUID(),
        noteDto.getDate(),
        noteDto.getContent(),
        noteDto.getTags());

    final NoteDocument saved = noteRepository.save(document);

    return Optional.of(saved)
        .map(NoteDocument::getId)
        .orElse(null);
  }

  @Nullable
  public NoteDto getByDate(@NotNull final String date) {
    logger.info("Getting NoteDto :{}", date);
    return noteRepository.findByDate(date)
        .map(this::mapToDto)
        .orElse(null);
  }

  public UUID saveOrUpdate(@NotNull final String date, @NotNull final NoteDto noteDto) {
    if (getByDate(date) == null) {
      return save(noteDto);
    } else {
      logger.info("Updating NoteDto :{}", date);
      return noteRepository.findByDate(date)
          .map(oldNote -> updateDocument(oldNote, noteDto))
          .map(noteRepository::save)
          .map(NoteDocument::getId)
          .orElse(null);
    }
  }

  private NoteDocument updateDocument(final NoteDocument oldNote, final NoteDto noteDto) {
    return new NoteDocument(oldNote.getId(), noteDto.getDate(), noteDto.getContent(), noteDto.getTags());
  }

  private NoteDto mapToDto(final NoteDocument document) {
    return new NoteDto(document.getDate(), document.getContent(), document.getTags());
  }

}
