package com.example.shaf.sgsports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.Utils.MyRequestsListAdapter;
import com.example.shaf.sgsports.Utils.RecyclerItemCustomListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;
import static com.example.shaf.sgsports.EventSearchFragment.EVENT_ID;
import static com.example.shaf.sgsports.HomeActivity.LOGIN_PREFS;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;

public class MyRequestsActivity extends AppCompatActivity {

    private static final String TAG = "MYREQUESTS_ACTIVITY";
    private static ArrayList<Event> myRequestsArrayList = new ArrayList<Event>();
    SharedPreferences sharedPref;
    FirebaseFirestore db;

    private RecyclerView recyclerView;
    private MyRequestsListAdapter myRequestsListAdapter;

    private TextView emptyMsgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        myRequestsListAdapter = new MyRequestsListAdapter(this);
        emptyMsgText = findViewById(R.id.my_requests_empty_text);
        emptyMsgText.setVisibility(View.GONE);

        sharedPref = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        String userId = sharedPref.getString(USER_ACCT_ID, UNKNOWN);
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.my_requests_recycler);
        recyclerView.setAdapter(myRequestsListAdapter);

        db.collection("userInEvents").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> f = (ArrayList<String>) documentSnapshot.get("requested");
                        Log.w(TAG, "=>" + f.toString());

                        if (f.isEmpty())
                            showEmptyMessage(true);

                        else {
                            showEmptyMessage(false);
                            loadEvents(f);
                        }
                    }
                });

        recyclerView.addOnItemTouchListener(new RecyclerItemCustomListener(this,
                recyclerView, new RecyclerItemCustomListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), EventDetailsActivity.class);
                intent.putExtra(EVENT_ID, myRequestsListAdapter.getEventID(position));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    private void loadEvents(final ArrayList<String> list) {

        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshots.isEmpty())
                    Log.e(TAG, "No data");

                myRequestsArrayList.clear();
                for (QueryDocumentSnapshot doc : snapshots) {
                    Event event = doc.toObject(Event.class);
                    if (list.contains(event.getEventID()))
                        myRequestsArrayList.add(event);
                }
                Log.e(TAG, "num requested events: " + myRequestsArrayList.size());
                myRequestsListAdapter.setEvents(myRequestsArrayList);
            }
        });
    }


    private void showEmptyMessage(boolean show) {
        if (show)
            emptyMsgText.setVisibility(View.VISIBLE);
        else
            emptyMsgText.setVisibility(View.GONE);

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
