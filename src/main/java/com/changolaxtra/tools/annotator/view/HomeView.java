package com.changolaxtra.tools.annotator.view;

import com.changolaxtra.tools.annotator.dto.NoteDto;
import com.changolaxtra.tools.annotator.service.NoteService;
import com.changolaxtra.tools.annotator.service.SystemUserService;
import com.changolaxtra.tools.annotator.view.factory.LayoutFactory;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
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
    getElement().setAttribute("theme", Lumo.DARK);
    setAlignItems(Alignment.CENTER);
    setWidthFull();

    final Markdown markdown = getMarkdown();
    final TextArea textArea = getEditor(markdown);

    addHeader(textArea);
    addEditorLayout(textArea, markdown);
  }

  private void addHeader(final TextArea textArea) {
    final String username = userService.getUserName();
    final HorizontalLayout headLayout = LayoutFactory.getHorizontalLayout();
    headLayout.setAlignItems(Alignment.CENTER);
    headLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    final Avatar avatar = new Avatar(username);
    avatar.setColorIndex(RandomUtils.secure().randomInt(1,7));

    headLayout.add(avatar);
    headLayout.add(new Html("<h1>Hey " + username + "! Here we have your daily notes</h1>"));
    headLayout.addToEnd(getDatePicker(textArea));
    add(headLayout);
  }

  private Markdown getMarkdown() {
    final Markdown markdown = new Markdown();
    markdown.setMinWidth(100, Unit.PERCENTAGE);
    markdown.setMinHeight(100, Unit.PERCENTAGE);
    markdown.setContent(currentContent);
    return markdown;
  }

  private TextArea getEditor(final Markdown markdown) {
    final TextArea textArea = new TextArea();
    textArea.setValue(currentContent);
    textArea.setMinWidth(100, Unit.PERCENTAGE);
    textArea.setMinHeight( 100, Unit.PERCENTAGE);

    textArea.addValueChangeListener(event -> {
      currentContent = event.getValue();
      markdown.setContent(currentContent);
    });

    return textArea;
  }

  private DatePicker getDatePicker(final TextArea textArea) {
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

    return datePicker;
  }

  private void addEditorLayout(final TextArea textArea, final Markdown markdown) {
    // Editor
    final MenuBar editorMenuBar = new MenuBar();
    editorMenuBar.addItem(new Html("<h3>Editor</h3>"));

    final VerticalLayout editorLayout = LayoutFactory.getVerticalLayout();
    editorLayout.add(editorMenuBar);
    editorLayout.add(textArea);

    final Button saveButton = getSaveButton();
    final Button refreshButton = getRefreshButton(textArea, markdown);

    // Preview
    final MenuBar previewMenuBar = new MenuBar();
    previewMenuBar.addItem(new Html("<h3>Preview</h3>"));
    previewMenuBar.addItem(saveButton);
    previewMenuBar.addItem(refreshButton);

    final VerticalLayout previewLayout = LayoutFactory.getVerticalLayout();
    previewLayout.add(previewMenuBar);
    previewLayout.add(markdown);


    final HorizontalLayout horizontalLayout = LayoutFactory.getHorizontalLayout();
    horizontalLayout.add(editorLayout);
    horizontalLayout.add(previewLayout);
    add(horizontalLayout);
  }

  private @NotNull Button getSaveButton() {
    final Button saveButton = new Button("Save", new Icon(VaadinIcon.STORAGE));
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    saveButton.addClickListener(event -> {
      final String selectedStringDate = getSelectedStringDate(selectedDate);
      noteService.saveOrUpdate(selectedStringDate, new NoteDto(selectedStringDate, currentContent, currentTags));
    });
    return saveButton;
  }

  private Button getRefreshButton(final TextArea textArea, final Markdown markdown) {
    final Button refreshButton = new Button("Refresh", new Icon(VaadinIcon.REFRESH));
    refreshButton.addClickListener(event -> {
      markdown.setContent(textArea.getValue());
    });
    return refreshButton;
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
