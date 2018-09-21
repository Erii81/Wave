package byr.wave;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.quietmodem.Quiet.FrameTransmitter;
import org.quietmodem.Quiet.FrameTransmitterConfig;
import org.quietmodem.Quiet.ModemException;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sendFra#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sendFra extends Fragment {

    private FrameTransmitter transmitter;
    private EditText sendMessage;
    private Spinner profileSpinner;
    private ArrayAdapter<String> spinnerArrayAdapter;

    public sendFra() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static sendFra newInstance(String param1, String param2) {
        sendFra fragment = new sendFra();
        Bundle args = new Bundle();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send, container, false);
    }

    @Override
    public void onStart(){
        getView().findViewById(R.id.transmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSendClick();
            }
        });

        sendMessage = (EditText) getView().findViewById(R.id.message);
        profileSpinner = (Spinner) getView().findViewById(R.id.profile);
        setupProfileSpinner();
        setupTransmitter();
        handleDataFromIntent();
        super.onStart();
    }

    private void setupTransmitter() {
        FrameTransmitterConfig transmitterConfig;
        try {
            transmitterConfig = new FrameTransmitterConfig(
                    getActivity(),getProfile());
            transmitter = new FrameTransmitter(transmitterConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ModemException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleSendClick() {
        if (transmitter == null) {
            setupTransmitter();
        }
        send();
    }

    private void send() {
        String payload = sendMessage.getText().toString();
        try {
            transmitter.send(payload.getBytes());
        } catch (IOException e) {
            // our message might be too long or the transmit queue full
        }
    }

    private void setupProfileSpinner() {
        spinnerArrayAdapter = ProfilesHelper.createArrayAdapter(getActivity());
        profileSpinner.setAdapter(spinnerArrayAdapter);
        profileSpinner.setSelection(0, false);
        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transmitter = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private String getProfile() {
        String profile = spinnerArrayAdapter.getItem(profileSpinner.getSelectedItemPosition());
        return profile;
    }

    private void handleDataFromIntent() {

        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                sendMessage.setText(sharedText);
            }
        }

    }
}
