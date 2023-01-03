package com.yydds.hackathonkakee.organizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import com.yydds.hackathonkakee.classes.News;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateNewsActivity extends AppCompatActivity {
    String hackathonID, newsID = "", picUri = "", title, content, randomStr;
    TextView pageTitleTV;
    ImageView backArrowIV, pictureIV;
    TextInputLayout contentTIL, titleTIL;
    TextInputEditText contentTIET, titleTIET;
    MaterialButton saveNewsBtn, deleteNewsBtn, addPictureBtn, deletePictureBtn;
    News news;
    Intent uploadedImageIntent;
    boolean picIsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);
        hackathonID = getIntent().getStringExtra("hackathonID");
        newsID = getIntent().getStringExtra("newsID");

        initComponents();

        if (newsID != null && !newsID.isEmpty()) {
            pageTitleTV.setText("Edit News");
            deleteNewsBtn.setVisibility(View.VISIBLE);
            saveNewsBtn.setText("Save news");
            DocumentReference newsDF = FirebaseFirestore.getInstance().collection("News").document(newsID);
            newsDF.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    news = documentSnapshot.toObject(News.class);
                    assignFieldForEditNews();
                }
            });
        } else {
            pageTitleTV.setText("Create News");
            deleteNewsBtn.setVisibility(View.INVISIBLE);
            saveNewsBtn.setText("Create news");
            pictureIV.setVisibility(View.GONE);
            addPictureBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initComponents() {
        pageTitleTV = findViewById(R.id.pageTitleTv);
        backArrowIV = findViewById(R.id.backArrowIv);
        saveNewsBtn = findViewById(R.id.saveNewsBtn);
        deleteNewsBtn = findViewById(R.id.deleteNewsBtn);
        contentTIET = findViewById(R.id.contentTIET);
        contentTIL = findViewById(R.id.contentTIL);
        titleTIET = findViewById(R.id.titleTIET);
        titleTIL = findViewById(R.id.titleTIL);
        pictureIV = findViewById(R.id.pictureIV);
        addPictureBtn = findViewById(R.id.addPictureBtn);
        deletePictureBtn = findViewById(R.id.deletePictureBtn);

        pageTitleTV.setText("Create News");

        backArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNews();
            }
        });
        deleteNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNews();
            }
        });
        addPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/");
                startActivityForResult(intent, 100);
            }
        });
        deletePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictureIV.setVisibility(View.GONE);
                addPictureBtn.setVisibility(View.VISIBLE);
                deletePictureBtn.setVisibility(View.INVISIBLE);
                picUri = "";
                picIsLoaded = false;
                uploadedImageIntent = null;
            }
        });

    }

    private void assignFieldForEditNews() {
        titleTIET.setText(news.getTitle());
        contentTIET.setText(news.getContent());
        if (news.getPictureUri().isEmpty()) {
            pictureIV.setVisibility(View.INVISIBLE);
            addPictureBtn.setVisibility(View.VISIBLE);
            deletePictureBtn.setVisibility(View.INVISIBLE);
        } else {
            picUri = news.getPictureUri();
            pictureIV.setVisibility(View.VISIBLE);
            Picasso.get().load(picUri).into(pictureIV);
            addPictureBtn.setVisibility(View.INVISIBLE);
            deletePictureBtn.setVisibility(View.VISIBLE);
        }
    }

    private void saveNews() {
        title = titleTIET.getText().toString();
        content = contentTIET.getText().toString();
        if (!validateInput(title, content)) return;

        StringBuilder randAlp = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            randAlp.append((char)('a' + (int)(Math.random() * 26)));
        }
        randomStr = randAlp.toString();

        if (picIsLoaded) {
            uploadImageToFirebase();
        } else {
            saveNewsToFirebase();
        }

    }

    private void deleteNews() {
        FirebaseFirestore.getInstance().collection("News").document(newsID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateNewsActivity.this, "Delete news successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void uploadImageToFirebase() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile picture is uploading......");
        progressDialog.show();

        String pictureName = (newsID != null && !newsID.isEmpty()) ? newsID : randomStr;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference reference = storageReference.child("newsPicture/" + pictureName +  ".png");
        reference.putFile(uploadedImageIntent.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                picUri = uri.toString();
                progressDialog.dismiss();
                saveNewsToFirebase();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateNewsActivity.this, "Upload profile picture failed.", Toast.LENGTH_SHORT).show();
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

    private void saveNewsToFirebase() {
        News newNews = new News(title, content, hackathonID, picUri, Timestamp.now());
        DocumentReference newsDR;
        String operation = "";
        if (newsID != null && !newsID.isEmpty()) {
            newsDR = FirebaseFirestore.getInstance().collection("News").document(newsID);
            operation = "Edit";
        } else {
            newsDR = FirebaseFirestore.getInstance().collection("News").document(randomStr);
            operation = "Create";
        }
        String finalOperation = operation;
        newsDR.set(newNews).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateNewsActivity.this, finalOperation + " news successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    private boolean validateInput(String title, String content) {
        boolean isValid = true;
        if (title.isEmpty()) {
            titleTIL.setErrorEnabled(true);
            titleTIL.setError("Please enter title");
            isValid = false;
        } else {
            titleTIL.setErrorEnabled(false);
        }
        if (content.isEmpty()) {
            contentTIL.setErrorEnabled(true);
            contentTIL.setError("Please enter content");
            isValid = false;
        } else {
            contentTIL.setErrorEnabled(false);
        }
        if (uploadedImageIntent == null && picUri.isEmpty()) {
            Toast.makeText(this, "Please upload news picture.", Toast.LENGTH_SHORT).show();
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
            pictureIV.setImageURI(selectedImage);
            pictureIV.setVisibility(View.VISIBLE);
            uploadedImageIntent = data;
            picIsLoaded = true;
            addPictureBtn.setVisibility(View.INVISIBLE);
            deletePictureBtn.setVisibility(View.VISIBLE);
        }
    }
}