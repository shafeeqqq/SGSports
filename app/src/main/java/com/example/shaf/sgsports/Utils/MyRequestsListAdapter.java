package com.example.shaf.sgsports.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;
import static com.example.shaf.sgsports.HomeActivity.LOGIN_PREFS;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;

public class MyRequestsListAdapter extends RecyclerView.Adapter<MyRequestsListAdapter.EventListViewHolder> {


    private Context mContext;
    private List<Event> mEvents;

    public MyRequestsListAdapter(Context context) {
        mContext = context;
    }


    @NonNull
    @Override
    public MyRequestsListAdapter.EventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_sent_requests, parent, false);

        return new MyRequestsListAdapter.EventListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequestsListAdapter.EventListViewHolder holder, int position) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        String userID = sharedPref.getString(USER_ACCT_ID, UNKNOWN);

        if (mEvents != null) {
            Event current = mEvents.get(position);

            holder.nameTextView.setText(current.getName());
            holder.catTextView.setText("| " + current.getSportsCategory());

            holder.dateTextView.setText(current.dateCreatedText());
            holder.timeTextView.setText(current.timeText());

            String status = current.getRequestStatus(userID) ;
            holder.statusTextView.setText(status);


        } else {
            Toast.makeText(mContext, "Error displaying data", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public int getItemCount() {
        if (mEvents != null)
            return mEvents.size();
        return 0;
    }


    public void setEvents(List<Event> events){
        mEvents = events;
        notifyDataSetChanged();

    }

    public String getEventID(int position) {
        return mEvents.get(position).getEventID();
    }


    public class EventListViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView catTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView statusTextView;

        public EventListViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.my_requests_name_text);
            catTextView = itemView.findViewById(R.id.my_requests_category_text);
            dateTextView = itemView.findViewById(R.id.my_requests_date_text);
            timeTextView = itemView.findViewById(R.id.my_requests_time_text);
            statusTextView = itemView.findViewById(R.id.my_requests_status);
        }
    }
}
