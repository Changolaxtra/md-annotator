package com.changolaxtra.tools.annotator.controller;

import com.changolaxtra.tools.annotator.dto.NoteDto;
import com.changolaxtra.tools.annotator.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class NoteController {

  private final NoteService noteService;

  public NoteController(final NoteService noteService) {
    this.noteService = noteService;
  }

  @PostMapping("/note")
  public ResponseEntity<String> create(@RequestBody final NoteDto request) {
    return Optional.ofNullable(request)
        .map(noteService::save)
        .map(uuid -> ResponseEntity.ok(uuid.toString()))
        .orElse(ResponseEntity.badRequest().body("Invalid Request"));
  }

  @GetMapping("/note/{date}")
  public ResponseEntity<NoteDto> get(@PathVariable String date) {
    return Optional.ofNullable(date)
        .map(noteService::getByDate)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/note/{date}")
  public ResponseEntity<String> update(@PathVariable String date, @RequestBody final NoteDto request) {
    return Optional.ofNullable(request)
        .map(dto -> noteService.saveOrUpdate(date, dto))
        .map(uuid -> ResponseEntity.ok(uuid.toString()))
        .orElse(ResponseEntity.badRequest().body("Invalid Request"));
  }

}
