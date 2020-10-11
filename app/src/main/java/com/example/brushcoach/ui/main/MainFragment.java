package com.example.brushcoach.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.brushcoach.MainActivity;
import com.example.brushcoach.R;

import java.util.ArrayList;

public class MainFragment extends Fragment implements View.OnClickListener, DeviceBluetoothInterface {

    private MainViewModel mViewModel;

    // Bluetooth device manager class
    private DeviceBluetoothManager bluetoothManager;

    // Metadata (make boolean an object so it can be linked to a button by reference)
    private Boolean connected;

    // Metadata (is the popup showing for help/setup)
    private Boolean isShowingSetup;

    // ImageViews
    private AnimateView image_teeth_top_right;
    private AnimateView image_teeth_top_left;
    private AnimateView image_teeth_low_right;
    private AnimateView image_teeth_low_left;

    // Progress
    private Measure measure_progress_general;

    // Buttons
    private xButton button_toggle_bluetooth;
    private xButton button_show_setup;

    // Spinners
    private Spinner spinner_load_save;

    // Measures
    private Measure measure_top_left;
    private Measure measure_top_right;
    private Measure measure_low_left;
    private Measure measure_low_right;

    // Animates views (tracking array)
    private ArrayList<AnimateView> animateViews;

    // Measures (tracking array)
    private ArrayList<Measure> measures;

    // Buttons (tracking array)
    private ArrayList<xButton> buttons;

