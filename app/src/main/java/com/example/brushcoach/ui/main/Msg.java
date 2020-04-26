package com.example.brushcoach.ui.main;

import android.util.Log;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Msg {


    /* ********** Message Metadata ********** */

    // The maximum message byte size
    public static final int MSG_BUFFER_MAX = 256;

    // The message header byte
    public static final byte MSG_BYTE_HEAD = (byte)0xFF;


    // Table containing message body sizes
    private int g_msg_size_tab[] = new int[] {
            4,            // Status message (4 bytes)
            9,            // Result message (9 bytes)
            0             // Request message (0 bytes)
    };


    /* ********** Message Fields ********** */

    // The message type
    private MsgType msgType = MsgType.MSG_TYPE_MAX;

    // The message status data fields
    private byte status_mode;
    private byte status_zone;
    private byte status_rate;
    private byte status_progress;

    // The message result data fields
    private byte result_id;
    private byte result_progress_zone_ll;
    private byte result_rate_zone_ll;
    private byte result_progress_zone_lr;
    private byte result_rate_zone_lr;
    private byte result_progress_zone_tl;
    private byte result_rate_zone_tl;
    private byte result_progress_zone_tr;
    private byte result_rate_zone_tr;

    // No data fields for the request


    /* ********** Internal Properties ********** */

    // String containing internal error message
    public String errorDescription = null;

    // Encloses a buffer and its length (instead of capacity)
    class MsgData {
        public int len;
        public byte[] data;

        public MsgData (int len, byte[] data) {
            this.len = len;
            this.data = data;
        }
    }


    /* ********** Constructors ********** */


    public Msg () {
        this.msgType = MsgType.MSG_TYPE_MAX;

        // Error description
        this.errorDescription = null;
    }


    /* ********** Message Configuration Methods ********** */

    // Configures the instance to be a status message
    /* Omitted */

    // Configures the instance to be a result message
    /* Omitted */

    // Configures the instance to be a request message
    public Msg configureAsRequestMessage () {

        // Set the message type
        this.msgType = MsgType.MSG_TYPE_REQUEST;

        // Return instance
        return this;
    }

    /* ********** Message Deserialization Methods ********** */


    // Configures the instance using a received message buffer
    public Msg fromByteBuffer (byte[] buffer) throws MessageSerializationException {
        int offset = 3;
        MsgType type = MsgType.MSG_TYPE_MAX;

        // Check the message size
        if (buffer.length < 3) {
            throw new MessageSerializationException("Buffer doesn't meet minimal message size requirements");
        }

        // Check the message head markers
        if (buffer[0] != MSG_BYTE_HEAD || buffer[1] != MSG_BYTE_HEAD) {
            throw new MessageSerializationException("Buffer doesn't begin with message head markers");
        }

        // Check the message type
        boolean isValidMessageType = false;
        for (MsgType t : MsgType.values()) {
            if (t.getByteValue() == buffer[2]) {
                isValidMessageType = true;
                break;
            }
        }
        if (!isValidMessageType) {
            throw new MessageSerializationException("Message type is not valid");
        } else {
            type = MsgType.fromByte(buffer[2]);
        }

        // Check if the remaining buffer is sufficient to decode message of type
        if (buffer.length - offset < g_msg_size_tab[(int)type.getByteValue()]) {
            throw new MessageSerializationException("Buffer not large enough to decode message of given type");
        }

        // Switch on the message type
        switch (type) {

            // Decoding status message
            case MSG_TYPE_STATUS: {

                // Deserialize fields
                this.status_mode             = buffer[offset++];
                this.status_zone             = buffer[offset++];
                this.status_rate             = buffer[offset++];
                this.status_progress         = buffer[offset++];

            }
            break;

            // Decoding result message
            case MSG_TYPE_RESULT: {

                // Deserialize fields
                this.result_id               = buffer[offset++];
                this.result_progress_zone_ll = buffer[offset++];
                this.result_rate_zone_ll     = buffer[offset++];
                this.result_progress_zone_lr = buffer[offset++];
                this.result_rate_zone_lr     = buffer[offset++];
                this.result_progress_zone_tl = buffer[offset++];
                this.result_rate_zone_tl     = buffer[offset++];
                this.result_progress_zone_tr = buffer[offset++];
                this.result_rate_zone_tr     = buffer[offset++];

            }
            break;

            // Unreachable case
            default:
                throw new MessageSerializationException("Unexpected message type");
        }

        // Update internal message type
        this.msgType = type;

        // Return instance
        return this;
    }

    // TODO: Add more constructors here for different message types


    /* ********** Message Serialization Methods ********** */

    // Serializes the message for transmission
    public MsgData getSerialized () throws MessageSerializationException {
        byte[] buffer = new byte[MSG_BUFFER_MAX];
        int offset = 0;

        // Set the message header
        buffer[0] = buffer[1] = MSG_BYTE_HEAD;

        // Set the message type
        buffer[2] = this.msgType.getByteValue();

        // Update offset
        offset = 3;

        switch (this.msgType) {

            // Serializing a status message (doesn't apply)
            case MSG_TYPE_STATUS: {
                throw new MessageSerializationException("Cannot serialize this message!");
            }

            // Serializing a result message (doesn't apply)
            case MSG_TYPE_RESULT: {
                throw new MessageSerializationException("Cannot serialize unknown type!");
            }

            // Serializing a request message (applies)
            case MSG_TYPE_REQUEST: {
                // No payload!
            }
            break;

            default:
                throw new MessageSerializationException("Cannot serialize unknown type!");
        }

        // Trim the outgoing data buffer
        byte[] outgoing_buffer = new byte[offset];
        System.arraycopy(buffer, 0, outgoing_buffer, 0, offset);

        return new MsgData(offset, outgoing_buffer);
    }

    // Returns the status message
    public StatusMessage getStatusMessage () {
        return new StatusMessage(this.status_mode, this.status_zone,
                this.status_rate, this.status_progress);
    }

    // Returns new result message
    public ResultMessage getResultMessage () {
        return new ResultMessage(this.result_id, this.result_progress_zone_ll,
                this.result_rate_zone_ll, this.result_progress_zone_lr,
                this.result_rate_zone_lr, this.result_progress_zone_tl,
                this.result_rate_zone_tl, this.result_progress_zone_tr,
                this.result_rate_zone_tr);
    }

    // Returns message type. If MSG_TYPE_MAX then it was unable to successfully parse
    public MsgType getMessageType () {
        return this.msgType;
    }

    // Returns internal error description
    public String getErrorDescription () {
        return this.errorDescription;
    }

}
