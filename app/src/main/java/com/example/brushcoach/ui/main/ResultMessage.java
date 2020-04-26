package com.example.brushcoach.ui.main;


import java.nio.ByteBuffer;

public class ResultMessage {

    // Brushing zones
    enum Zone {
        BRUSH_ZONE_LL,
        BRUSH_ZONE_LR,
        BRUSH_ZONE_TL,
        BRUSH_ZONE_TR,

        BRUSH_ZONE_MAX
    }

    // Status message fields
    private byte id;
    private byte progress_zone_ll, rate_zone_ll;
    private byte progress_zone_lr, rate_zone_lr;
    private byte progress_zone_tl, rate_zone_tl;
    private byte progress_zone_tr, rate_zone_tr;

    public ResultMessage (byte id,
                          byte progress_zone_ll, byte rate_zone_ll,
                          byte progress_zone_lr, byte rate_zone_lr,
                          byte progress_zone_tl, byte rate_zone_tl,
                          byte progress_zone_tr, byte rate_zone_tr)
    {
        this.id = id;
        this.progress_zone_ll = progress_zone_ll;
        this.rate_zone_ll     = rate_zone_ll;
        this.progress_zone_lr = progress_zone_lr;
        this.rate_zone_lr     = rate_zone_lr;
        this.progress_zone_tl = progress_zone_tl;
        this.rate_zone_tl     = rate_zone_tl;
        this.progress_zone_tr = progress_zone_tr;
        this.rate_zone_tr     = rate_zone_tr;
    }

    public short getID () {
        return ByteBuffer.wrap(new byte[]{0x00, this.id}).getShort();
    }

    public short getProgress (Zone zone) {
        byte select = 0x0;
        switch (zone) {
            case BRUSH_ZONE_LL: select = this.progress_zone_ll; break;
            case BRUSH_ZONE_LR: select = this.progress_zone_lr; break;
            case BRUSH_ZONE_TL: select = this.progress_zone_tl; break;
            case BRUSH_ZONE_TR: select = this.progress_zone_tr; break;

            case BRUSH_ZONE_MAX: select = 0x0; break;
        }
        return ByteBuffer.wrap(new byte[]{0x00, select}).getShort();
    }

    public short getRate (Zone zone) {
        byte select = 0x0;
        switch (zone) {
            case BRUSH_ZONE_LL: select = this.rate_zone_ll; break;
            case BRUSH_ZONE_LR: select = this.rate_zone_lr; break;
            case BRUSH_ZONE_TL: select = this.rate_zone_tl; break;
            case BRUSH_ZONE_TR: select = this.rate_zone_tr; break;

            case BRUSH_ZONE_MAX: select = 0x0; break;
        }
        return ByteBuffer.wrap(new byte[]{0x00, select}).getShort();
    }
}
