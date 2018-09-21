package byr.wave;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class recFra extends Fragment {

    private TextView receivedContent;
    private Spinner profileSpinner;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private TextView receiveStatus;
    private Subscription frameSubscription = Subscriptions.empty();
    private boolean subscribed = false;

    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    public recFra() {
        // Required empty public constructor
    }

    public static recFra newInstance(String param1, String param2) {
        recFra fragment = new recFra();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rec, container, false);
    }

    @Override
    public void onStart(){
        receivedContent = (TextView) getActivity().findViewById(R.id.received_content);
        profileSpinner = (Spinner) getActivity().findViewById(R.id.profile);
        receiveStatus = (TextView) getActivity().findViewById(R.id.receive_status);
        setupProfileSpinner();
        setupReceiver();
        subscribed = true;
        super.onStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden){

        if(hidden == true){
            subscribed = false;
            frameSubscription.unsubscribe();
        }
        else{
            setupReceiver();
            subscribed = true;
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    subscribeToFrames();
                } else {
                    showMissingAudioPermissionToast();
                }
            }
        }
    }


    private void setupReceiver() {
        if (hasRecordAudioPersmission()) {
            subscribeToFrames();
        } else {
            requestPermission();
        };
    }

    private void subscribeToFrames() {
        frameSubscription.unsubscribe();
        frameSubscription = FrameReceiverObservable.create(getActivity(), getProfile()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(buf -> {
            receivedContent.setText(new String(buf, Charset.forName("UTF-8")));
            Long time = System.currentTimeMillis() / 1000;
            String timestamp = time.toString();
            receiveStatus.setText("Received " + buf.length + " @" + timestamp);
        }, error-> {
            receiveStatus.setText("error " + error.toString());
        });
    }

    boolean hasRecordAudioPersmission() {
        return ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        /*
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO},
                MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        */
        this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
    }

    private void showMissingAudioPermissionToast() {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getActivity(), R.string.missing_audio_permission, duration);
        toast.show();
    }

    private void setupProfileSpinner() {
        spinnerArrayAdapter = ProfilesHelper.createArrayAdapter(getActivity());
        profileSpinner.setAdapter(spinnerArrayAdapter);
        profileSpinner.setSelection(0, false);
        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupReceiver();
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

}
