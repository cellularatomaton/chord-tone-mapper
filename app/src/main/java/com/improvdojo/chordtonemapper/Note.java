package com.improvdojo.chordtonemapper;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by wmoore on 3/13/17.
 */

public enum Note {
    C("C"),
    CD(String.format("D%s", Note.flat)),
    D("D"),
    DE(String.format("E%s", Note.flat)),
    E("E"),
    F("F"),
    FG(String.format("G%s", Note.flat)),
    G("G"),
    GA(String.format("A%s", Note.flat)),
    A("A"),
    AB(String.format("B%s", Note.flat)),
    B("B");

    public static final String flat = "\u266D";
    public static final String sharp = "\u266F";
    public static final ArrayList<Note> notes = new ArrayList<>(Arrays.asList(Note.values()));
    public static final ArrayList<Note> fifths = getCircle(Note.values(), Interval.PERFECT_FIFTH);
    public static final ArrayList<Note> fourths = getCircle(Note.values(), Interval.PERFECT_FOURTH);

    private final String display;

    Note(String display){
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }

    public String getDisplay() {
        return display;
    }

    public static Note getNextNote(Note note){
        return getInterval(note, 1);
    }

    public static Note getPrevNote(Note note){
        return getInterval(note, 11);
    }

    public static int getInterval(int root, int chromatic_interval){
        return (root + chromatic_interval) % notes.size();
    }

    public static Note getInterval(Note root, int chromatic_interval){
        int i = notes.indexOf(root);
        int index = getInterval(i, chromatic_interval);
        return notes.get(index);
    }

    public static ArrayList<Note> getCircle(Note[] notes, Interval interval){
        Note[] fifths = new Note[notes.length];
        int current = 0;
        for(int i = 0; i < notes.length; i++){
            fifths[i] = notes[getInterval(0, current)];
            current += interval.half_steps;
        }
        return new ArrayList<>(Arrays.asList(fifths));
    }

    public static int getColor(Note note){
        return getFifthsColor(note);
    }

    public static int getChromaticColor(Note note){
        int i = notes.indexOf(note);
        float[] hsv = { ((float)i/notes.size())*360, 1.0f, 1.0f };
        return Color.HSVToColor(hsv);
    }

    public static int getFifthsColor(Note note){
        int i = fifths.indexOf(note);
        float[] hsv = { ((float)i/notes.size())*360, 1.0f, 1.0f };
        return Color.HSVToColor(hsv);
    }

    public static int getFourthsColor(Note note){
        int i = fourths.indexOf(note);
        float[] hsv = { ((float)i/notes.size())*360, 1.0f, 1.0f };
        return Color.HSVToColor(hsv);
    }
}
