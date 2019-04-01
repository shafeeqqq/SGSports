package com.example.shaf.sgsports;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shaf.sgsports.Model.Facility;
import com.example.shaf.sgsports.Model.SkillLevel;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.shaf.sgsports.EventSearchFragment.EVENT_ID;

public class EditEventActivity extends AppCompatActivity {


    FirebaseDatabase database;
    SharedPreferences sharedPref;

    private String mCategory;
    private SkillLevel skillLevel;
    private int numPlayers;
    private String mLocation;
    private String mUnixTime;
    private Facility facility;

    private TextView categoryTextView;
    private TextView fromTimeTextView;
    private TextView toTimeTextView;
    private TextView dateTextView;
    private EditText eventNameEditText;
    private EditText numPlayersEditText;
    private Spinner skillLevelSpinner;
    private EditText descEditText;
    private TextView locationText;

    private Button createButton;
    FirebaseFirestore db;


    final FragmentManager fm = getSupportFragmentManager();
    final Fragment editEventFragment = new CreateEventDetailsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_event_details);

        String eventId = getIntent().getStringExtra(EVENT_ID);

        Bundle args = new Bundle();
        args.putString(EVENT_ID, eventId);
        editEventFragment.setArguments(args);

        fm.beginTransaction().add(R.id.home_container, editEventFragment).commit();


    }
}
