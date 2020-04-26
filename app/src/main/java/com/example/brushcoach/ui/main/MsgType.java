package com.example.brushcoach.ui.main;



public enum MsgType {
    MSG_TYPE_STATUS((byte)0), MSG_TYPE_RESULT((byte)1), MSG_TYPE_REQUEST((byte)2), MSG_TYPE_MAX((byte)3);

    public byte byteVal;

    MsgType(byte byteVal) {
        this.byteVal = byteVal;
    }

    public byte getByteValue() {
        return byteVal;
    }

    public static MsgType fromByte(byte b) {
        for (MsgType type : values()) {
            if (type.getByteValue() == b) {
                return type;
            }
        }
        return null;
    }
}


