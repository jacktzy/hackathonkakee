package com.yydds.hackathonkakee.participant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.general.LoginActivity;

public class ParticipantHomePageActivity extends AppCompatActivity {

    Toolbar toolbar;
    String participantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_home_page);

        //get extra from login activity
        participantID = getIntent().getStringExtra("participantID");

        toolbar = findViewById(R.id.participantHomeToolbar);
        setSupportActionBar(toolbar);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.participantHomeFragmentContainer);
        NavController navController = host.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        setupBottomNavMenu(navController);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            Navigation.findNavController(this, R.id.participantHomeFragmentContainer).navigate(item.getItemId());
            return true;
        } catch (Exception e) {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.participantHomeFragmentContainer).navigateUp();
    }

    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.participantHomeBottomNavbar);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}