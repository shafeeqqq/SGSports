package com.example.shaf.sgsports;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shaf.sgsports.Model.Facility;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.shaf.sgsports.ChooseFacilityActivity.CHOOSE;
import static com.example.shaf.sgsports.FacilitiesFragment.FACILITY_ID;

public class FacilityDetailsActivity extends AppCompatActivity {

    public static final String CHOICE_CONFIRM = "confirm";
    private TextView mapIntentTextView;
    private TextView addressTextView;
    private TextView nameTextView;
    private TextView operatingHrsTextView;
    private TextView phoneTextView;
    private TextView facilityInfoTextView;
    private LinearLayoutCompat container;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Button button;

    private String facilityID;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_details);

        db = FirebaseFirestore.getInstance();

        facilityID = getIntent().getStringExtra(FACILITY_ID);
        boolean choose = getIntent().getBooleanExtra(CHOOSE, false);

        assert facilityID != null;

        progressBar = findViewById(R.id.facility_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        button = findViewById(R.id.facility_details_button);
        if (choose)
            button.setText("SELECT");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
                Toast.makeText(FacilityDetailsActivity.this, "Location chosen.", Toast.LENGTH_SHORT).show();
            }
        });

        mapIntentTextView = findViewById(R.id.facility_details_map_intent_text);
        addressTextView = findViewById(R.id.facility_details_address);
        nameTextView = findViewById(R.id.facility_details_name_text);
        operatingHrsTextView = findViewById(R.id.facility_details_operating_hours);
        phoneTextView = findViewById(R.id.facility_details_telephone);
        facilityInfoTextView = findViewById(R.id.facility_details_description);
        imageView = findViewById(R.id.facility_details_org_pic);

        container = findViewById(R.id.facility_container);
        container.setVisibility(View.GONE);

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

        addressTextView.setOnClickListener(new View.OnClickListener() {
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

    private void closeActivity() {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(CHOICE_CONFIRM, true);

        setResult(RESULT_OK, replyIntent);
        finish();
    }


    @Override
    public void onStart() {
        super.onStart();

        db.collection("facility").document(facilityID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Facility facility = documentSnapshot.toObject(Facility.class);

                        if (facility != null) {
                            nameTextView.setText(facility.getName());

                            Glide.with(FacilityDetailsActivity.this).load(facility.getImageUrl()).centerCrop().into(imageView);

                            String operatingHrs = facility.getOperatingHours();
                            if (operatingHrs.isEmpty())
                                operatingHrs = "-";
                            operatingHrsTextView.setText(operatingHrs);

                            String address = facility.getAddressText();
                            if (address.isEmpty())
                                address = "-";
                            addressTextView.setText(address);

                            String phone = facility.getPhone();
                            if (phone.isEmpty())
                                phone = "-";
                            phoneTextView.setText(phone);

                            String facilityInfo = facility.getFacilityInformation();
                            if (facilityInfo.isEmpty())
                                facilityInfo = "-";
                            facilityInfoTextView.setText(facilityInfo);

                            container.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });

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
