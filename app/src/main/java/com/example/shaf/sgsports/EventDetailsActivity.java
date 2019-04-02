package com.example.shaf.sgsports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.Model.Request;
import com.example.shaf.sgsports.Model.User;
import com.example.shaf.sgsports.Utils.ManageRequestAdapter;
import com.example.shaf.sgsports.Utils.ParticipantListAdapter;
import com.example.shaf.sgsports.Utils.RecyclerItemCustomListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;
import static com.example.shaf.sgsports.HomeActivity.LOGIN_PREFS;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ICON;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_NAME;

public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String EVENT_ID = "eventID";
    private static final String TAG = "EVENT_DETAILS_ACTIVITY" ;

    DatabaseReference ref = null;
    private ValueEventListener mEventDetailsListener;
    RecyclerView recyclerView;

    String requesterName = "";
    String requesterIcon = "";
    private Event localEvent;

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
    private ProgressBar progressBar;
    private Button joinButton;
    private LinearLayout container;
    private String eventID;
    FirebaseFirestore db;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        db = FirebaseFirestore.getInstance();
        container = findViewById(R.id.event_details_main_container);
        container.setVisibility(View.GONE);
        progressBar = findViewById(R.id.event_progress_bar);

        SharedPreferences sharedPref = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        userId = sharedPref.getString(USER_ACCT_ID, UNKNOWN);
        requesterName = sharedPref.getString(USER_ACCT_NAME, UNKNOWN);
        requesterIcon = sharedPref.getString(USER_ACCT_ICON, UNKNOWN);


        eventID = getIntent().getStringExtra(EVENT_ID);
        assert eventID != null;

        categoryTextView = findViewById(R.id.event_details_category_text);
        skillLvlTextView = findViewById(R.id.event_details_skill_text);
        organiserNameTextView = findViewById(R.id.event_details_host_name);
        dateTextView = findViewById(R.id.event_details_date);
        timeTextView = findViewById(R.id.event_details_time);
        addressTextView = findViewById(R.id.event_details_address);
        LinearLayout addressContainer = findViewById(R.id.event_det_address_container);
        maxPlayersTextView = findViewById(R.id.event_details_max_players);
        descTextView = findViewById(R.id.event_details_description);

        joinButton = findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processRequest(userId);
            }
        });

        ImageView chatIcon = findViewById(R.id.event_details_chat_icon);
        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey there! I am ...");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        recyclerView = findViewById(R.id.event_details_recycler);

        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        addressContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = String.valueOf(addressTextView.getText());
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

    }


    private void setOrganiserUI(ArrayList<Request> requests) {
        Log.e(TAG, "filter requests: " + requests.toString());

        updateRequests(requests);

        mapView.setVisibility(View.GONE);
        findViewById(R.id.event_details_host_info).setVisibility(View.GONE);
        joinButton.setVisibility(View.GONE);

        LinearLayout requestContainer = findViewById(R.id.event_details_requests);
        requestContainer.setVisibility(View.VISIBLE);

        Button editEventButton = findViewById(R.id.edit_event_button);
        editEventButton.setVisibility(View.VISIBLE);
        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailsActivity.this, EditEventActivity.class);
                intent.putExtra(EVENT_ID, eventID);
                startActivity(intent);
            }
        });

    }

    private void updateRequests(ArrayList<Request> requests) {
        TextView emptyText = findViewById(R.id.event_details_empty_requests);
        ManageRequestAdapter adapter = new ManageRequestAdapter(this, eventID);

        if (!requests.isEmpty()) {
            recyclerView.setAdapter(adapter);
            adapter.setRequests(requests);

        } else {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }


    private void processRequest(final String userId) {
        db.collection("userInEvents").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> f = (ArrayList<String>) documentSnapshot.get("requested");
                        Log.w(TAG, "--=" + f.toString());

                        if (f.contains(eventID))
                            notifyUser();
                        else
                            storeRequest(userId);
                    }
                });
    }

    private void notifyUser() {
        Toast.makeText(EventDetailsActivity.this, "Request Already Sent",
                Toast.LENGTH_LONG).show();
    }

    private void storeRequest(String userId) {
        String requestId = db.collection("events").getId();

        if (requesterName.isEmpty()) {
            try {
                wait(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Request request = new Request(requestId, userId, requesterName, requesterIcon, new Date(), Request.PENDING);
        DocumentReference ref = db.collection("events").document(eventID);
        ref.update("requests", FieldValue.arrayUnion(request));

        ref = db.collection("userInEvents").document(userId);
        ref.update("requested", FieldValue.arrayUnion(eventID));

        Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show();

        joinButton.setAlpha(.5f);
        joinButton.setClickable(false);
        joinButton.setText("REQUEST " + "PENDING");
    }



    @Override
    public void onStart() {
        super.onStart();

        db.collection("events").document(eventID).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    final Event currentEvent = documentSnapshot.toObject(Event.class);
                    if (currentEvent != null) {
                        if (currentEvent.getOrganiser().equals(userId)) {
                            setOrganiserUI(currentEvent.queryPendingRequests());
                            Log.e(TAG, currentEvent.getRequests().toString());
                        }

                        getOrganiserName(currentEvent.getOrganiser());
                        setTitle(currentEvent.getName());
                        categoryTextView.setText(currentEvent.getSportsCategory());
                        skillLvlTextView.setText(currentEvent.getSkillLevel());
                        dateTextView.setText(currentEvent.dateCreatedText());
                        timeTextView.setText(currentEvent.timeText());
                        addressTextView.setText(currentEvent.getAddress());
                        organiserNameTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(EventDetailsActivity.this, ProfileActivity.class);
                                intent.putExtra(USER_ACCT_ID, currentEvent.getOrganiser());
                                startActivity(intent);
                            }
                        });

                        String numText = currentEvent.getMaxParticipants() + " Players";
                        maxPlayersTextView.setText(numText);

                        if (currentEvent.getVacancy()==0){
                            joinButton.setAlpha(.5f);
                            joinButton.setClickable(false);
                            joinButton.setText("Event is Full ");
                        }

                        String descText = currentEvent.getDescription();
                        if (descText.equals(""))
                            descTextView.setText(getString(R.string.no_description_given));
                        else
                            descTextView.setText(currentEvent.getDescription());

                        String status = currentEvent.hasRequestFrom(userId);
                        if (!status.equals("NO")) {
                            joinButton.setAlpha(.5f);
                            joinButton.setClickable(false);
                            joinButton.setText("REQUEST " + status);
                        }

                        final ParticipantListAdapter adapter = new ParticipantListAdapter(EventDetailsActivity.this);
                        adapter.setParticipants(currentEvent.getRequests());
                        RecyclerView partcipantsList = findViewById(R.id.event_detail_participants);
                        partcipantsList.addOnItemTouchListener(new RecyclerItemCustomListener(EventDetailsActivity.this,
                                partcipantsList, new RecyclerItemCustomListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(EventDetailsActivity.this, ProfileActivity.class);
                                intent.putExtra(USER_ACCT_ID, adapter.getItem(position));
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                            }
                        }));
                        partcipantsList.setAdapter(adapter);
                    }


                }
            });

    }

    private void getOrganiserName(String userId) {

        db.collection("users").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                assert user != null;
                setOrganiserName(user.getName());
            }
        });

    }

    private void setOrganiserName(String name) {
        organiserNameTextView.setText(name);
        progressBar.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ntu = new LatLng(-1.3483, 103.6831);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ntu,4));
        mMap.addMarker(new MarkerOptions().position(ntu).title("NTU Singapore"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ntu,4));
    }


    @Override
    public void onStop() {
        super.onStop();
        // Remove eventDetails value event listener
        if (mEventDetailsListener != null)
            ref.removeEventListener(mEventDetailsListener);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }


}
