package com.improvdojo.chordtonemapper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final int NUMBER_STRINGS = 4;
    public static final int NUMBER_FRETS = 18;

    Dialog screenDialog;
    static final int ID_STRINGS_DIALOG = 1;
    static final int ID_QUALITY_DIALOG = 2;
    static final int ID_SIXTHS_DIALOG = 3;
    static final int ID_SEVENTHS_DIALOG = 4;
    static final int ID_NINTHS_DIALOG = 5;

    Note[] tuning = {Note.G, Note.D, Note.A, Note.E};
    ArrayList<Integer> dots = new ArrayList<>(Arrays.asList(0, 3, 5, 7, 9, 12));
    TextView[] dotViews = new TextView[NUMBER_FRETS];
    Interval[] chord = {};
    Note root = Note.A;
    Note[] chordNotes = Chord.getChord(root, chord);
    //ArrayList<Note> chordNotesArrayList = new ArrayList<>(Arrays.asList(chordNotes));

    GridLayout fingerboard;

    NoteView[][] noteViews = new NoteView[NUMBER_STRINGS][NUMBER_FRETS];
    InstrumentString[] strings = new InstrumentString[NUMBER_STRINGS];
    Button lastSelectedNote;

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
            int c = Note.getColor(n);
            b.setBackgroundColor(c);
            b.setText(n.getDisplay());
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    if(lastSelectedNote != null){
                        //lastSelectedNote.set
                    }
                    root = n;
                    selectChord(root, chord);
                }
            });
            rootButtons.addView(b);
        }
    }