    // Progress dialog
    private LoadingDialog loadingDialog;





    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.main_fragment, container, false);

        // Set metadata
        connected = new Boolean(false);
        isShowingSetup = new Boolean(false);

        // Set all views
        this.image_teeth_top_right = new AnimateView(v.findViewById(R.id.image_teeth_top_right), false);
        this.image_teeth_top_left  = new AnimateView(v.findViewById(R.id.image_teeth_top_left), false);
        this.image_teeth_low_right = new AnimateView(v.findViewById(R.id.image_teeth_low_right), false);
        this.image_teeth_low_left  = new AnimateView(v.findViewById(R.id.image_teeth_low_left), false);

        this.measure_progress_general = new Measure((ProgressBar)v.findViewById(R.id.progress_bar_general),
                (TextView)v.findViewById(R.id.progress_text_general));

        this.button_toggle_bluetooth = new xButton((Button)v.findViewById(R.id.button_toggle_bluetooth),
                this.connected);
        this.button_show_setup       = new xButton((Button)v.findViewById(R.id.button_toggle_setup), isShowingSetup);

        this.spinner_load_save       = v.findViewById(R.id.spinner_load_save);

        this.measure_top_left = new Measure((ProgressBar)v.findViewById(R.id.progress_bar_top_left),
                (TextView)v.findViewById(R.id.progress_text_top_left));
        this.measure_top_right = new Measure((ProgressBar)v.findViewById(R.id.progress_bar_top_right),
                (TextView)v.findViewById(R.id.progress_text_top_right));
        this.measure_low_left = new Measure((ProgressBar)v.findViewById(R.id.progress_bar_low_left),
                (TextView)v.findViewById(R.id.progress_text_low_left));
        this.measure_low_right = new Measure((ProgressBar)v.findViewById(R.id.progress_bar_low_right),
                (TextView)v.findViewById(R.id.progress_text_low_right));

        this.measures = new ArrayList<Measure>() {{
            add(MainFragment.this.measure_progress_general);
            add(MainFragment.this.measure_top_left);
            add(MainFragment.this.measure_top_right);
            add(MainFragment.this.measure_low_left);
            add(MainFragment.this.measure_low_right);
        }};

        this.buttons = new ArrayList<xButton>() {{
            add(MainFragment.this.button_toggle_bluetooth);
            add(MainFragment.this.button_show_setup);
        }};

        this.animateViews = new ArrayList<AnimateView>() {{
            add(MainFragment.this.image_teeth_low_left);
            add(MainFragment.this.image_teeth_low_right);
            add(MainFragment.this.image_teeth_top_left);
            add(MainFragment.this.image_teeth_top_right);

        }};

        // Setup the buttons
        this.button_toggle_bluetooth.getButton().setOnClickListener(this);
        this.button_show_setup.getButton().setOnClickListener(this);

        // Setup the spinner
        // TODO


        // Initialize the bluetooth interface
        this.bluetoothManager = new DeviceBluetoothManager(getContext(), this);

        // Initialize the loading dialog popup class
        loadingDialog = new LoadingDialog(this.getActivity());

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button_toggle_bluetooth:
                Log.i("onClick", "button_toggle_bluetooth");
                int a = (int)(Math.random()*100);
                int b = (int)(Math.random()*100);
                int c = (int)(Math.random()*100);
                int d = (int)(Math.random()*100);
                int e = (int)(Math.random()*100);
                measure_low_right.setValue(a);
                measure_low_left.setValue(b);
                measure_top_left.setValue(c);
                measure_top_right.setValue(d);
                measure_progress_general.setValue(e);
                button_toggle_bluetooth.setIsActive((e % 2) == 0);

                // Connect if disconnected, and vice versa
                if (this.connected == false) {
                    try {
                        bluetoothManager.scanForDevice("ESP-Tbrush");
                        MainFragment.this.loadingDialog.startLoadingDialog();
                    } catch (DeviceBluetoothException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    bluetoothManager.disconnectDevice();
                }

                break;
            case R.id.button_toggle_setup:
                Log.i("onClick", "button_toggle_setup");

                // Create a message to send (request for data)
                Msg m = new Msg().configureAsRequestMessage();

                try {

                    // Serialize the message
                    Msg.MsgData data = m.getSerialized();

                    // Try to send it
                    bluetoothManager.enqueueMessageBuffer(data.data);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                break;
            default:
                Log.i("onClick", "default");
                break;
        }

        // Refresh
        refresh(false);
    }

    public void animateView (View view) {
        ImageView v = (ImageView)view;

        Drawable d = v.getDrawable();

        if (d instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat)d;
            avd.start();
        } else {
            if (d instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable avd = (AnimatedVectorDrawable)d;
                avd.start();
            }
        }
    }

    public void refresh (final boolean dismissLoadingDialog) {


        // Locate what structures to update
        final ArrayList<Measure> measuresToUpdate = new ArrayList<>();
        for (Measure m : this.measures) {
            if (m.needsUpdate()) {
                measuresToUpdate.add(m);
            }
        }

        // Locate what buttons to update
        final ArrayList<xButton> buttonsToUpdate = new ArrayList<>();
        buttonsToUpdate.add(this.button_toggle_bluetooth);


        // Locate what animate views to update
        final ArrayList<AnimateView> animateViewsToUpdate = new ArrayList<>();
        for (AnimateView v : this.animateViews) {
            if (v.shouldAnimate()) {
                animateViewsToUpdate.add(v);
                v.setShouldAnimate(false);
            }
        }

        getActivity().runOnUiThread(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {

                // Update toggle button
                for (xButton b : buttonsToUpdate) {
                    if (b.getIsActive() == true) {
                        b.getButton().setBackgroundResource(R.drawable.circular_button_highlight);
                    } else {
                        b.getButton().setBackgroundResource(R.drawable.circular_button);
                    }
                }

                // Update progress bars
                for (Measure m : measuresToUpdate) {
                    m.getProgressBar().setProgress(m.getValue(), true);
                    m.getTextView().setText(String.format("%d%%", m.getValue()));
                    m.setNeedsUpdate(false);
                }

                // Run any animations
                for (AnimateView v : animateViewsToUpdate) {
                    MainFragment.this.animateView(v.getView());
                }

                // Dismiss any active dialogs
                if (dismissLoadingDialog) {
                    MainFragment.this.loadingDialog.dismissDialog();
                }
            }
        });
    }

    @Override
    public void onDeviceLocated(boolean didSucceed) {
        Log.i("onDeviceLocated", "Outcome = " + didSucceed);
        if (didSucceed) {
            try {
                MainFragment.this.bluetoothManager.connectDevice(getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            refresh(true);
        }

    }

    @Override
    public void onBluetoothConnect(boolean didSucceed) {

        // Log message
        Log.i("onBluetoothConnect", "Outcome = " + didSucceed);

        // Reset everything in preparation for brush mode
        if (didSucceed) {
            MainFragment.this.measure_progress_general.setValue(0);
            this.measure_low_left.setValue(0);
            this.measure_low_right.setValue(0);
            this.measure_top_left.setValue(0);
            this.measure_top_right.setValue(0);
        }

        // Update the user interface
        this.button_toggle_bluetooth.setIsActive((this.connected = didSucceed));
        refresh(true);
    }

    @Override
    public void onCharacteristicChanged(byte[] value) {
        Log.i("onCharacteristicChanged", "Received " + value.length + " bytes of data!");

        try {

            // Create message
            Msg m = new Msg().fromByteBuffer(value);

            // Act on type
            switch (m.getMessageType()) {
                case MSG_TYPE_STATUS: {
                    StatusMessage statusMessage = m.getStatusMessage();
                    AnimateView v = null;
                    // Update general progress indicator flash animation for section
                    MainFragment.this.measure_progress_general.setValue(statusMessage.getProgress());
                    switch (statusMessage.getZone()) {
                        case BRUSH_ZONE_LL: v = image_teeth_low_left;  break;
                        case BRUSH_ZONE_LR: v = image_teeth_low_right; break;
                        case BRUSH_ZONE_TL: v = image_teeth_top_left;  break;
                        case BRUSH_ZONE_TR: v = image_teeth_top_right; break;
                        default:                                       break;
                    }
                    if (v != null) {
                        v.setShouldAnimate(true);
                    }
                }
                break;
                case MSG_TYPE_RESULT: {
                    ResultMessage resultMessage = m.getResultMessage();

                    // Update all the smaller progress thingies
                    this.measure_low_left.setValue(resultMessage.getProgress(ResultMessage.Zone.BRUSH_ZONE_LL));
                    this.measure_low_right.setValue(resultMessage.getProgress(ResultMessage.Zone.BRUSH_ZONE_LR));
                    this.measure_top_left.setValue(resultMessage.getProgress(ResultMessage.Zone.BRUSH_ZONE_TL));
                    this.measure_top_right.setValue(resultMessage.getProgress(ResultMessage.Zone.BRUSH_ZONE_TR));

                    Log.i("onCharacteristicChanged", "MSG_TYPE_RESULT (unimplemented)");
                }
                break;
                default:
                    Log.i("onCharacteristicChanged",
                            "Unhandled message type: " + m.getMessageType());
            }
        } catch (MessageSerializationException e) {
            Log.e("onCharacteristicChanged", "Couldn't deserialize message!");
        }

        refresh(false);
    }

    @Override
    public void onCharacteristicWrite(boolean didSucceed) {
        Log.i("onCharacteristicWrite", "Outcome of write = " + didSucceed);
    }
}
