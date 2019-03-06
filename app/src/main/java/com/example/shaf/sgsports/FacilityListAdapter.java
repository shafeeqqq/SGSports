package com.example.shaf.sgsports;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.shaf.sgsports.Model.Facility;

import java.util.List;

public class FacilityListAdapter extends RecyclerView.Adapter<FacilityListAdapter.FacilityListViewHolder> {


    private Context mContext;

    private List<Facility> mFacilities;
    private String[] mCategories;


    public FacilityListAdapter(Context context) {
        mContext = context;
        mCategories = context.getResources().getStringArray(R.array.sports_categories);

    }


    @NonNull
    @Override
    public FacilityListAdapter.FacilityListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_facilities, parent, false);


        return new FacilityListAdapter.FacilityListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityListAdapter.FacilityListViewHolder holder, int position) {
//        if (mFacilitys != null) {
//            Facility current = mFacilitys.get(position);
//
//            // TODO: facility object binding
//
//        }
//
//        else {
//            Toast.makeText(mContext, "Error displaying data", Toast.LENGTH_LONG).show();
//        }
        if (mCategories != null) {
            String current = mCategories[position];
            holder.nameTextView.setText(current);
        }
    }


    @Override
    public int getItemCount() {
        // TODO: change this to event later
        if (mCategories != null)
            return mCategories.length;
        return 0;
    }

    public class FacilityListViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView addressTextView;
        private ImageView imageView;

        public FacilityListViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.facilities_name_text);
            addressTextView = itemView.findViewById(R.id.facilities_address_text);
            imageView = itemView.findViewById(R.id.facilities_image_view);

        }
    }
}
