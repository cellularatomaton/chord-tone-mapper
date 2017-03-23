package com.improvdojo.chordtonemapper;

/**
 * Created by wmoore on 3/13/17.
 */

public class InstrumentString {

    private Note[] notes;

    InstrumentString(Note openNote, int numberFrets){
        this.notes = new Note[numberFrets];
        Note currentNote = openNote;
        for(int i = 0; i < numberFrets; i++){
            this.notes[i] = currentNote;
            currentNote = Note.getNextNote(currentNote);
        }
    }

    public Note[] getNotes() {
        return notes;
    }
}
