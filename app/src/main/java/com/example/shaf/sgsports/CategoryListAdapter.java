package com.example.shaf.sgsports;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder> {


    private Context mContext;

    private String[] mCategories;


    public CategoryListAdapter(Context context) {
        mContext = context;
        mCategories = context.getResources().getStringArray(R.array.sports_categories);
    }


    @NonNull
    @Override
    public CategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_category, parent, false);


        return new CategoryListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListViewHolder holder, int position) {
        if (mCategories != null) {
            String current = mCategories[position];
            holder.textView.setText(current);

        } else {
            Toast.makeText(mContext, "Error displaying data", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public int getItemCount() {
        if (mCategories != null)
            return mCategories.length;
        return 0;
    }

    public class CategoryListViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        public CategoryListViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.list_text);
            imageView = itemView.findViewById(R.id.list_icon);

        }
    }
}
