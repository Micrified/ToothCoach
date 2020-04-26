package com.example.brushcoach.ui.main;


import java.nio.ByteBuffer;

public class StatusMessage {

    // Modes that device can be in
    enum Mode {
        MODE_IDLE,
        MODE_TRAIN,
        MODE_BRUSH,
        MODE_MAX
    }

    // Brushing zones
    enum Zone {
        BRUSH_ZONE_LL,
        BRUSH_ZONE_LR,
        BRUSH_ZONE_TL,
        BRUSH_ZONE_TR,

        BRUSH_ZONE_MAX
    }

    // Status message fields
    private byte mode, zone, rate, progress;

    public StatusMessage (byte mode, byte zone, byte rate, byte progress) {
        this.mode = mode;
        this.zone = zone;
        this.rate = rate;
        this.progress = progress;
    }

    public Mode getMode () {
        switch (this.mode) {
            case 0: return Mode.MODE_IDLE;
            case 1: return Mode.MODE_TRAIN;
            case 2: return Mode.MODE_BRUSH;
        }
        return Mode.MODE_MAX;
    }

    public Zone getZone () {
        switch (this.zone) {
            case 0: return Zone.BRUSH_ZONE_LL;
            case 1: return Zone.BRUSH_ZONE_LR;
            case 2: return Zone.BRUSH_ZONE_TL;
            case 3: return Zone.BRUSH_ZONE_TR;
        }
        return Zone.BRUSH_ZONE_MAX;
    }

    public short getRate () {
        return ByteBuffer.wrap(new byte[]{0x00, this.rate}).getShort();
    }

    public short getProgress () {
        return ByteBuffer.wrap(new byte[]{0x00, this.progress}).getShort();
    }
}
