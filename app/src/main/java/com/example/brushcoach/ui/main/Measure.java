package com.example.brushcoach.ui.main;

import android.widget.ProgressBar;
import android.widget.TextView;

public class Measure {

    private boolean needsUpdate;
    private int value;
    private ProgressBar progressBar;
    private TextView textView;

    public Measure (ProgressBar progressBar, TextView textView) {
        this.needsUpdate = true;
        this.value = 0;
        this.progressBar = progressBar;
        this.textView = textView;
    }

    public boolean needsUpdate () {
        return this.needsUpdate;
    }

    public void setNeedsUpdate (boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public int getValue () {
        return value;
    }

    public void setValue (int value) {
        this.value = value;
        this.needsUpdate = true;
    }

    public ProgressBar getProgressBar () {
        return this.progressBar;
    }

    public TextView getTextView () {
        return this.textView;
    }
}
