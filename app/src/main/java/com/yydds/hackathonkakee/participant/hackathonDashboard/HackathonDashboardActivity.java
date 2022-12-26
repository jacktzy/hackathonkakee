package com.yydds.hackathonkakee.participant.hackathonDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;

public class HackathonDashboardActivity extends AppCompatActivity {
    Toolbar toolbar;
    String hackathonID, participantID, hackathonName;
    TextView pageTitleTV;
    ImageView backArrowIB;

    AnnouncementFragment announcementFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hackathon_dashboard);

        hackathonID = getIntent().getStringExtra("hackathonID");
        participantID = getIntent().getStringExtra("participantID");
        hackathonName = getIntent().getStringExtra("hackathonName");

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        announcementFragment = new AnnouncementFragment();
//
//        fragmentTransaction.replace(android.R.id.content, announcementFragment, "ANNOUNCEMENT_FRAGMENT");

        initComponents();

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.hackathonDashboardFragmentContainer);
        NavController navController = host.getNavController();

        setupBottomNavMenu(navController);

    }

    private void initComponents() {
        pageTitleTV = findViewById(R.id.pageTitleTv);
        pageTitleTV.setText(hackathonName);

        backArrowIB = findViewById(R.id.backArrowIv);
        backArrowIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (!Navigation.findNavController(HackathonDashboardActivity.this, R.id.hackathonDashboardFragmentContainer).navigateUp()) finish();

//                Fragment fragment = getFragmentManager().findFragmentById(R.id.hackathonDashboardFragmentContainer);
//                if (fragment instanceof AnnouncementFragment) {
//
//                }
//                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            Navigation.findNavController(this, R.id.hackathonDashboardFragmentContainer).navigate(item.getItemId());
            return true;
        } catch (Exception e) {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.hackathonDashboardFragmentContainer).navigateUp();
    }

    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.hackathonDashboardBottomNavbar);
        NavigationUI.setupWithNavController(bottomNavigationView, navController, false);
    }
}