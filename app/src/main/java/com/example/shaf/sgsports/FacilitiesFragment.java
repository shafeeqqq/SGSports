package com.example.shaf.sgsports;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class FacilitiesFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "FACILITIES_FRAGMENT";
    public static final String FACILITY_ID = "facility_id";

    ArrayList<Integer> selectedItems;
    FirebaseFirestore db;

    private EditText searchField;

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private FacilityListAdapter facilityListAdapter;
    private TextView categoryFilter;

    private OnFragmentInteractionListener mListener;
    private static ArrayList<Facility> facilityArrayList = new ArrayList<>();


    public FacilitiesFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static FacilitiesFragment newInstance(String param1, String param2) {
        FacilitiesFragment fragment = new FacilitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_facilities, container, false);
        final Context context = view.getContext();
        selectedItems = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        searchField = view.findViewById(R.id.facility_search_edittext);
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

        facilityListAdapter = new FacilityListAdapter(view.getContext());

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

        recyclerView = view.findViewById(R.id.facilities_recycler_view);
        recyclerView.setAdapter(facilityListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemCustomListener(view.getContext(),
                recyclerView, new RecyclerItemCustomListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), FacilityDetailsActivity.class);
                intent.putExtra(FACILITY_ID, facilityListAdapter.getFacilityID(position));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
        return view;
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

    public void openCategoryDialog(Context context) {

        final boolean[] checkedItems = new boolean[10];
        for (int i = 0; i < 9; i++) {
            if (selectedItems != null && selectedItems.contains(i))
                checkedItems[i] = true;
            else
                checkedItems[i] = false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMultiChoiceItems(R.array.sports_categories, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(which);
                        } else if (selectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(Integer.valueOf(which));
                        }
                    }
                });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              //
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
