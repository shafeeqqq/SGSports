package com.example.shaf.sgsports;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.Model.SkillLevel;
import com.example.shaf.sgsports.Model.UserInEvents;
import com.example.shaf.sgsports.Utils.DatePickerFragment;
import com.example.shaf.sgsports.Utils.TimePickerFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateEventDetailsFragment extends Fragment {

    public static final String UNKNOWN = "404";

    public static final String ARG_CATEGORY = UNKNOWN;
    public static final String ARG_LOCATION = UNKNOWN;
    public static final String LOGIN_PREFS = "LoginInfo";
    private static final String USER_ACCT_ID = "emailAddress" ;

    FirebaseDatabase database;
    SharedPreferences sharedPref;

    private String mCategory;
    private SkillLevel skillLevel;
    private int numPlayers;
    private String mLocation;
    private String mUnixTime;

    private TextView categoryTextView;
    private TextView fromTimeTextView;
    private TextView toTimeTextView;
    private TextView dateTextView;

    private EditText eventNameEditText;
    private EditText numPlayersEditText;
    private Spinner skillLevelSpinner;
    private EditText descEditText;

    private Button createButton;

    public CreateEventDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getString(ARG_CATEGORY);
            mLocation = getArguments().getString(ARG_LOCATION);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_event_details, container, false);

        database = FirebaseDatabase.getInstance();

        eventNameEditText = view.findViewById(R.id.create_event_details_name);
        categoryTextView = view.findViewById(R.id.create_event_details_cat_text);
        categoryTextView.setText(mCategory);

        numPlayersEditText = view.findViewById(R.id.create_event_details_num_text);
        descEditText = view.findViewById(R.id.create_event_details_desc);

        dateTextView = view.findViewById(R.id.create_event_details_date);
        dateTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                return false;
            }
        });

        fromTimeTextView = view.findViewById(R.id.create_event_details_from);
        fromTimeTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DialogFragment newFragment = new TimePickerFragment("from");
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
                return false;
            }
        });

        toTimeTextView = view.findViewById(R.id.create_event_details_to);
        toTimeTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DialogFragment newFragment = new TimePickerFragment("to");
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
                return false;
            }
        });

        createButton = view.findViewById(R.id.create_event_details_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                getActivity().finish();
                Toast.makeText(view.getContext(), "Event Created", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    private void saveData() {

        DatabaseReference myRef = database.getReference("events");

        Date now = new Date();
        sharedPref = getActivity().getSharedPreferences(LOGIN_PREFS, getActivity().MODE_PRIVATE);
        String userId = sharedPref.getString(USER_ACCT_ID, UNKNOWN);

        String id = myRef.push().getKey();
        String eventName = eventNameEditText.getText().toString();
        Integer maxPlayers = Integer.getInteger(numPlayersEditText.getText().toString());
        String eventDesc = descEditText.getText().toString();

        String dateText = dateTextView.getText().toString();

        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM, yyyy");
        Date date = null;
        try {
            date = getDate.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String from = fromTimeTextView.getText().toString();
        String to = toTimeTextView.getText().toString();

        String sportCategory = categoryTextView.getText().toString();

        if (maxPlayers==null) maxPlayers = 4;


        Event event = new Event(id, eventName,
                userId,
                now, date, from, to, sportCategory,
                maxPlayers, eventDesc, SkillLevel.Advanced.toString());

        myRef.child(id).setValue(event);

        saveUserInEvent(userId, event);

    }

    private void saveUserInEvent(String userId, Event event) {
        DatabaseReference myRef = database.getReference("userInEvents/" + userId);
        myRef.child(event.getEventID()).setValue(event);
    }


}
