package com.improvdojo.chordtonemapper;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final int NUMBER_STRINGS = 6;
    public static final int NUMBER_FRETS = 18;
    Note[] tuning = {Note.E, Note.A, Note.D, Note.G, Note.B, Note.E };
    ArrayList<Integer> dots = new ArrayList<>(Arrays.asList(0, 3, 5, 7, 9, 12));
    TextView[] dotViews = new TextView[NUMBER_FRETS];
    Interval[] chord = {};
    Note root = Note.A;
    Note[] chordNotes = Chord.getChord(root, chord);
    //ArrayList<Note> chordNotesArrayList = new ArrayList<>(Arrays.asList(chordNotes));

    GridLayout fingerboard;

    NoteView[][] noteViews = new NoteView[NUMBER_STRINGS][NUMBER_FRETS];
    InstrumentString[] strings = new InstrumentString[NUMBER_STRINGS];

    private void setNoteVisibility(Note note, boolean show) {
        for (int stringPos = 0; stringPos < NUMBER_STRINGS; stringPos++) {
            for (int fretPos = 0; fretPos < NUMBER_FRETS; fretPos++) {
                NoteView view = noteViews[stringPos][fretPos];
                if(view.getNote() == note) {
                    view.setShow(show);
                }
            }
        }
    }

    private void setNoteVisibility(Note[] notes, boolean show){
        for(int i = 0; i < notes.length; i++){
            Note note = notes[i];
            setNoteVisibility(note, show);
        }
    }

    private void changeChord(Note[] fromNotes, Note[] toNotes){
        setNoteVisibility(fromNotes, false);
        setNoteVisibility(toNotes, true);
    }

    private void addNoteButtons(){
        LinearLayout rootButtons = (LinearLayout) findViewById(R.id.rootButtonLayout);
        for(final Note n : Note.notes){
            Button b = new Button(this);
            b.setText(n.getDisplay());
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    root = n;
                    selectChord(root, chord);
                }
            });
            rootButtons.addView(b);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        fingerboard = (GridLayout) findViewById(R.id.fingerboard);
        fingerboard.setColumnCount(NUMBER_STRINGS + 1);
        fingerboard.setRowCount(NUMBER_FRETS);

        for (int fretPos = 0; fretPos < NUMBER_FRETS; fretPos++) {
            // Notes:
            for (int stringPos = 0; stringPos < NUMBER_STRINGS; stringPos++) {
                strings[stringPos] = new InstrumentString(tuning[stringPos], NUMBER_FRETS);
                Note note = strings[stringPos].getNotes()[fretPos];
                NoteView noteView = new NoteView(this, stringPos, fretPos, note, false);
                noteViews[stringPos][fretPos] = noteView;
                GridLayout.Spec row = GridLayout.spec(fretPos);
                GridLayout.Spec col = GridLayout.spec(stringPos+1);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
                noteView.setLayoutParams(params);
                fingerboard.addView(noteView, params);
            }
            // Dots:
            TextView dotView = new TextView(this);
            dotView.setTextColor(Color.BLACK);
            //dotView.setBackgroundColor(Color.MAGENTA);
            dotView.setGravity(Gravity.CENTER);
            dotView.setTextSize(20.0f);
            if(dots.contains(fretPos)) {
                dotView.setText( Integer.toString(fretPos));
            }
            GridLayout.Spec dotCol = GridLayout.spec(0);
            GridLayout.Spec dotRow = GridLayout.spec(fretPos);
            GridLayout.LayoutParams dotParams = new GridLayout.LayoutParams(dotRow, dotCol);
            dotView.setLayoutParams(dotParams);
            dotViews[fretPos] = dotView;
            fingerboard.addView(dotView, dotParams);
        }

        addNoteButtons();
        selectMinor(null);

        fingerboard.getViewTreeObserver().addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    final int MARGIN = 5;

                    int pWidth = fingerboard.getWidth();
                    int pHeight = fingerboard.getHeight();
                    int numOfCol = fingerboard.getColumnCount();
                    int numOfRow = fingerboard.getRowCount();
                    int w = pWidth / numOfCol;
                    //int h = pHeight / numOfRow;

                    for (int xPos = 0; xPos < numOfRow; xPos++) {
                        // Notes:
                        for (int yPos = 0; yPos < numOfCol-1; yPos++) {
                            GridLayout.LayoutParams params =
                                    (GridLayout.LayoutParams) noteViews[yPos][xPos].getLayoutParams();
                            params.width = w - 2 * MARGIN;
                            params.height = w - 2 * MARGIN;
                            params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                            noteViews[yPos][xPos].setLayoutParams(params);
                        }
                        // Dots:
                        GridLayout.LayoutParams dotParams =
                                (GridLayout.LayoutParams) dotViews[xPos].getLayoutParams();
                        dotParams.width = w - 2 * MARGIN;
                        dotParams.height = w - 2 * MARGIN;
                        dotParams.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                        dotViews[xPos].setLayoutParams(dotParams);
                    }
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_settings:
                // User chose the "Open Settings" item, show the app settings UI...
                openSettings();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void selectChord(Note root, Interval[] chord){
        Note[] chordNotes = Chord.getChord(root, chord);
        changeChord(this.chordNotes, chordNotes);
        this.chord = chord;
        this.chordNotes = chordNotes;
    }

    public void selectDiminished(View view) {
        Interval[] chord = {
                Interval.MINOR_THIRD,
                Interval.MINOR_THIRD
        };
        selectChord(root, chord);
    }

    public void selectMinor(View view) {
        Interval[] chord = {
                Interval.MINOR_THIRD,
                Interval.MAJOR_THIRD
        };
        selectChord(root, chord);
    }

    public void selectMajor(View view) {
        Interval[] chord = {
                Interval.MAJOR_THIRD,
                Interval.MINOR_THIRD
        };
        selectChord(root, chord);
    }

    public void selectAugmented(View view) {
        Interval[] chord = {
                Interval.MAJOR_THIRD,
                Interval.MAJOR_THIRD
        };
        selectChord(root, chord);
    }
}

