package com.improvdojo.chordtonemapper;

/**
 * Created by wmoore on 3/16/17.
 */

public class Chord {
    public static Note[] getChord(Note root, Interval[] stacked_intervals){
        int[] half_steps = getChromaticIntervals(stacked_intervals);
        return getChord(root, half_steps);
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

    public static int[] getChromaticIntervals(Interval[] stacked_intervals){
        int[] half_steps = new int[stacked_intervals.length];
        int current_interval = 0;
        for(int i = 0; i < stacked_intervals.length; i++){
            current_interval += stacked_intervals[i].half_steps;
            half_steps[i] =current_interval;
        }
        return half_steps;
    }
}
