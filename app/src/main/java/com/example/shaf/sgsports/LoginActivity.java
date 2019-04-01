package com.example.shaf.sgsports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shaf.sgsports.Model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.shaf.sgsports.HomeActivity.LOGGED_IN_FLAG;
import static com.example.shaf.sgsports.HomeActivity.LOGIN_PREFS;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity" ;

    public static final String USER_ACCT_ID = "emailAddress" ;
    public static final String USER_ACCT_NAME = "user-name" ;
    public static final String USER_ACCT_ICON = "profile-icon" ;

    SharedPreferences sharedPref;
    FirebaseFirestore db;

    private GoogleApiClient googleApiClient;
    private FirebaseAuth mAuth;

    private static final int RC_SIGN_IN = 901;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        ImageView logo = findViewById(R.id.login_logo);
        Glide.with(this).load(R.drawable.fullsize_logo).into(logo);

        SignInButton mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.signin_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            finish();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            final GoogleSignInAccount account = result.getSignInAccount();
            Log.e(TAG, account.getId() + "IMG URL");


            final String name = account.getGivenName();
            String accountId = account.getId();
            String imgUrl = String.valueOf(account.getPhotoUrl());

            Log.e(TAG, imgUrl + "IMG URL");

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        Toast.makeText(LoginActivity.this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (account != null) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(LOGGED_IN_FLAG, true);
                editor.putString(USER_ACCT_ID, accountId);
                editor.putString(USER_ACCT_NAME, name);
                editor.putString(USER_ACCT_ICON, imgUrl);
                editor.apply();

                processUser(name, accountId, imgUrl);
            }

            else
                Toast.makeText(this, "Login Failed. Please try again.", Toast.LENGTH_LONG).show();

        }


    }

    private void processUser(String name, String accountId, String imgUrl) {
        final User user = new User(accountId, name, new Date(), imgUrl);

        db.collection("users").whereEqualTo("userID", accountId).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        initialiseUser(user);
                        Log.e(TAG, "New User");
                    }
                    else {
                        Log.e(TAG, "Old User");
                    }
                }
            }
        });

    }

    private void initialiseUser(User user) {
        db.collection("users").document(user.getUserID()).set(user);

        Map<String, Object> data = new HashMap<>();
        ArrayList<String> emptyList = new ArrayList<>();

        data.put("created", emptyList);
        data.put("joined", emptyList);
        data.put("requested", emptyList);
        data.put("rejected", emptyList);

        db.collection("userInEvents").document(user.getUserID()).set(data);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Error.", Toast.LENGTH_SHORT).show();
    }
}
