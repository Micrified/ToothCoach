package com.example.brushcoach.ui.main;

import android.widget.Button;

public class xButton {

    Button button;
    Boolean active;

    public xButton (Button button, Boolean active) {
        this.button = button;
        this.active = active;
    }

    public Button getButton () {
        return button;
    }

    public void setButton (Button button) {
        this.button = button;
    }

    public Boolean getIsActive () {
        return this.active;
    }

    public void setIsActive (boolean active) {
        this.active = active;
    }
}
