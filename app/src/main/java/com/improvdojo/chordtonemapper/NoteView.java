package com.improvdojo.chordtonemapper;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * Created by wmoore on 3/11/17.
 */

public class NoteView extends View {

    int stringPos;
    int fretPos;
    Note note;
    int color;
    int noColor;
    boolean show;

    public NoteView(Context context, int stringPos, int fretPos, Note note, boolean show) {
        super(context);
        this.stringPos = stringPos;
        this.fretPos = fretPos;
        this.note = note;
        this.color = Note.getColor(note);
        float[] noColorArray = {0.0f, 0.0f, 0.0f};
        this.noColor = Color.HSVToColor(noColorArray);
        this.show = show;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(show) {
            canvas.drawColor(color);
        }else{
            canvas.drawColor(noColor);
        }
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public Note getNote(){
        return note;
    }
}
