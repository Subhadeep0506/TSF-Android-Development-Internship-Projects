package com.example.socialmediaintegrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UserView extends AppCompatActivity {

    ImageView roundImage;
    TextView tvUsername, tvEmail, textView3;
    Button btnLogout;

    int loginMode;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        roundImage = findViewById(R.id.roundImage);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        textView3 = findViewById(R.id.textView3);
        btnLogout = findViewById(R.id.btnLogout);

        String username = "";
        String email = "";
        Uri personPhoto = null;

        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        loginMode = getIntent().getIntExtra("login_mode", 1);

        //  Initializing google sign-in tools
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //  displaying the user details depending upon the sign-in mode. Username is common for all
        tvUsername.setText(username);
        //  When logged-in using username and password
        if (loginMode == 1) {

            tvEmail.setText(email);
        }
        //  Google sign-in
        else if (loginMode == 2){
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                username = acct.getDisplayName();
                email = acct.getEmail();
                personPhoto = acct.getPhotoUrl();
            }
            tvUsername.setText(username);
            tvEmail.setText(email);
            Glide.with(this).load(String.valueOf(personPhoto)).into(roundImage);
        }
        //  Facebook sign-in
        else {
            textView3.setVisibility(View.INVISIBLE);
            tvEmail.setVisibility(View.INVISIBLE);

            Glide.with(this).load("https://graph.facebook.com/" +
                    getIntent().getStringExtra("userId") + "/picture?type=large").into(roundImage);
        }

        //  Logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Signing out from facebook login
                if (loginMode == 3) {
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(UserView.this, MainActivity.class);
                    Toast.makeText(UserView.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
                //  Signing out from google login
                else if (loginMode == 2){
                    switch (v.getId()) {
                        case R.id.btnLogout:
                            signOut();
                            break;
                    }
                    Intent intent = new Intent(UserView.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
                //  Signing out from username login
                else {
                    Intent intent = new Intent(UserView.this, MainActivity.class);
                    Toast.makeText(UserView.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
            }
        });
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UserView.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}