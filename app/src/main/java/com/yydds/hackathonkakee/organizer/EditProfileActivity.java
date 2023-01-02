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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.yydds.hackathonkakee.classes.Organizer;
import com.yydds.hackathonkakee.participant.ParticipantEditProfileActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {
    String organizerID, name, phoneNumber, jobPosition, employer, picUri = "", gender = "";

    Organizer currentOrganizer;
    EditText nameET, phoneNumET, jobPosET, employerET;
    TextView emailTV, pageTitleTV;
    MaterialButton birthDateBtn, saveBtn;
    RadioGroup genderRG;
    RadioButton maleRB, femaleRB;
    ImageView profilePicIV, backArrowIV, deletePicBtn;
    DatePickerDialog datePickerDialog;
    Intent uploadedImageIntent;
    boolean picIsLoaded;
    Timestamp birthDate = new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1900"));;

    public EditProfileActivity() throws ParseException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        organizerID = getIntent().getStringExtra("organizerID");

        initComponents();
        initDatePicker();
        assignValue();

    }

    private void initComponents() {
        nameET = findViewById(R.id.nameET);
        phoneNumET = findViewById(R.id.phoneNumberET);
        jobPosET = findViewById(R.id.jobPositionET);
        employerET = findViewById(R.id.employerET);
        nameET = findViewById(R.id.nameET);
        birthDateBtn = findViewById(R.id.birthDateBtn);
        genderRG = findViewById(R.id.genderRG);
        maleRB = findViewById(R.id.maleRB);
        femaleRB = findViewById(R.id.femaleRB);
        emailTV = findViewById(R.id.emailTV);
        pageTitleTV = findViewById(R.id.pageTitleTv);
        profilePicIV = findViewById(R.id.profilePictureIV);
        backArrowIV = findViewById(R.id.backArrowIv);
        deletePicBtn = findViewById(R.id.deletePicBtn);
        saveBtn = findViewById(R.id.saveBtn);

        backArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrganizerProfile();
            }
        });
        birthDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        profilePicIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/");
                startActivityForResult(intent, 100);
            }
        });
    }

    private void assignValue() {
        pageTitleTV.setText("Edit profile");

        DocumentReference organizerDR = FirebaseFirestore.getInstance().collection("Organizers").document(organizerID);
        organizerDR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Organizer organizer = documentSnapshot.toObject(Organizer.class);
                currentOrganizer = organizer;
                nameET.setText(organizer.getName());
                emailTV.setText(organizer.getEmail());
                phoneNumET.setText(organizer.getPhoneNumber());
                jobPosET.setText(organizer.getJobPosition());
                employerET.setText(organizer.getEmployer());
                picUri = organizer.getProfilePicUrl();
                if (!organizer.getProfilePicUrl().isEmpty()) Picasso.get().load(organizer.getProfilePicUrl()).into(profilePicIV);
                birthDateBtn.setText(makeDateString(organizer.getBirthDate().toDate().getDate(), organizer.getBirthDate().toDate().getMonth() + 1, organizer.getBirthDate().toDate().getYear() + 1900));
                birthDate = organizer.getBirthDate();

                String genderFromDB = organizer.getGender();
                if (maleRB.getText().toString().equals(genderFromDB)) maleRB.setChecked(true);
                else if (femaleRB.getText().toString().equals(genderFromDB)) femaleRB.setChecked(true);

                picIsLoaded = false;

                checkPicUrl();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Failed to get organizer details", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void saveOrganizerProfile() {
        int selectedID = genderRG.getCheckedRadioButtonId();

        name = nameET.getText().toString();
        gender = (maleRB.isChecked() | femaleRB.isChecked()) ?  ((RadioButton) findViewById(selectedID)).getText().toString() : "";
        phoneNumber = phoneNumET.getText().toString();
        jobPosition = jobPosET.getText().toString();
        employer = employerET.getText().toString();
        if (!validateInput(name)) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
            return;
        }
        uploadPicToFirebase();
    }

    private void uploadPicToFirebase() {
        if (!picIsLoaded) {
            saveOrganizerProfileToFirebase();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile picture is uploading......");
        progressDialog.show();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference reference = storageReference.child("organizerProfilePic/" + organizerID + ".png");
        reference.putFile(uploadedImageIntent.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                picUri = uri.toString();
                progressDialog.dismiss();
                saveOrganizerProfileToFirebase();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Upload profile picture failed.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploading profile picture..." + (int) progress + "%");
            }
        });
    }

    private void saveOrganizerProfileToFirebase() {
        currentOrganizer.updateProfile(name, gender, phoneNumber, jobPosition, employer, picUri, birthDate);
        FirebaseFirestore.getInstance().collection("Organizers").document(organizerID).set(currentOrganizer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditProfileActivity.this, "Update profile successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;

                String date = makeDateString(day, month, year);
                try {
                    birthDate = new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse( day +"/" + month + "/" + year));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
                birthDateBtn.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void checkPicUrl() {
        if (picUri.isEmpty()) {
            deletePicBtn.setVisibility(View.INVISIBLE);
            profilePicIV.setImageResource(R.drawable.default_image);
        } else {
            deletePicBtn.setVisibility(View.VISIBLE);
        }
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

    private boolean validateInput(String name) {
        boolean isValid = true;
        if (name.isEmpty()) {
            nameET.setError("Name cannot be empty!");
            nameET.requestFocus();
            isValid = false;
        }
        return isValid;
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
            profilePicIV.setImageURI(selectedImage);
            uploadedImageIntent = data;
            picIsLoaded = true;
            deletePicBtn.setVisibility(View.INVISIBLE);
        }
    }
}