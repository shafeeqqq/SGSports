package com.example.shaf.sgsports;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateEventDetailsFragment extends Fragment {

    public static final String UNKNOWN = "404";

    public static final String ARG_CATEGORY = UNKNOWN;
    public static final String ARG_LOCATION = UNKNOWN;


    private String mCategory;
    private SkillLevel skillLevel;
    private int numPlayers;
    private String mLocation;
    private String mUnixTime;

    private TextView categoryTextView;
    private TextView fromTimeTextView;
    private TextView toTimeTextView;
    private TextView dateTextView;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event_details, container, false);

        categoryTextView = view.findViewById(R.id.create_event_details_cat_text);
        categoryTextView.setText(mCategory);

        createButton = view.findViewById(R.id.create_event_details_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });


        return view;
    }

    private void saveData() {

    }
}
