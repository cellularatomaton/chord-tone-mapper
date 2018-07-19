package com.improvdojo.chordtonemapper;

import android.util.ArraySet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wmoore on 3/16/17.
 */

public enum Interval {
    MINOR_SECOND("m2", 1),
    MAJOR_SECOND("M2", 2),
    MINOR_THIRD("m3", 3),
    MAJOR_THIRD("M3", 4),
    PERFECT_FOURTH("P4", 5),
    TRITONE("T", 6),
    PERFECT_FIFTH("P5", 7),
    MINOR_SIXTH("m6", 8),
    MAJOR_SIXTH("M6", 9),
    MINOR_SEVENTH("m7", 10),
    MAJOR_SEVENTH("M7", 11);

    public static final Interval[] intervals = Interval.values();
    String display;
    int half_steps;

    Interval(String display, int half_steps){
        this.display = display;
        this.half_steps = half_steps;
    }

    public static int[] getChromatics(Interval[] intervals){
        int[] half_steps = new int[intervals.length];
        for(int i = 0; i < intervals.length; i++){
            half_steps[i] = intervals[i].half_steps;
        }
        return half_steps;
    }

    public static int[] getChromaticsFromStacked(Interval[] stacked_intervals){
        int[] half_steps = new int[stacked_intervals.length];
        int current_interval = 0;
        for(int i = 0; i < stacked_intervals.length; i++){
            current_interval += stacked_intervals[i].half_steps;
            half_steps[i] =current_interval;
        }
        return half_steps;
    }

    public static Interval[] getIntervalsFromChromatics(int[] chromatics){
        Interval[] intervals = new Interval[chromatics.length];
        for(int i = 0; i < chromatics.length; i++){
            intervals[i] = Interval.intervals[chromatics[i]-1];
        }
        return intervals;
    }

    public static Interval[] getIntervalsFromStacked(Interval[] stacked_intervals){
        int[] chromatics = getChromaticsFromStacked(stacked_intervals);
        return getIntervalsFromChromatics(chromatics);
    }

    public static Interval[] mergeIntervals(Interval[] ia, Interval[] ib){
        Set<Interval> intervals = new HashSet<>();
        intervals.addAll(Arrays.asList(ia));
        intervals.addAll(Arrays.asList(ib));
        Interval[] result = new Interval[intervals.size()];
        return intervals.toArray(result);
    }

    public static Interval[] addInterval(Interval[] intervals, Interval add){
        Interval[] newIntervals = {add};
        return Interval.mergeIntervals(intervals, newIntervals);
    }

    public static Interval[] removeIntervals(Interval[] intervals, Interval[] removes){
        Set<Interval> newIntervals = new HashSet<>();
        List<Interval> removeList = Arrays.asList(removes);
        for(Interval i : intervals){
            if (!removeList.contains(i)) {
                newIntervals.add(i);
            }
        }
        Interval[] result = new Interval[newIntervals.size()];
        return newIntervals.toArray(result);
    }

    public static Interval[] removeInterval(Interval[] intervals, Interval remove){
        Interval[] removes = {remove};
        return removeIntervals(intervals, removes);
    }
}
