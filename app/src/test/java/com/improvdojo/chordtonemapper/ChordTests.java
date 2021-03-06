package com.improvdojo.chordtonemapper;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wmoore on 3/16/17.
 */

public class ChordTests {
    @Test
    public void testChordNotes() throws Exception {
        // Diminished chord
        Interval[] intervals = {
            Interval.MINOR_THIRD,
            Interval.MINOR_THIRD,
            Interval.MINOR_THIRD
        };
        Note[] notes = Chord.getChordFromStacked(Note.C, intervals);
        assertEquals(Note.C, notes[0]);
        assertEquals(Note.DE, notes[1]);
        assertEquals(Note.FG, notes[2]);
        assertEquals(Note.A, notes[3]);
    }

    @Test
    public void testChordHalfSteps() throws Exception {
        // Major 7th chord
        int[] half_steps = {4, 7, 11};
        Note[] notes = Chord.getChord(Note.C, half_steps);
        assertEquals(Note.C, notes[0]);
        assertEquals(Note.E, notes[1]);
        assertEquals(Note.G, notes[2]);
        assertEquals(Note.B, notes[3]);
    }
}
