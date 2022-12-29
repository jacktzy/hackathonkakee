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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;
import com.yydds.hackathonkakee.classes.Team;
import com.yydds.hackathonkakee.general.Utility;

import java.util.List;

public class HackathonDashboardActivity extends AppCompatActivity {
    Toolbar toolbar;
    String hackathonID, participantID, hackathonName;
    TextView pageTitleTV;
    ImageView backArrowIB, menuIV;

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
        menuIV = findViewById(R.id.menuIV);

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

        menuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(HackathonDashboardActivity.this, menuIV);
                popupMenu.getMenu().add("Withdraw");
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle() == "Withdraw") {
                            withdrawHackathon();
                            return true;
                        }
                        return false;
                    }
                });
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

    private void withdrawHackathon() {
        Query query = FirebaseFirestore.getInstance().collection("Teams").whereArrayContains("membersID", participantID);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean hasTeam ;
                String teamID = "";
                if (queryDocumentSnapshots.size() == 0) hasTeam = false;
                else hasTeam = true;

                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : list) {
                    teamID = documentSnapshot.getId();
                }
                FirebaseFirestore.getInstance().collection("Participants").document(participantID).update("participatedHackathonId", FieldValue.arrayRemove(hackathonID));
                FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID).update("participantsID", FieldValue.arrayRemove(participantID));
                if (hasTeam) {
                    Utility.deleteAMemberFromTeam(participantID, teamID, hackathonID); FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID).update("participantsID", FieldValue.arrayRemove(participantID));
                }
                finish();
            }
        });
    }
}