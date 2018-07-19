package com.improvdojo.chordtonemapper;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wmoore on 3/24/17.
 */

public class IntervalTests {

    @Test
    public void testChromatics() throws Exception {
        // Major 7th chord
        Interval[] intervals = {
                Interval.MAJOR_THIRD,
                Interval.PERFECT_FIFTH,
                Interval.MAJOR_SEVENTH
        };
        int[] chromatics = Interval.getChromatics(intervals);
        assertEquals(4, chromatics[0]);
        assertEquals(7, chromatics[1]);
        assertEquals(11, chromatics[2]);
    }

    @Test
    public void testChromaticFromStacked() throws Exception {
        // Minor 7th chord (In stacked 3rds)
        Interval[] intervals = {
                Interval.MINOR_THIRD,
                Interval.MAJOR_THIRD,
                Interval.MINOR_THIRD
        };
        int[] chromatics = Interval.getChromaticsFromStacked(intervals);
        assertEquals(3, chromatics[0]);
        assertEquals(7, chromatics[1]);
        assertEquals(10, chromatics[2]);
    }

    @Test
    public void testIntervalsFromChromatics() throws Exception {
        // Diminished chord (In chromatics)
        int[] chromatics = {3, 6, 9};
        Interval[] intervals = Interval.getIntervalsFromChromatics(chromatics);
        assertEquals(Interval.MINOR_THIRD, intervals[0]);
        assertEquals(Interval.TRITONE, intervals[1]);
        assertEquals(Interval.MAJOR_SIXTH, intervals[2]);
    }

    @Test
    public void testIntervalsFromStacked() throws Exception {
        // Augmented 7th chord (In stacked intervals)
        Interval[] stacked_intervals = {
                Interval.MAJOR_THIRD,
                Interval.MAJOR_THIRD,
                Interval.MAJOR_SECOND
        };
        Interval[] intervals = Interval.getIntervalsFromStacked(stacked_intervals);
        assertEquals(Interval.MAJOR_THIRD, intervals[0]);
        assertEquals(Interval.MINOR_SIXTH, intervals[1]);
        assertEquals(Interval.MINOR_SEVENTH, intervals[2]);
    }

    @Test
    public void testMergeIntervals() throws Exception {
        // Major triad:
        Interval[] triad = {
                Interval.MAJOR_THIRD,
                Interval.PERFECT_FIFTH
        };
        // Extensions:
        // Some extra intervals:
        Interval[] extensions = {
                Interval.PERFECT_FIFTH,
                Interval.MAJOR_SEVENTH
        };
        Interval[] result = Interval.mergeIntervals(triad, extensions);
        assertEquals(3, result.length);
    }
}
