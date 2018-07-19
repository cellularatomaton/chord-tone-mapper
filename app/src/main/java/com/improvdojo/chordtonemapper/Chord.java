package com.improvdojo.chordtonemapper;

/**
 * Created by wmoore on 3/16/17.
 */

public class Chord {
    public static Note[] getChordFromStacked(Note root, Interval[] stacked_intervals){
        int[] half_steps = Interval.getChromaticsFromStacked(stacked_intervals);
        return getChord(root, half_steps);
    }

    public static Note[] getChord(Note root, Interval[] intervals){
        int[] chromatics = Interval.getChromatics(intervals);
        return getChord(root, chromatics);
    }

    public static Note[] getChord(Note root, int[] half_steps){
        Note[] chord = new Note[half_steps.length+1];
        chord[0] = root;
        for(int i = 0; i < half_steps.length; i++){
            int interval = half_steps[i];
            Note current = Note.getInterval(root, interval);
            chord[i+1] = current;
        }
        return chord;
    }
}
