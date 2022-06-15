package com.example.notes;

import com.example.model.Note;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class ControllerTest {

    @Test
    public void findNoteTileEqual() {
        Controller controller = new Controller();
        Note note = new Note();
        Note noteToFind = new Note();
        noteToFind.setId("asd");
        noteToFind.setTitle("Title1");
        noteToFind.setText("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">Text1</span></p></body></html>");
        noteToFind.setCreatedOn(LocalDateTime.now());
        NoteEditController noteEditController = new NoteEditController();
        noteEditController.add(noteToFind);
        note = controller.findNote("Title1");
        assertEquals(note.getTitle(),noteToFind.getTitle());
    }
    @Test
    public void findNoteTextEqual() {
        Controller controller = new Controller();
        Note note = new Note();
        Note noteToFind = new Note();
        noteToFind.setId("asd");
        noteToFind.setTitle("Title2");
        noteToFind.setText("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">Text1</span></p></body></html>");
        noteToFind.setCreatedOn(LocalDateTime.now());
        NoteEditController noteEditController = new NoteEditController();
        noteEditController.add(noteToFind);
        note = controller.findNote("Title2");
        assertEquals(note.getText(),noteToFind.getText());
    }
    @Test
    public void findNoteIdEqual() {
        Controller controller = new Controller();
        Note note = new Note();
        Note noteToFind = new Note();
        noteToFind.setId("asd");
        noteToFind.setTitle("Title3");
        noteToFind.setText("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">Text1</span></p></body></html>");
        noteToFind.setCreatedOn(LocalDateTime.now());
        NoteEditController noteEditController = new NoteEditController();
        noteEditController.add(noteToFind);
        note = controller.findNote("Title3");
        assertEquals(note.getId(),noteToFind.getId());
    }
    @Test
    public void findNoteCreatedOnEqual() {
        Controller controller = new Controller();
        Note note = new Note();
        Note noteToFind = new Note();
        noteToFind.setId("asd");
        noteToFind.setTitle("Title4");
        noteToFind.setText("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">Text1</span></p></body></html>");
        noteToFind.setCreatedOn(LocalDateTime.now());
        NoteEditController noteEditController = new NoteEditController();
        noteEditController.add(noteToFind);
        note = controller.findNote("Title4");
        assertEquals(note.getCreatedOn(),noteToFind.getCreatedOn());
    }
}