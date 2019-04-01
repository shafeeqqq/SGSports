package com.example.shaf.sgsports;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shaf.sgsports.Model.Facility;
import com.example.shaf.sgsports.Utils.FacilityListAdapter;
import com.example.shaf.sgsports.Utils.RecyclerItemCustomListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.shaf.sgsports.CreateEventDetailsFragment.FACILITY_NAME;
import static com.example.shaf.sgsports.FacilityDetailsActivity.CHOICE_CONFIRM;

public class ChooseFacilityActivity extends AppCompatActivity {

    private static final String TAG = ChooseFacilityActivity.class.getSimpleName();
    public static final String FACILITY_ID = "facility_id";
    public static final String CHOOSE = "choose_facility";

    private static final int FACILITY_DETAILS = 201;


    ArrayList<Integer> selectedItems;
    FirebaseFirestore db;

    private EditText searchField;

    private String[] chosenFacility = new String[2];

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private FacilityListAdapter facilityListAdapter;
    private TextView categoryFilter;

    private FacilitiesFragment.OnFragmentInteractionListener mListener;
    private static ArrayList<Facility> facilityArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_facilities);

        selectedItems = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        searchField = findViewById(R.id.choose_facility_search_edittext);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFilter(String.valueOf(s).toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        categoryFilter = view.findViewById(R.id.facilities_filter_cat);
//        categoryFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCategoryDialog(context);
//            }
//        });

        facilityListAdapter = new FacilityListAdapter(this);

        db.collection("facility").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshots.isEmpty())
                    Log.e(TAG, "No data");

                facilityArrayList.clear();
                for (QueryDocumentSnapshot doc : snapshots) {
                    Facility facility = doc.toObject(Facility.class);
                    facilityArrayList.add(facility);
                }
                Log.e(TAG, "FACILITY ARRAY SIZE: " + String.valueOf(facilityArrayList.size()));
                facilityListAdapter.setFacilities(facilityArrayList);
            }
        });

        recyclerView = findViewById(R.id.choose_facility_recycler_view);
        recyclerView.setAdapter(facilityListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemCustomListener(this,
                recyclerView, new RecyclerItemCustomListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), FacilityDetailsActivity.class);
                chosenFacility[0] = facilityListAdapter.getFacilityID(position);
                chosenFacility[1] = facilityListAdapter.getFacilityName(position);
                intent.putExtra(FACILITY_ID, chosenFacility[0]);
                intent.putExtra(CHOOSE, true);
                startActivityForResult(intent, FACILITY_DETAILS);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                chosenFacility[0] = facilityListAdapter.getFacilityID(position);
                chosenFacility[1] = facilityListAdapter.getFacilityName(position);
                showConfirmationDialog();
            }
        }));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FACILITY_DETAILS && data != null && resultCode== RESULT_OK) {
            boolean confirm = data.getBooleanExtra(CHOICE_CONFIRM, false);
            if (confirm)
                closeActivity();
        }
    }

    private void searchFilter(String s) {
        Log.e(TAG, "search filter enter" + s);
        ArrayList<Facility> arr = new ArrayList<>();

        if (s.isEmpty())
            facilityListAdapter.setFacilities(facilityArrayList);

        Log.e(TAG, facilityArrayList.size() + "<- main array");

        for (Facility facility: facilityArrayList) {
            Log.e(TAG, facility.getName() + s);
            if (facility.getName().toLowerCase().contains(s))
                arr.add(facility);
        }

        facilityListAdapter.setFacilities(arr);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm Location?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeActivity();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void closeActivity() {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(FACILITY_ID, chosenFacility[0]);
        replyIntent.putExtra(FACILITY_NAME, chosenFacility[1]);

        setResult(RESULT_OK, replyIntent);
        finish();
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
