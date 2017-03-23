package com.improvdojo.chordtonemapper;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wmoore on 3/16/17.
 */

public class NoteTests {
    @Test
    public void nextNoteTest() throws Exception {
        assertEquals(Note.F, Note.getNextNote(Note.E));
    }

    @Test
    public void prevNoteTest() throws Exception {
        assertEquals(Note.B, Note.getPrevNote(Note.C));
    }

    @Test
    public void intervalTest() throws Exception {
        assertEquals(Note.DE, Note.getInterval(Note.C, 3));
        assertEquals(Note.C, Note.getInterval(Note.C, 12));
    }
}
