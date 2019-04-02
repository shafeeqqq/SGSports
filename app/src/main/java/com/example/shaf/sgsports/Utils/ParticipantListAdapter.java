package com.example.shaf.sgsports.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaf.sgsports.Model.Request;
import com.example.shaf.sgsports.R;

import java.util.List;

public class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.CategoryListViewHolder> {

    private Context mContext;
    private List<Request> mParticipants;
    private String[] mUrls;



    public ParticipantListAdapter(Context context) {
        mContext = context;
    }


    @NonNull
    @Override
    public CategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_participant, parent, false);

        return new CategoryListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListViewHolder holder, int position) {
        if (mParticipants != null) {
            Request current = mParticipants.get(position);

            holder.textView.setText(current.getRequesterName());
        } else {
            Toast.makeText(mContext, "Error displaying data", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public int getItemCount() {
        if (mParticipants != null)
            return mParticipants.size();
        return 0;
    }

    public String getItem(int position) {
        return mParticipants.get(position).getUserID();
    }


    public void setParticipants(List<Request> requests){
        for (Request req: requests) {
            if (!req.isAccepted())
                requests.remove(req);
        }
        mParticipants = requests;
        notifyDataSetChanged();

    }


    public class CategoryListViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public CategoryListViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.label);

        }
    }
}
