package com.example.shaf.sgsports.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.Model.Request;
import com.example.shaf.sgsports.ProfileActivity;
import com.example.shaf.sgsports.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;
import static com.example.shaf.sgsports.HomeActivity.LOGIN_PREFS;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;

public class ManageRequestAdapter extends RecyclerView.Adapter<ManageRequestAdapter.EventListViewHolder> {


    public static final int ACCEPT = 1 ;
    public static final int DECLINE = 0 ;

    private Context mContext;
    private List<Request> mRequest;
    private String eventID;
    private Event mEvent;
    FirebaseFirestore db;

    public ManageRequestAdapter(Context context, String eventID) {
        mContext = context;
        this.eventID = eventID;
        db = FirebaseFirestore.getInstance();
        setCurrentEvent();
    }


    @NonNull
    @Override
    public ManageRequestAdapter.EventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_request, parent, false);

        return new ManageRequestAdapter.EventListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ManageRequestAdapter.EventListViewHolder holder, int position) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        String userID = sharedPref.getString(USER_ACCT_ID, UNKNOWN);

        final int pos = position;

        if (mRequest != null) {
            final Request current = mRequest.get(position);
            holder.nameTextView.setText(current.getRequesterName());

            holder.nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra(USER_ACCT_ID, current.getUserID());
                    mContext.startActivity(intent);
                }
            });

            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("requestAdapter","enter accept" );
                    if (mEvent.getVacancy() > 0) {
                        updateRequestStatus(current.getId(), current.getUserID(), ACCEPT, pos);
                        holder.acceptButton.setAlpha(.5f);
                        holder.acceptButton.setClickable(false);
                    }
                    else
                        Toast.makeText(mContext, "Event is Full!", Toast.LENGTH_SHORT).show();
                }
            });

            holder.declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("requestAdapter","enter decline" );

                    updateRequestStatus(current.getId(), current.getUserID(), DECLINE, pos);
                    holder.declineButton.setAlpha(.5f);
                    holder.declineButton.setClickable(false);
//                    Toast.makeText(mContext, "Processing...", Toast.LENGTH_LONG).show();

                }
            });

        } else {
            Toast.makeText(mContext, "Error displaying data", Toast.LENGTH_LONG).show();
        }
    }

    private void updateRequestStatus(final String requestID, String requesterID, final int flag, int position) {
        DocumentReference ref = db.collection("userInEvents").document(requesterID);
        Log.e("requestAdapter","enter update method" );

        // update userInEvents
        if (flag == ACCEPT) {
            ref.update("joined", FieldValue.arrayUnion(eventID));
            Toast.makeText(mContext, "Accepted request.", Toast.LENGTH_SHORT).show();
            Log.e("requestAdapter","enter accept flag" );

        } else if (flag == DECLINE) {
            Log.e("requestAdapter","enter accept flag" );

            ref.update("rejected", FieldValue.arrayUnion(eventID));
            Toast.makeText(mContext, "Declined request.", Toast.LENGTH_SHORT).show();

        }

        // updated Event
        db.collection("events").document(eventID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Event currentEvent = documentSnapshot.toObject(Event.class);
                assert currentEvent != null;
                currentEvent.updateRequestStatus(requestID, flag);
                updateEventDocument(currentEvent);
            }
        });

        mRequest.remove(position);
        notifyDataSetChanged();


    }

    private void updateEventDocument(Event event) {
        db.collection("events").document(event.getEventID())
                .update("requests", event.getRequests());
    }


    @Override
    public int getItemCount() {
        if (mRequest != null)
            return mRequest.size();
        return 0;
    }


    public void setRequests(List<Request> requests){
        mRequest = requests;
        notifyDataSetChanged();
        setCurrentEvent();
    }

    public void setCurrentEvent() {
        db.collection("events").document(eventID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Event currentEvent = documentSnapshot.toObject(Event.class);
                        assert currentEvent != null;
                        mEvent = currentEvent;
                    }
                });

    }

    public String getUserID(int position) {
        return mRequest.get(position).getUserID();
    }


    public class EventListViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private Button acceptButton;
        private Button declineButton;

        public EventListViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.req_name);
            acceptButton = itemView.findViewById(R.id.req_accept);
            declineButton = itemView.findViewById(R.id.req_decline);

        }
    }


}
