package com.example.shaf.sgsports;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shaf.sgsports.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;

public class EditProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    User mUser;
    FirebaseFirestore db;
    String userId;
    ArrayList<Integer> selectedItems = new ArrayList<>();
    final int[] checkedItem = {0};

    EditText nameEditText;
    EditText aboutMeEditText;
    TextView genderText;

    public static final String TAG = EditProfileActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setElevation(0);
        db = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra(USER_ACCT_ID);
        getUser(userId);

        nameEditText = findViewById(R.id.edit_profile_name);
        aboutMeEditText = findViewById(R.id.edit_profile_about_me);

        genderText = findViewById(R.id.edit_profile_gender_text);
        genderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });


    }

    private void showGenderDialog() {
        final List<String> genderArray = Arrays.asList(getResources().getStringArray(R.array.gender));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setSingleChoiceItems(R.array.gender, checkedItem[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItem[0] = which;
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String gender = genderArray.get(checkedItem[0]);
                mUser.setGender(gender);
                genderText.setText(gender);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void getUser(String userId) {
        Log.e(TAG, "user id:" + userId);

        db.collection("users").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User currentUser = documentSnapshot.toObject(User.class);
                        if (currentUser != null) {
                            mUser = currentUser;
                            nameEditText.setText(currentUser.getName());

                            String bio = currentUser.getAboutMe();
                            if (bio == null || bio.isEmpty())
                                aboutMeEditText.setHint("Add bio");
                            else
                                aboutMeEditText.setText(bio);

                            String gender = currentUser.getGender();
                            if (gender==null || gender.isEmpty())
                                genderText.setText("Unknown");
                            else
                                genderText.setText(gender);

                            CircleImageView profileIcon = findViewById(R.id.edit_profile_icon);
                            Glide.with(EditProfileActivity.this).load(currentUser.getImageURL()).into(profileIcon);


//                            container.setVisibility(View.VISIBLE);
//                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });


    }

    private void updateUser() {
        if (mUser != null) {
            mUser.setGender(genderText.getText().toString());
            mUser.setAboutMe(aboutMeEditText.getText().toString());
            db.collection("users").document(userId).set(mUser);
            Toast.makeText(this, "Profile Updated.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.update_profile) {
            updateUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
