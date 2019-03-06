package com.example.shaf.sgsports;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FacilityDetailsActivity extends AppCompatActivity {

    private TextView mapIntentTextView;
    private TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_details);

        mapIntentTextView = findViewById(R.id.facility_details_map_intent_text);
        addressTextView = findViewById(R.id.facility_details_address);

        mapIntentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = String.valueOf(addressTextView.getText());
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
}
