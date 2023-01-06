package com.yydds.hackathonkakee.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.general.LoginActivity;

public class OrganizerHomePageActivity extends AppCompatActivity {

    MaterialButton createHackathonBtn, myHackathonBtn;
    FloatingActionButton logoutBtn;
    String organizerID;
    ImageView profileIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_home_page);
        organizerID = getIntent().getStringExtra("organizerID");
//        Toast.makeText(this, "" + organizerID, Toast.LENGTH_SHORT).show();

        initializeComponent();
    }

    private void initializeComponent() {
        createHackathonBtn = findViewById(R.id.createHackathonBtn);
        myHackathonBtn = findViewById(R.id.myHackathonBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        profileIV = findViewById(R.id.profileIV);

        createHackathonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerHomePageActivity.this, CreateNewHackathonActivity.class);
//                System.out.println(organizerID);
                intent.putExtra("organizerID", organizerID);
                startActivity(intent);
            }
        });
        myHackathonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerHomePageActivity.this, OrganizerMyHackathonPageActivity.class);
                intent.putExtra("organizerID", organizerID);
                startActivity(intent);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                MaterialButton yesBtn, noBtn;
                dialog = new Dialog(OrganizerHomePageActivity.this);
                dialog.setContentView(R.layout.confirmation_pop_up);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false); //Optional
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                TextView titleTV = dialog.findViewById(R.id.titleTV), contentTV = dialog.findViewById(R.id.contentTV);
                titleTV.setText("Logout");
                contentTV.setText("Are you sure to logout from your account?");
                yesBtn = dialog.findViewById(R.id.yesBtn);
                noBtn = dialog.findViewById(R.id.noBtn);

                dialog.show();

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(OrganizerHomePageActivity.this, LoginActivity.class));
                        finish();
                        dialog.dismiss();
                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerHomePageActivity.this, ProfileActivity.class);
                intent.putExtra("organizerID", organizerID);
                startActivity(intent);
            }
        });
    }
}