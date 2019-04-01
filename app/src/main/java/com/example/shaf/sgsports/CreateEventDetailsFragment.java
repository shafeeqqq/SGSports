package com.example.shaf.sgsports;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.example.shaf.sgsports.Model.Facility;
import com.example.shaf.sgsports.Model.SkillLevel;
import com.example.shaf.sgsports.Utils.DatePickerFragment;
import com.example.shaf.sgsports.Utils.TimePickerFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.shaf.sgsports.ChooseFacilityActivity.FACILITY_ID;
import static com.example.shaf.sgsports.EventSearchFragment.EVENT_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateEventDetailsFragment extends Fragment {

    public static final String UNKNOWN = "404";
    public static final String TAG = "C_EVENT_DEETZ_FRAGMENT";


    public static final String ARG_CATEGORY = UNKNOWN;
    public static final String ARG_LOCATION = UNKNOWN;
    public static final String LOGIN_PREFS = "LoginInfo";
    private static final String USER_ACCT_ID = "emailAddress" ;
    private static final int CHOOSE_FACULTY = 600;
    public static final String FACILITY_NAME = "facility_name";

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
    String eventId;
    Event localEvent;

    final int[] checkedItem = {0};


    public CreateEventDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getString(ARG_CATEGORY);
            mLocation = getArguments().getString(ARG_LOCATION);
            eventId = getArguments().getString(EVENT_ID);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_FACULTY && data != null) {
            String facilityID = data.getStringExtra(FACILITY_ID);
            String facilityName = data.getStringExtra(FACILITY_NAME);
            Log.e(FACILITY_ID, facilityID + "  " + facilityName);
            locationText.setText(facilityName);
            getFacility(facilityID);
        }

    }

    private void getFacility(String facilityID) {
        db.collection("facility").document(facilityID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        facility = documentSnapshot.toObject(Facility.class);
                    }
                });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_event_details, container, false);

        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        eventNameEditText = view.findViewById(R.id.create_event_details_name);
        categoryTextView = view.findViewById(R.id.create_event_details_cat_text);
        categoryTextView.setText(mCategory);
        categoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryDialog();
            }
        });

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
                Integer maxPlayers = null;
                try {
                     maxPlayers = Integer.parseInt(numPlayersEditText.getText().toString().trim());
                } catch (java.lang.NumberFormatException e) {
                    Log.e(TAG, e.getMessage());
                }

                if (eventNameEditText.getText().toString().isEmpty())
                    Toast.makeText(getContext(), "Please enter a Name.", Toast.LENGTH_LONG).show();
                else if (locationText.getText().toString().equals("Choose location"))
                    Toast.makeText(getContext(), "Please choose a location.", Toast.LENGTH_LONG).show();
                else if (maxPlayers ==null || maxPlayers < 1)
                    Toast.makeText(getContext(), "Maximum no. of players has to be atleast 1.", Toast.LENGTH_LONG).show();
                else {
                    saveData();
                    getActivity().finish();
                    Toast.makeText(view.getContext(), "Event Created", Toast.LENGTH_LONG).show();
                }
            }
        });

        skillLevelSpinner = view.findViewById(R.id.create_event_details_skill);
        locationText = view.findViewById(R.id.create_event_details_location);
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ChooseFacilityActivity.class);
                startActivityForResult(intent, CHOOSE_FACULTY);
            }
        });

        if (eventId != null && !eventId.isEmpty()) {
            loadEvent();
        }


        return view;
    }

    private void openCategoryDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final List<String> categoryArray = Arrays.asList(getResources().getStringArray(R.array.sports_categories));
        int index = categoryArray.indexOf(mCategory);

        builder.setSingleChoiceItems(R.array.sports_categories, index, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkedItem[0] = which;
                }
            });

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String gender = categoryArray.get(checkedItem[0]);
                    categoryTextView.setText(gender);
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

    }

    private void loadEvent() {
        final List<String> skillArray = Arrays.asList(getResources().getStringArray(R.array.skill_level));
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Event currentEvent = documentSnapshot.toObject(Event.class);

                        if (currentEvent != null) {
                            localEvent = currentEvent;
                            eventNameEditText.setText(currentEvent.getName());
                            categoryTextView.setText(currentEvent.getSportsCategory());
                            mCategory = currentEvent.getSportsCategory();
                            skillLevelSpinner.setSelection(skillArray.indexOf(currentEvent.getSkillLevel()));
                            dateTextView.setText(currentEvent.dateCreatedText());
                            fromTimeTextView.setText(currentEvent.getFromTime());
                            toTimeTextView.setText(currentEvent.getToTime());
                            locationText.setText(currentEvent.getAddress());

                            numPlayersEditText.setText(String.valueOf(currentEvent.getMaxParticipants()));

                            String descText = currentEvent.getDescription();
                            if (descText.equals(""))
                                descEditText.setHint("Add Description");
                            else
                                descEditText.setText(currentEvent.getDescription());

                            createButton.setText("update");
                            createButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Integer maxPlayers = Integer.parseInt(numPlayersEditText.getText().toString().trim());

                                    if (eventNameEditText.getText().toString().isEmpty())
                                        Toast.makeText(getContext(), "Please enter a Name.", Toast.LENGTH_LONG).show();
                                    else if (locationText.getText().toString().equals("Choose location"))
                                        Toast.makeText(getContext(), "Please choose a location.", Toast.LENGTH_LONG).show();
                                    else if (maxPlayers==null || maxPlayers < 1)
                                        Toast.makeText(getContext(), "Maximum no. of players has to be atleast 1.", Toast.LENGTH_LONG).show();
                                    else {
                                        updateEvent();
                                        getActivity().finish();
                                        Toast.makeText(getContext(), "Event Updated.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void saveData() {
        Date now = new Date();
        sharedPref = getActivity().getSharedPreferences(LOGIN_PREFS, getActivity().MODE_PRIVATE);

        String userId = sharedPref.getString(USER_ACCT_ID, UNKNOWN);

        String eventID = db.collection("events").document().getId();
        String eventName = eventNameEditText.getText().toString();
        Integer maxPlayers = null;
        try {
            maxPlayers = Integer.parseInt(numPlayersEditText.getText().toString().trim());
        } catch (java.lang.NumberFormatException e) {
            Log.e(TAG, e.getMessage());
        }
        String eventDesc = descEditText.getText().toString();

        String dateText = dateTextView.getText().toString();
        String skill = skillLevelSpinner.getSelectedItem().toString();

        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM, yyyy");
        Date date = null;
        try {
            date = getDate.parse(dateText);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }

        String from = fromTimeTextView.getText().toString();
        String to = toTimeTextView.getText().toString();

        String sportCategory = categoryTextView.getText().toString();

        if (maxPlayers==null) maxPlayers = 4;

        Event event = new Event(eventID, eventName, facility,
                userId,
                now, date, from, to, sportCategory,
                maxPlayers, eventDesc, skill);

        saveUserInEvent(userId, eventID);
        db.collection("events").document(eventID).set(event);

    }

    private void updateEvent() {

        String eventName = eventNameEditText.getText().toString();
        if (!eventName.isEmpty())
            localEvent.setName(eventName);

        Integer maxPlayers = null;
        try {
            maxPlayers = Integer.parseInt(numPlayersEditText.getText().toString().trim());
        } catch (java.lang.NumberFormatException e) {
            Log.e(TAG, e.getMessage());
        }
        if (maxPlayers != null)
            localEvent.setMaxParticipants(maxPlayers);

        String eventDesc = descEditText.getText().toString();
        localEvent.setDescription(eventDesc);

        String skill = skillLevelSpinner.getSelectedItem().toString();
        localEvent.setSkillLevel(skill);

        String dateText = dateTextView.getText().toString();
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM, yyyy");
        Date date;
        try {
            date = getDate.parse(dateText);
            localEvent.setDateOfEvent(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String from = fromTimeTextView.getText().toString();
        localEvent.setFromTime(from);

        String to = toTimeTextView.getText().toString();
        localEvent.setToTime(to);

        String sportCategory = categoryTextView.getText().toString();
        localEvent.setSportsCategory(sportCategory);

        db.collection("events").document(eventId).set(localEvent);

    }

    private void saveUserInEvent(String userId, String id) {
        DocumentReference ref = db.collection("userInEvents").document(userId);
        ref.update("created", FieldValue.arrayUnion(id));
        Log.e(TAG, "user in events updated");
    }


}
