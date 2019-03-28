package com.example.shaf.sgsports;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.Model.Request;
import com.example.shaf.sgsports.Model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;
import static com.example.shaf.sgsports.HomeActivity.LOGIN_PREFS;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;

public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String EVENT_ID = "eventID";
    private static final String TAG = "EVENT_DETAILS_ACTIVITY" ;

    DatabaseReference ref = null;
    private ValueEventListener mEventDetailsListener;

    DatabaseReference requestRef;
    DatabaseReference userReqRef;
    FirebaseDatabase database;

    ArrayList<String> userRequestsarray = new ArrayList<>();

    private MapView mapView;
    private GoogleMap mMap;

    private TextView categoryTextView;
    private TextView skillLvlTextView;
    private TextView organiserNameTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView addressTextView;
    private TextView maxPlayersTextView;
    private TextView descTextView;

    private Button joinButton;

    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapview);
//        mapFragment.getMapAsync(this);

        eventID = getIntent().getStringExtra(EVENT_ID);
        assert eventID != null;
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("events").child(eventID);

        String path = "events/"+ eventID + "/requests";
        requestRef = database.getReference(path);

        categoryTextView = findViewById(R.id.event_details_category_text);
        skillLvlTextView = findViewById(R.id.event_details_skill_text);
        organiserNameTextView = findViewById(R.id.event_details_host_name);
        dateTextView = findViewById(R.id.event_details_date);
        timeTextView = findViewById(R.id.event_details_time);
        addressTextView = findViewById(R.id.event_details_address);
        maxPlayersTextView = findViewById(R.id.event_details_max_players);
        descTextView = findViewById(R.id.event_details_description);

        joinButton = findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    private void sendRequest() {
        SharedPreferences sharedPref = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        final String userId = sharedPref.getString(USER_ACCT_ID, UNKNOWN);

        requestExists(userId);
    }

    private void requestExists(final String userId) {
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean exists = false;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Request request = ds.getValue(Request.class);
                    if (request.getUserID().equals(userId))
                        exists = true;
                }

                if (exists)
                    notifyUser();

                else
                    storeRequest(userId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EventDetailsActivity.this, "Failed to load user.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notifyUser() {
        Toast.makeText(EventDetailsActivity.this, "Request Already Sent",
                Toast.LENGTH_SHORT).show();
    }

    private void storeRequest(String userId) {
        String id = requestRef.push().getKey();
        String requesterName = getOrganiserName(userId);

        Request request = new Request(id, userId, requesterName, new Date(), Request.PENDING);
        requestRef.child(id).setValue(request);

        userReqRef = database.getReference("userRequests/" + userId);

        userReqRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("events"))
                    loadIntoArray(dataSnapshot.child("events").getValue().toString());

                saveUserRequests();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void saveUserRequests() {
        userRequestsarray.add(eventID);
        userReqRef.child("events").setValue(userRequestsarray.toString());
        Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show();
    }

    private void loadIntoArray(String events) {
        events = events.replace("[", "");
        events = events.replace("]", "");

        List<String> arr = Arrays.asList(events.split(","));

        for (String item: arr)
            userRequestsarray.add(item.trim());
    }


    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener eventDetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Event currentEvent = dataSnapshot.getValue(Event.class);
                if (currentEvent != null) {
                    setTitle(currentEvent.getName());
                    categoryTextView.setText(currentEvent.getSportsCategory());
                    skillLvlTextView.setText(currentEvent.getSkillLevel());
                    organiserNameTextView.setText(getOrganiserName(currentEvent.getOrganiser()));
                    dateTextView.setText(currentEvent.dateCreatedText());
                    timeTextView.setText(currentEvent.timeText());

                    String numText = currentEvent.getMaxParticipants() + " Players";
                    maxPlayersTextView.setText(numText);

                    String descText = currentEvent.getDescription();
                    if (descText.equals(""))
                        descTextView.setText(getString(R.string.no_description_given));
                    else
                        descTextView.setText(currentEvent.getDescription());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

                Toast.makeText(EventDetailsActivity.this, "Failed to load event.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        ref.addValueEventListener(eventDetailsListener);
        // Keep copy of post listener so we can remove it when app stops
        mEventDetailsListener = eventDetailsListener;
    }

    private String getOrganiserName(String userId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userId);
        final List<String> param = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = dataSnapshot.getValue(User.class);
                if (user == null)
                    Log.e(TAG, "Failed to load user");
                else {
                    Log.e(TAG, user.getName());
                    setOrganiseName(user.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EventDetailsActivity.this, "Failed to load user.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Log.e(TAG, param.toString());
        if (param.isEmpty())
            return "";
        else
            return param.get(0);
    }

    private void setOrganiseName(String name) {
        organiserNameTextView.setText(name);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ntu = new LatLng(-1.3483, 103.6831);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ntu,4));
        mMap.addMarker(new MarkerOptions().position(ntu).title("NTU Singapore"));

//        UiSettings uiSettings = mMap.getUiSettings();
//        uiSettings.setCompassEnabled(true);
//        uiSettings.setZoomControlsEnabled(true);
//        uiSettings.setRotateGesturesEnabled(true);
//        uiSettings.setZoomGesturesEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ntu,4));
    }


    @Override
    public void onStop() {
        super.onStop();
        // Remove eventDetails value event listener
        if (mEventDetailsListener != null)
            ref.removeEventListener(mEventDetailsListener);

    }




}
