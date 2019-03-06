package com.example.shaf.sgsports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CreateEventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryListAdapter categoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        categoryListAdapter = new CategoryListAdapter(this);

        recyclerView = findViewById(R.id.select_category_recycler_view);
        recyclerView.setAdapter(categoryListAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemCustomListener(this,
                recyclerView, new RecyclerItemCustomListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO: switch fragment
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));


    }

}