//    private void populateQualities(){
//        final Spinner spinner = (Spinner) findViewById(R.id.quality_spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//            this,
//            R.array.qualities,
//            android.R.layout.simple_spinner_item
//        );
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int arg2, long arg3) {
//                String q = spinner.getSelectedItem().toString();
//                if(q.equals(getString(R.string.diminished))){
//                    selectDiminished(arg0);
//                }else if(q.equals(getString(R.string.minor))){
//                    selectMinor(arg0);
//                }else if(q.equals(getString(R.string.major))){
//                    selectMajor(arg0);
//                }else if(q.equals(getString(R.string.augmented))){
//                    selectAugmented(arg0);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//        spinner.setSelection(0);
//    }

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
        //selectMinor(null);
        //populateQualities();

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

    @Override
    protected Dialog onCreateDialog(int id) {
        screenDialog = null;
        switch(id){
            case(ID_STRINGS_DIALOG):
                screenDialog = createStringsDialog();
                break;
            case(ID_QUALITY_DIALOG):
                screenDialog = createQualitiesDialog();
                break;
            case(ID_SIXTHS_DIALOG):
                screenDialog = createSixthsDialog();
                break;
            case(ID_SEVENTHS_DIALOG):
                screenDialog = createSeventhsDialog();
                break;
            case(ID_NINTHS_DIALOG):
                screenDialog = createNinthsDialog();
                break;
            default:
                break;
        }
        return screenDialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id){
            case(ID_STRINGS_DIALOG):
                prepareStringsDialog(dialog);
                break;
            case(ID_QUALITY_DIALOG):
                break;
            case(ID_SIXTHS_DIALOG):
                break;
            case(ID_SEVENTHS_DIALOG):
                break;
            case(ID_NINTHS_DIALOG):
                break;
            default:
                break;
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

    public void selectNoQuality(View view) {
        Interval[] chord = {};
        selectChord(root, chord);
    }

    public void addInterval(Interval interval){
        Interval[] newChord = Interval.addInterval(this.chord, interval);
        selectChord(root, newChord);
    }

    public void removeInterval(Interval interval){
        Interval[] newChord = Interval.removeInterval(this.chord, interval);
        selectChord(root, newChord);
    }

    public void removeIntervals(Interval[] removes){
        Interval[] newChord = Interval.removeIntervals(this.chord, removes);
        selectChord(root, newChord);
    }

    public void selectDiminished(View view) {
        Interval[] chord = {
                Interval.MINOR_THIRD,
                Interval.TRITONE
        };
        selectChord(root, chord);
    }

    public void selectMinor(View view) {
        Interval[] chord = {
                Interval.MINOR_THIRD,
                Interval.PERFECT_FIFTH
        };
        selectChord(root, chord);
    }

    public void selectMajor(View view) {
        Interval[] chord = {
                Interval.MAJOR_THIRD,
                Interval.PERFECT_FIFTH
        };
        selectChord(root, chord);
    }

    public void selectAugmented(View view) {
        Interval[] chord = {
                Interval.MAJOR_THIRD,
                Interval.MINOR_SIXTH
        };
        selectChord(root, chord);
    }

    public void showStringsDialog(View view){
        showDialog(ID_STRINGS_DIALOG);
    }

    private Dialog createStringsDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.strings_dialog);
        LinearLayout instrumentLayout = (LinearLayout) dialog.findViewById(R.id.strings_layout);

        for(Note n : tuning){
            final LinearLayout stringLayout = new LinearLayout(this);
            stringLayout.setOrientation(LinearLayout.HORIZONTAL);
            final Spinner note_spinner = new Spinner(this);
            ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(
                this,
                android.R.layout.simple_spinner_item,
                Note.values()
            );
            note_spinner.setAdapter(adapter);
            note_spinner.setSelection(Note.notes.indexOf(n));
            Button delete_button = new Button(this);
            delete_button.setText("x");
            delete_button.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    // TODO Remove String
                    note_spinner.removeView(stringLayout);
                }
            });
            stringLayout.addView(note_spinner);
            stringLayout.addView(delete_button);
            instrumentLayout.addView(stringLayout);
        }
        return dialog;
    }

    private void prepareStringsDialog(Dialog dialog){
        dialog.setTitle(R.string.string_dialog_title);
    }

    public void showQualitiesDialog(View view){
        showDialog(ID_QUALITY_DIALOG);
    }

    private Dialog createQualitiesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Resources res = getResources();
        final String[] qualityArray = res.getStringArray(R.array.qualities);
        builder
            .setTitle(R.string.quality_dialog_title)
            .setItems(R.array.qualities, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String q = qualityArray[which];

                    if(q.equals(getString(R.string.no_quality))){
                        selectNoQuality(null);
                    }else if(q.equals(getString(R.string.diminished))){
                        selectDiminished(null);
                    }else if(q.equals(getString(R.string.minor))){
                        selectMinor(null);
                    }else if(q.equals(getString(R.string.major))){
                        selectMajor(null);
                    }else if(q.equals(getString(R.string.augmented))){
                        selectAugmented(null);
                    }
                }
            });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void showSixthsDialog(View view){
        showDialog(ID_SIXTHS_DIALOG);
    }

    private Dialog createSixthsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Resources res = getResources();
        final String[] sixthsArray = res.getStringArray(R.array.sixths);
        builder
            .setTitle(R.string.sixth_dialog_title)
            .setItems(R.array.sixths, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String q = sixthsArray[which];

                    if(q.equals(getString(R.string.no_sixth))){
                        Interval[] removes = { Interval.MINOR_SIXTH, Interval.MAJOR_SIXTH };
                        removeIntervals(removes);
                    }else if(q.equals(getString(R.string.minor_sixth))){
                        addInterval(Interval.MINOR_SIXTH);
                    }else if(q.equals(getString(R.string.major_sixth))){
                        addInterval(Interval.MAJOR_SIXTH);
                    }
                }
            });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void showSeventhsDialog(View view){
        showDialog(ID_SEVENTHS_DIALOG);
    }

    private Dialog createSeventhsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Resources res = getResources();
        final String[] seventhsArray = res.getStringArray(R.array.sevenths);
        builder
            .setTitle(R.string.seventh_dialog_title)
            .setItems(R.array.sevenths, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String q = seventhsArray[which];

                    if(q.equals(getString(R.string.no_seventh))){
                        Interval[] removes = { Interval.MINOR_SEVENTH, Interval.MAJOR_SEVENTH };
                        removeIntervals(removes);
                    }else if(q.equals(getString(R.string.minor_seventh))){
                        addInterval(Interval.MINOR_SEVENTH);
                    }else if(q.equals(getString(R.string.major_seventh))){
                        addInterval(Interval.MAJOR_SEVENTH);
                    }
                }
            });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void showNinthsDialog(View view){
        showDialog(ID_NINTHS_DIALOG);
    }

    private Dialog createNinthsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Resources res = getResources();
        final String[] ninthsArray = res.getStringArray(R.array.ninths);
        builder
            .setTitle(R.string.ninth_dialog_title)
            .setItems(R.array.ninths, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String q = ninthsArray[which];

                    if(q.equals(getString(R.string.no_ninth))){
                        Interval[] removes = { Interval.MINOR_SECOND, Interval.MAJOR_SECOND };
                        removeIntervals(removes);
                    }else if(q.equals(getString(R.string.minor_ninth))){
                        addInterval(Interval.MINOR_SECOND);
                    }else if(q.equals(getString(R.string.major_ninth))){
                        addInterval(Interval.MAJOR_SECOND);
                    }
                }
            });
        AlertDialog dialog = builder.create();
        return dialog;
    }
}

