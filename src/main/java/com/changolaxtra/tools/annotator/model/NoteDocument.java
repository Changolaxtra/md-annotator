package com.changolaxtra.tools.annotator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "authors")
public class NoteDocument {

  @Id
  private final UUID id;
  @Indexed(unique = true)
  private final String date;
  @Field
  private final String content;
  @Field
  private List<String> tags;

  public NoteDocument(final UUID id, final String date, final String content, final List<String> tags) {
    this.id = id;
    this.date = date;
    this.content = content;
    this.tags = tags;
  }

  public UUID getId() {
    return id;
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
