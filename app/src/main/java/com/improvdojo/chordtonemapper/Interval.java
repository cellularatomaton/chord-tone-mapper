package com.improvdojo.chordtonemapper;

/**
 * Created by wmoore on 3/16/17.
 */

public enum Interval {
    MINOR_SECOND("m2", 1),
    MAJOR_SECOND("M2", 2),
    MINOR_THIRD("m3", 3),
    MAJOR_THIRD("M3", 4),
    TRITONE("T", 5);

    String display;
    int half_steps;

    Interval(String display, int half_steps){
        this.display = display;
        this.half_steps = half_steps;
    }
}
