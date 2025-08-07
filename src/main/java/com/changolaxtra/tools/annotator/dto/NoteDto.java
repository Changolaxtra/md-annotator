package com.changolaxtra.tools.annotator.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NoteDto {

  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

  private final String date;
  private final String content;
  private List<String> tags;

  public static NoteDto getDefault(){
    return new NoteDto(FORMATTER.format(LocalDate.now()), "", new ArrayList<>());
  }

  public NoteDto(final String date, final String content, final List<String> tags) {
    this.date = date;
    this.content = content;
    this.tags = tags;
  }

  public String getDate() {
    return date;
  }

  public String getContent() {
    return content;
  }

  public List<String> getTags() {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }

}
