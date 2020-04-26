package com.example.brushcoach.ui.main;

import android.view.View;

public class AnimateView {

    private View view;
    private Boolean shouldAnimate;

    public AnimateView (View view, Boolean shouldAnimate) {
        this.view = view;
        this.shouldAnimate = shouldAnimate;
    }

    public View getView () {
        return this.view;
    }

    public Boolean shouldAnimate() {
        return shouldAnimate;
    }

    public void setShouldAnimate(Boolean shouldAnimate) {
        this.shouldAnimate = shouldAnimate;
    }
}
