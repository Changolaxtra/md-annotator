package com.changolaxtra.tools.annotator.view;

import com.changolaxtra.tools.annotator.dto.NoteDto;
import com.changolaxtra.tools.annotator.service.NoteService;
import com.changolaxtra.tools.annotator.service.SystemUserService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route("")
@RouteAlias("home")
public class HomeView extends VerticalLayout {

  private final NoteService noteService;
  private final SystemUserService userService;

  private String currentContent = StringUtils.EMPTY;
  private LocalDate selectedDate;
  private List<String> currentTags = new ArrayList<>();
  private String username;

  public HomeView(final NoteService noteService, SystemUserService userService) {
    this.noteService = noteService;
    this.userService = userService;
    initNoteAndTags();
    buildUi();
  }

  private void initNoteAndTags() {
    selectedDate = LocalDate.now();
    Optional.ofNullable(getNote(selectedDate))
        .ifPresent(note -> {
          currentContent = note.getContent();
          currentTags = note.getTags();
        });
  }

  private void buildUi() {
    username = userService.getUserName();
    getElement().setAttribute("theme", Lumo.DARK);
    setAlignItems(Alignment.CENTER);
    setWidthFull();
    add("Hey " + username + "! here are your daily notes");

    final Markdown markdown = getMarkdown();
    final TextArea textArea = getEditor(markdown);

    addDatePicker(textArea);
    addEditorLayout(textArea, markdown);
    addSaveButton(textArea);
  }

  private Markdown getMarkdown() {
    final Markdown markdown = new Markdown();
    markdown.setMinWidth(100, Unit.PERCENTAGE);
    markdown.setMinHeight(600, Unit.PIXELS);
    return markdown;
  }

  private TextArea getEditor(final Markdown markdown) {
    final TextArea textArea = new TextArea();
    textArea.setValue(currentContent);
    textArea.setMinWidth(100, Unit.PERCENTAGE);
    textArea.setMinHeight( 600, Unit.PIXELS);

    textArea.addValueChangeListener(event -> {
      currentContent = event.getValue();
      markdown.setContent(currentContent);
    });

    return textArea;
  }

  private void addDatePicker(final TextArea textArea) {
    final DatePicker datePicker = new DatePicker("Select the Note Date");
    final LocalDate initialDate = LocalDate.now();
    datePicker.setMax(initialDate);
    datePicker.setInitialPosition(initialDate);
    datePicker.setValue(initialDate);

    datePicker.addValueChangeListener(event -> {
      selectedDate = event.getValue();
      Optional.ofNullable(getNote(selectedDate))
          .ifPresent(noteDto -> {
            textArea.setValue(noteDto.getContent());
            currentTags = noteDto.getTags();
          });
    });

    add(datePicker);
  }

  private void addEditorLayout(final TextArea textArea, final Markdown markdown) {
    final VerticalLayout editorLayout = getVerticalLayout();
    editorLayout.add("Editor");
    editorLayout.add(textArea);

    final VerticalLayout previewLayout = getVerticalLayout();
    previewLayout.add("Preview");
    previewLayout.add(markdown);

    final HorizontalLayout horizontalLayout = getHorizontalLayout();
    horizontalLayout.add(editorLayout);
    horizontalLayout.add(previewLayout);
    add(horizontalLayout);
  }

  private VerticalLayout getVerticalLayout() {
    final VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setWidthFull();
    verticalLayout.setAlignItems(Alignment.CENTER);
    verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    verticalLayout.setPadding(true);

    return verticalLayout;
  }

  private HorizontalLayout getHorizontalLayout() {
    final HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.setWidthFull();
    horizontalLayout.setAlignItems(Alignment.CENTER);
    horizontalLayout.setPadding(true);
    horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

    return horizontalLayout;
  }

  private void addSaveButton(final TextArea textArea) {
    final Button button = new Button("Save");
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    final ProgressBar progressBar = new ProgressBar();
    progressBar.setMaxWidth(80, Unit.PERCENTAGE);

    button.addClickListener(event -> {
      progressBar.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
      progressBar.setValue(0.1);
      final String selectedStringDate = getSelectedStringDate(selectedDate);
      progressBar.setValue(0.5);
      noteService.update(selectedStringDate, new NoteDto(selectedStringDate, currentContent, currentTags));
      progressBar.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
      progressBar.setValue(1);
    });

    final HorizontalLayout buttonHorizontalLayout = getHorizontalLayout();
    buttonHorizontalLayout.add(button);

    add(progressBar);
    add(buttonHorizontalLayout);
  }

  private NoteDto getNote(final LocalDate selectedDate) {
    return Optional.ofNullable(noteService)
        .map(service -> service.getByDate(getSelectedStringDate(selectedDate)))
        .orElse(NoteDto.getDefault());
  }

  private String getSelectedStringDate(final LocalDate selectedDate) {
    return Optional.ofNullable(selectedDate)
        .map(NoteDto.FORMATTER::format)
        .orElse(NoteDto.FORMATTER.format(LocalDate.now()));
  }

}
