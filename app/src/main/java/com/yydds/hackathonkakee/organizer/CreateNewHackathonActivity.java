package com.yydds.hackathonkakee.organizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;
import com.yydds.hackathonkakee.general.Utility;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateNewHackathonActivity extends AppCompatActivity {

    final String[] modes = {"Online", "Hybrid", "Physical"};
    String mode = "", hackathonName, hackathonVenue, shortDesc, longDesc, iconUri = "";
    int prizePool, maxTeamMembers;
    boolean iconIsLoaded, isCurrentDateStart = true, gotImageFromDB = false;
    Intent uploadedImageIntent;
    Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse("1/1/1990");
    Timestamp startDateTS = new Timestamp(initDate);
    Timestamp endDateTS = new Timestamp(initDate);

    //fields get from Intent extras
    String organizerID = "";
    Hackathon hackathon = null;
    String hackathonID = "";

    TextView pageTitleTv;
    EditText hackathonNameEt, hackathonVenueEt, prizePoolEt, maxTeamMembersEt, shortDescEt, longDescEt;
    MaterialButton createHackathonBtn, deleteHackathonBtn, startDateBtn, endDateBtn;
    ImageView backArrowIv, hackathonIconIv;
    ProgressBar createHackathonLoadPB, deleteHackathonLoadPB;

    AutoCompleteTextView modeActv;

    ArrayAdapter<String> modeAdapterItems;

    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;

    DatePickerDialog datePickerDialog;

    public CreateNewHackathonActivity() throws ParseException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_hackathon);

        //get Intent
        organizerID = getIntent().getStringExtra("organizerID");
        hackathon = (Hackathon) getIntent().getSerializableExtra("hackathon");
        hackathonID = getIntent().getStringExtra("hackathonID");

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        initializeComponents();
        initDatePicker();

        // set up page for EDIT hackathon page
        if (hackathonID != null && !hackathonID.isEmpty()) {
            pageTitleTv.setText("Edit Hackathon");
            deleteHackathonBtn.setVisibility(View.VISIBLE);
            createHackathonBtn.setText("Save");
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    hackathon = documentSnapshot.toObject(Hackathon.class);
                    initializeFieldForEditHackathon();
                }
            });
        }
        //set up page for CREATE hackathon page
        else {
            pageTitleTv.setText("Create New Hackathon");
            deleteHackathonBtn.setVisibility(View.GONE);
            createHackathonBtn.setText("Create New Hackathon");
        }
    }

    private void initializeComponents() {
        modeActv = findViewById(R.id.modeActv);
        hackathonNameEt = findViewById(R.id.hackathonNameEt);
        hackathonVenueEt = findViewById(R.id.hackathonVenueEt);
        prizePoolEt = findViewById(R.id.prizePoolEt);
        maxTeamMembersEt = findViewById(R.id.maxTeamMembersEt);
        shortDescEt = findViewById(R.id.shortDescEt);
        longDescEt = findViewById(R.id.longDescEt);
        backArrowIv = findViewById(R.id.backArrowIv);
        pageTitleTv = findViewById(R.id.pageTitleTv);
        createHackathonBtn = findViewById(R.id.createHackathonBtn);
        deleteHackathonBtn = findViewById(R.id.deleteHackathonBtn);
        hackathonIconIv = findViewById(R.id.hackathonIconIv);
        createHackathonLoadPB = findViewById(R.id.createHackathonLoadPB);
        deleteHackathonLoadPB = findViewById(R.id.deleteHackathonLoadPB);
        startDateBtn = findViewById(R.id.startDateBtn);
        endDateBtn = findViewById(R.id.endDateBtn);

        startDateBtn.setOnClickListener((v) -> handleStartDateBtn());
        endDateBtn.setOnClickListener((v) -> handleEndDateBtn());

        createHackathonBtn.setOnClickListener((v) -> saveHackathon());
        deleteHackathonBtn.setOnClickListener((v) -> deleteHackathonFromFirebase());

        modeAdapterItems = new ArrayAdapter<String>(this, R.layout.create_hackathon_mode_list_view, modes);
        modeActv.setAdapter(modeAdapterItems);
        modeActv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mode = adapterView.getItemAtPosition(i).toString();
            }
        });

        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iconIsLoaded = false;
        hackathonIconIv.setOnClickListener((v) -> handleUploadIcon());
    }

    private void initializeFieldForEditHackathon() {
        String startDate = new SimpleDateFormat("dd/MM/yyyy").format(hackathon.getStartDateTS().toDate());
        String endDate = new SimpleDateFormat("dd/MM/yyyy").format(hackathon.getEndDateTS().toDate());
        String period = startDate + " - " + endDate;

        Picasso.get().load(hackathon.getIconUri()).into(hackathonIconIv);
        iconIsLoaded = true;
        gotImageFromDB = true;

        hackathonNameEt.setText(hackathon.getName());
        hackathonVenueEt.setText(hackathon.getVenue());
        shortDescEt.setText(hackathon.getShortDesc());
        longDescEt.setText(hackathon.getLongDesc());
        prizePoolEt.setText(String.valueOf(hackathon.getPrizePool()));
        maxTeamMembersEt.setText(String.valueOf(hackathon.getMaxTeamMembers()));
        modeActv.setText(hackathon.getMode(), false);
        mode = hackathon.getMode();
        startDateTS = hackathon.getStartDateTS();
        endDateTS = hackathon.getEndDateTS();
        System.out.println(hackathon.getStartDateTS().toDate().getDay() + " " + hackathon.getStartDateTS().toDate().getMonth() + " " + hackathon.getStartDateTS().toDate().getYear());
        startDateBtn.setText(makeDateString(hackathon.getStartDateTS().toDate().getDate(), hackathon.getStartDateTS().toDate().getMonth() + 1, hackathon.getStartDateTS().toDate().getYear() + 1900));
        endDateBtn.setText(makeDateString(hackathon.getEndDateTS().toDate().getDay(), hackathon.getEndDateTS().toDate().getMonth() + 1, hackathon.getEndDateTS().toDate().getYear() + 1900));
    }

    private void saveHackathon() {
        hackathonName = hackathonNameEt.getText().toString();
        hackathonVenue = hackathonVenueEt.getText().toString();
        shortDesc = shortDescEt.getText().toString();
        longDesc = longDescEt.getText().toString();
        prizePool = 0;
        maxTeamMembers = 0;

        if (!validateInput(hackathonName, hackathonVenue, shortDesc, longDesc)) return;
        if (!iconIsLoaded) {
            Toast.makeText(this, "Please upload hackathon icon!", Toast.LENGTH_SHORT).show();
            return;
        }

        changeInProgress(true);
        uploadIconToFirebase();
        changeInProgress(false);
    }

    private void saveHackathonInFirebase() {
//        String startDate = startDateBtn.getText().toString();
//        String endDate = endDateBtn.getText().toString();

        Hackathon newHackathon = new Hackathon(hackathonName, shortDesc, longDesc, mode, hackathonVenue, organizerID, iconUri, startDateTS, endDateTS, prizePool, maxTeamMembers);
        DocumentReference documentReference;
        if (hackathonID != null && !hackathonID.isEmpty()) {
            documentReference = firebaseFirestore.collection("Hackathons").document(hackathonID);
        } else {
            documentReference = firebaseFirestore.collection("Hackathons").document(Utility.generateID());
        }
        documentReference.set(newHackathon).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateNewHackathonActivity.this, "Save hackathon successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateNewHackathonActivity.this, "Failed to save hackathon.", Toast.LENGTH_SHORT).show();
                //TODO delete uploaded hackathon icon
                changeInProgress(false);
            }
        });
    }

    private void deleteHackathonFromFirebase() {
        changeInProgress(true);
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateNewHackathonActivity.this, "Delete hackathon successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateNewHackathonActivity.this, "Failed to delete hackathon.", Toast.LENGTH_SHORT).show();
            }
        });
        changeInProgress(false);
    }

    private boolean validateInput(String hackathonName, String hackathonLocation, String shortDesc, String longDesc) {
        boolean isValid = true;
        if (hackathonName.isEmpty()) {
            hackathonNameEt.setError("Please provide hackathon name.");
            hackathonNameEt.requestFocus();
            isValid = false;
        }
        if (hackathonLocation.isEmpty()) {
            hackathonVenueEt.setError("Please provide hackathon location.");
            hackathonVenueEt.requestFocus();
            isValid = false;
        }
        if (prizePoolEt.getText().toString().isEmpty()) {
            prizePoolEt.setError("Please provide prize pool.");
            prizePoolEt.requestFocus();
            isValid = false;
        } else {
            try {
                prizePool = Integer.parseInt(prizePoolEt.getText().toString());
            } catch (Exception e) {
                prizePoolEt.setError("Please enter amount of prize pool.");
                prizePoolEt.requestFocus();
                isValid = false;
            }
        }
        if (maxTeamMembersEt.getText().toString().isEmpty()) {
            maxTeamMembersEt.setError("Please provide max team members.");
            maxTeamMembersEt.requestFocus();
            isValid = false;
        } else {
            try {
                maxTeamMembers = Integer.parseInt(prizePoolEt.getText().toString());
            } catch (Exception e) {
                maxTeamMembersEt.setError("Please provide max team members in number.");
                maxTeamMembersEt.requestFocus();
                isValid = false;
            }
        }
        if (shortDesc.isEmpty()) {
            shortDescEt.setError("Please provide short description.");
            shortDescEt.requestFocus();
            isValid = false;
        }
        if (longDesc.isEmpty()) {
            longDescEt.setError("Please provide long description");
            longDescEt.requestFocus();
            isValid = false;
        }
        if (mode.isEmpty()) {
            modeActv.setError("Please choose mode.");
            modeActv.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private void handleUploadIcon() {
        gotImageFromDB = false;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            InputStream inputStream = null;
            try {
                assert selectedImage != null;
                inputStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            BitmapFactory.decodeStream(inputStream);
            hackathonIconIv.setImageURI(selectedImage);
            uploadedImageIntent = data;
            iconIsLoaded = true;
        }
    }

    private void uploadIconToFirebase() {
        if (gotImageFromDB) {
            iconUri = hackathon.getIconUri();
            saveHackathonInFirebase();
            return;
        }
        if (iconIsLoaded && uploadedImageIntent != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Hackathon icon is uploading......");
            progressDialog.show();

            StorageReference reference = storageReference.child("hackathonIcon/" + hackathonName + ".png");
            reference.putFile(uploadedImageIntent.getData())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri uri = uriTask.getResult();
                            System.out.println(uri.toString());
                            iconUri = uri.toString();
                            progressDialog.dismiss();
                            saveHackathonInFirebase();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressDialog.setMessage("Hackathon icon uploaded... " + (int) progress + "%");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateNewHackathonActivity.this, "Upload hackathon icon failed.", Toast.LENGTH_SHORT).show();
                            changeInProgress(false);
                        }
                    });
        } else {
            Toast.makeText(this, "Upload hackathon icon failed.", Toast.LENGTH_SHORT).show();
            changeInProgress(false);
        }
    }

    private void changeInProgress(boolean inProgress) {
        if (inProgress) {
            createHackathonLoadPB.setVisibility(View.VISIBLE);
            createHackathonBtn.setVisibility(View.INVISIBLE);
            deleteHackathonBtn.setVisibility(View.INVISIBLE);
        } else {
            createHackathonLoadPB.setVisibility(View.INVISIBLE);
            createHackathonBtn.setVisibility(View.VISIBLE);
            if (hackathon != null) {
                deleteHackathonBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    private void handleStartDateBtn() {
        isCurrentDateStart = true;
        datePickerDialog.show();
    }

    private void handleEndDateBtn() {
        isCurrentDateStart = false;
        datePickerDialog.show();
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;

                String date = makeDateString(day, month, year);
                if (isCurrentDateStart) {
                    try {
                        startDateTS = new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse( day +"/" + month + "/" + year));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return;
                    }
                    startDateBtn.setText(date);
                    System.out.println(new SimpleDateFormat("MM/dd/yyyy").format(startDateTS.toDate()));
                }
                else {
                    try {
                        endDateTS = new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse( day +"/" + month + "/" + year));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return;
                    }
                    endDateBtn.setText(date);
                    System.out.println(new SimpleDateFormat("MM/dd/yyyy").format(endDateTS.toDate()));
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        String monthString = "";
        if (month == 1) monthString = "JAN";
        else if (month == 2) monthString = "FEB";
        else if (month == 3) monthString = "MAR";
        else if (month == 4) monthString = "APR";
        else if (month == 5) monthString = "MAY";
        else if (month == 6) monthString = "JUN";
        else if (month == 7) monthString = "JUL";
        else if (month == 8) monthString = "AUG";
        else if (month == 9) monthString = "SEP";
        else if (month == 10) monthString = "OCT";
        else if (month == 11) monthString = "NOV";
        else if (month == 12) monthString = "DEC";
        return day + " " + monthString + " " + year;
    }

}