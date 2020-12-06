package com.example.myfood;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Home extends AppCompatActivity {
    ImageButton btn_home,btn_user,btn_Go;
    TextInputLayout store_name,store_owner_name,store_Email_address,store_location,store_phone_number,store_password,store_pincode;
    ImageView top_heading_image,iv_store_photo,iv_store_owner_pancard;
    TextView tv_Not_available,tv_Zafir,fill_up_the_form_tv;
    Spinner district;
    Button btn_submit_for_new_seller,btn_submit_for_new_seller_documents,btn_upload_store_photo,btn_upload_owner_pancard_photo;
    LinearLayout LL1,location_LL,new_seller_LL,new_seller_documentUpload_LL,last_page_for_newseller,existing_seller_profile_LL,existing_seller_all_item_LL;
    ScrollView existing_seller_loginpage_LL,existing_seller_addItem_LL;
    ProgressBar progressBar_newseller,progressBar_store_pancard,progressbar_store_photo;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseFirestore fstore;
    StorageReference mStorageRef;
    adapter_additem_existingseller myAdapter;

    RecyclerView myrecyclerView;

    TimePickerDialog picker;

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle("Exit app");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //iv_store_photo.setImageBitmap(imageBitmap);
            Uri imageuri = data.getData();

            upload_store_imageTO_firebase(imageuri);
        }
        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {
            Uri imageuri = data.getData();
            upload_pancard_imageTO_firebase(imageuri);
        }
        if (requestCode == 3000 && resultCode == Activity.RESULT_OK) {
            Uri imageuri = data.getData();
            ImageView item_imageView1 = (ImageView) findViewById(R.id.item_imageView1);
            String item1 = new String("item1");
            upload_item_imageTO_firebase(imageuri,item1,item_imageView1);
        }
        if (requestCode == 3001 && resultCode == Activity.RESULT_OK) {
            Uri imageuri = data.getData();
            final ImageView item_imageView2 = (ImageView) findViewById(R.id.item_imageView2);
            String item2 = new String("item2");
            upload_item_imageTO_firebase(imageuri,item2,item_imageView2);
        }
        if (requestCode == 3002 && resultCode == Activity.RESULT_OK) {
            Uri imageuri = data.getData();
            final ImageView item_imageView3 = (ImageView) findViewById(R.id.item_imageView3);
            String item3 = new String("item3");
            upload_item_imageTO_firebase(imageuri,item3,item_imageView3);
        }
        if (requestCode == 3003 && resultCode == Activity.RESULT_OK) {
            Uri imageuri = data.getData();
            final ImageView item_imageView4 = (ImageView) findViewById(R.id.item_imageView4);
            String item4 = new String("item4");
            upload_item_imageTO_firebase(imageuri,item4,item_imageView4);
        }
    }

    private void upload_item_imageTO_firebase(Uri imageuri, final String item, final ImageView item_imageView) {
        final String email = mAuth.getCurrentUser().getEmail();
        final EditText existing_seller_item_name = (EditText) findViewById(R.id.existing_seller_item_name);
        final String item_name = existing_seller_item_name.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Image uploading.........");

        final StorageReference filename = mStorageRef.child("Seller and store personal data/"+ email +"/Store All item images/"+item_name+"/"+item);
        filename.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().into(item_imageView);

                         String item_image_url = String.valueOf(uri);

                        Map<String,String> user2 = new HashMap<>();
                        user2.put("imagelink" + item,item_image_url);
                        fstore.collection("Seller and store personal data")
                                .document(email).collection("store all Item")
                                .document(item_name).set(user2,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(Home.this, "link not created", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }


    //upload seller store photo
    private void upload_store_imageTO_firebase(Uri imageuri) {
        String email = mAuth.getCurrentUser().getEmail();
        final StorageReference filename = mStorageRef.child("Seller and store personal data/"+ email +"/store image.jpg");
        filename.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().into(iv_store_photo);
                        progressbar_store_photo.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
    //upload seller store photo
//upload seller pancard photo -0
    private void upload_pancard_imageTO_firebase(Uri imageuri) {
        String email = mAuth.getCurrentUser().getEmail();
        final StorageReference filename = mStorageRef.child("Seller and store personal data/" + email +"/store pancard.jpg");
        filename.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().into(iv_store_owner_pancard);
                        progressBar_store_pancard.setVisibility(View.GONE);
                    }
                });
            }
        });
    }// upload seller pancard photo -1

    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }

    //private  FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private  CollectionReference collectionReference = db.collection("Seller and store personal data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_home = findViewById(R.id.btnhome);
        btn_user = findViewById(R.id.btn_user);
        btn_Go = findViewById(R.id.btn_Go);
        tv_Zafir = findViewById(R.id.Zafir);
        tv_Not_available = findViewById(R.id.tv_Not_available);
        LL1 = findViewById(R.id.LL1);
        location_LL = findViewById(R.id.location_LL);
        top_heading_image = findViewById(R.id.imageView7);
        fill_up_the_form_tv = findViewById(R.id.fill_the_form_tv);
        new_seller_LL = findViewById(R.id.new_seller_LL);
        new_seller_documentUpload_LL = findViewById(R.id.new_seller_documentUpload_LL);
        last_page_for_newseller = findViewById(R.id.last_page_for_newseller);
        btn_submit_for_new_seller = findViewById(R.id.btn_submit_for_new_seller);
        existing_seller_loginpage_LL = findViewById(R.id.existing_seller_loginpage_LL);
        btn_submit_for_new_seller_documents = findViewById(R.id.btn_submit_for_new_seller_documents);
        store_name = findViewById(R.id.store_name);
        iv_store_photo = findViewById(R.id.iv_store_photo);
        iv_store_owner_pancard = findViewById(R.id.iv_store_owner_pancard);
        btn_upload_store_photo = findViewById(R.id.btn_upload_store_photo);
        btn_upload_owner_pancard_photo = findViewById(R.id.btn_upload_owner_pancard_photo);
        //existing_seller_store_photo_iv = findViewById(R.id.existing_seller_store_photo_iv);
        store_owner_name = findViewById(R.id.store_owner_name);store_Email_address = findViewById(R.id.store_Email_address);store_location = findViewById(R.id.store_location);store_phone_number = findViewById(R.id.store_phone_number);store_password = findViewById(R.id.store_password);store_pincode = findViewById(R.id.store_pincode);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progressBar_newseller = findViewById(R.id.progressBar_newseller);progressbar_store_photo = findViewById(R.id.progressbar_store_photo);progressBar_store_pancard = findViewById(R.id.progressBar_store_pancard);
        myrecyclerView = findViewById(R.id.my_recycler_view);
        existing_seller_profile_LL = findViewById(R.id.existing_seller_profile_LL);
        existing_seller_addItem_LL = findViewById(R.id.existing_seller_addItem_LL);
        existing_seller_all_item_LL = findViewById(R.id.existing_seller_all_item_LL);
        addListenerOnSpinnerItemSelection();

        //setActionBar()
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.mytoolbar);
        // finish




//NEW SELLER REGISTRATION
        // upload store front photo
        btn_upload_store_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final int REQUEST_IMAGE_CAPTURE = 1;

                    Intent takePictureIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        progressbar_store_photo.setVisibility(View.VISIBLE);
                        startActivityForResult(takePictureIntent, 1000);
                }
                    else{
                        progressbar_store_photo.setVisibility(View.GONE);
                    }
            }
        });
        //upload store front photo

        //upload store owner pancard

        btn_upload_owner_pancard_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final int REQUEST_IMAGE_CAPTURE = 1;

                Intent takePictureIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 2000);
                    progressBar_store_pancard.setVisibility(View.VISIBLE);
                }
                else{
                    progressBar_store_pancard.setVisibility(View.GONE);
                }
            }
        });
        //upload store owner pancard

        // upload store personal details to cloud
        btn_submit_for_new_seller_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new_seller_documentUpload_LL.setVisibility(View.GONE);
                fill_up_the_form_tv.setVisibility(View.GONE);
                last_page_for_newseller.setVisibility(View.VISIBLE);
                existing_seller_all_item_LL.setVisibility(View.GONE);
                existing_seller_addItem_LL.setVisibility(View.GONE);
                existing_seller_profile_LL.setVisibility(View.GONE);
                existing_seller_loginpage_LL.setVisibility(View.GONE);
                last_page_for_newseller.setVisibility(View.GONE);
                new_seller_documentUpload_LL.setVisibility(View.GONE);
                new_seller_LL.setVisibility(View.GONE);
                LL1.setVisibility(View.GONE);

            }
        });
        // upload store personal details to cloud

        // create new-seller with email and password when submit button is clicked
        btn_submit_for_new_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = store_Email_address.getEditText().getText().toString().trim();
                final String password = store_password.getEditText().getText().toString();
                final String store_number = store_phone_number.getEditText().getText().toString();
                final String user_ID = mAuth.getCurrentUser().getUid();

                if (store_name.getEditText().getText().toString().equals("")) {
                    store_name.setError("!");
                    store_name.requestFocus();
                }
                if (store_phone_number.getEditText().getText().toString().equals("")) {
                    store_phone_number.setError("!");
                    store_phone_number.requestFocus();
                }
                if (store_pincode.getEditText().getText().toString().equals("")) {
                    store_pincode.setError("!");
                    store_pincode.requestFocus();
                }

                System.out.println("1");
                if (!store_Email_address.getEditText().getText().toString().equals("") && !store_password.getEditText().getText().toString().equals("")&& !store_pincode.getEditText().getText().toString().equals("")&& !store_name.getEditText().getText().toString().equals("")&& !store_owner_name.getEditText().getText().toString().equals("")&& !store_location.getEditText().getText().toString().equals("")) {
                    System.out.println("2");
                    progressBar_newseller.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Home.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                System.out.println("3");
                                //Toast.makeText(Home.this, "user created", Toast.LENGTH_SHORT).show();

                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                String store_email = currentUser.getEmail();

                                //write seller personal information into the database

/*
                                DocumentReference documentReference = fstore.collection("Seller and store personal data").document(UID);
                                Map<String,String> user = new HashMap<>();
                                user.put("store name" ,store_name.getEditText().getText().toString().trim());
                                user.put("store owner name" ,store_owner_name.getEditText().getText().toString().trim());
                                user.put("store email address" ,store_Email_address.getEditText().getText().toString().trim());
                                user.put("store Location" ,store_location.getEditText().getText().toString().trim());
                                user.put("store contact number" ,store_phone_number.getEditText().getText().toString());
                                user.put("store pincode" ,store_pincode.getEditText().getText().toString().trim());

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Home.this, "user created", Toast.LENGTH_SHORT).show();
                                    }
                                });
*/

                                Map<String,String> user = new HashMap<>();
                                user.put("user-ID",user_ID);
                                user.put("store name" ,store_name.getEditText().getText().toString().trim());
                                user.put("store owner name" ,store_owner_name.getEditText().getText().toString().trim());
                                user.put("store email address" ,store_Email_address.getEditText().getText().toString().trim());
                                user.put("store Location" ,store_location.getEditText().getText().toString().trim());
                                user.put("store contact number" ,store_phone_number.getEditText().getText().toString());
                                user.put("store pincode" ,store_pincode.getEditText().getText().toString().trim());

                                fstore.collection("Seller and store personal data").document(store_email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        new_seller_LL.setVisibility(View.INVISIBLE);
                                        new_seller_documentUpload_LL.setVisibility(View.VISIBLE);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar_newseller.setVisibility(View.GONE);
                                        Toast.makeText(Home.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //write seller personal information into the database

                                //email-link verification
                                currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Home.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                            progressBar_newseller.setVisibility(View.GONE);
                                        }
                                        else {
                                            Toast.makeText(Home.this, "Provide valid email address", Toast.LENGTH_SHORT).show();
                                            progressBar_newseller.setVisibility(View.GONE);
                                        }
                                    }
                                });
                                //email-link verification
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                progressBar_newseller.setVisibility(View.GONE);
                                Toast.makeText(Home.this, "Email Already exist",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    System.out.println("5");
                }
            }
        });

        //create new-seller with email and password when submit button is clicked
//FINISH (NEW SELLER REGISTRATION)
        // Pressing Home button start
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this,Home.class);
                startActivity(i);
            }
        });
        // Pressing Home button start


        // Pressing Go button start
        btn_Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Home.this,
                        "Your Location is Set to : " + String.valueOf(district.getSelectedItem()),Toast.LENGTH_SHORT).show();
                System.out.println(district.getSelectedItem());
                if (district.getSelectedItemId() == 0)
                {
                    System.out.println("Yes it is ok");
                    tv_Zafir.setVisibility(View.VISIBLE);
                    tv_Not_available.setVisibility(View.INVISIBLE);

                }
                else {
                    System.out.println("No it is NO");
                    tv_Not_available.setVisibility(View.VISIBLE);
                    tv_Zafir.setVisibility(View.INVISIBLE);
                }
            }
        });// Pressing Go button start

        //recycler view for existing seller add item -0
if(mAuth != null){
    String existing_seller_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    Query query = fstore
            .collection("Seller and store personal data")
            .document(existing_seller_email).collection("store all Item");
    FirestoreRecyclerOptions<model_add_item_existingseller> options = new FirestoreRecyclerOptions.Builder<model_add_item_existingseller>()
            .setQuery(query, model_add_item_existingseller.class)
            .build();


    myAdapter = new adapter_additem_existingseller(options);
    myrecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    myrecyclerView.setLayoutManager(layoutManager);
    myrecyclerView.setAdapter(myAdapter);
    //recycler view for existing seller add item -c

    //set username for currentuser -0
    TextInputLayout seller_email = (TextInputLayout) findViewById(R.id.existing_seller_email_ET);
    TextInputLayout seller_password = (TextInputLayout) findViewById(R.id.existing_seller_password_ET);
    TextInputLayout existing_seller_pin = (TextInputLayout)findViewById(R.id.existing_seller_pin_ET);
    seller_email.getEditText().setText("islammushtaq@gmail.com");
    seller_password.getEditText().setText("qwerty1234");
    existing_seller_pin.getEditText().setText("0000");
    //set username for currentuser -1
}

    }


    // Pressing spinner button start
    private void addListenerOnSpinnerItemSelection() {
        district = (Spinner) findViewById(R.id.district);
        district.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }// finish

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.btn_back:
                if(existing_seller_loginpage_LL.getVisibility() == View.VISIBLE){
                    existing_seller_loginpage_LL.setVisibility(View.GONE);
                    LL1.setVisibility(View.VISIBLE);
                }

                else if(existing_seller_profile_LL.getVisibility() == View.VISIBLE){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setTitle("Are You sure");
                    builder.setMessage("Log-out from this account");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            existing_seller_loginpage_LL.setVisibility(View.VISIBLE);
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }

                else if(existing_seller_all_item_LL.getVisibility() == View.VISIBLE){
                    existing_seller_profile_LL.setVisibility(View.VISIBLE);
                    existing_seller_all_item_LL.setVisibility(View.GONE);
                }
                else if(existing_seller_addItem_LL.getVisibility()==View.VISIBLE){
                    Toast.makeText(this, "Press cancel to exit", Toast.LENGTH_SHORT).show();
                }

                else if(last_page_for_newseller.getVisibility()==View.VISIBLE){
                    last_page_for_newseller.setVisibility(View.GONE);
                    LL1.setVisibility(View.VISIBLE);
                }

                return true;



            case R.id.administer:
                // when administer button is clicked
                final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
                builder.setTitle("Password");

                // Set up the input
                final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString().trim();
                // administer password
                        if (m_Text.equals("mushtaq")){
                            Toast.makeText(Home.this, m_Text, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Home.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                        //finish
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            // finish

            // code for new seller
            case R.id.new_seller:
                location_LL.setVisibility(View.GONE);
                fill_up_the_form_tv.setVisibility(View.VISIBLE);
                new_seller_LL.setVisibility(View.VISIBLE);
                LL1.setVisibility(View.VISIBLE);
                existing_seller_loginpage_LL.setVisibility(View.GONE);
                top_heading_image.setImageResource(R.drawable.new_seller);

                return true;
            // finish
            case R.id.existing_seller:
                //when existing seller is clicked
                LL1.setVisibility(View.GONE);
                last_page_for_newseller.setVisibility(View.GONE);
                new_seller_documentUpload_LL.setVisibility(View.GONE);
                new_seller_LL.setVisibility(View.GONE);
                existing_seller_loginpage_LL.setVisibility(View.VISIBLE);

                final TextInputLayout existing_seller_email = (TextInputLayout) findViewById(R.id.existing_seller_email_ET);
                final TextInputLayout existing_seller_password = (TextInputLayout)findViewById(R.id.existing_seller_password_ET);
                final TextInputLayout existing_seller_pin = (TextInputLayout)findViewById(R.id.existing_seller_pin_ET);
                Button btn_existing_seller_forgot = (Button)findViewById(R.id.btn_existing_seller_forgot);
                Button btn_existing_seller_cancel = (Button)findViewById(R.id.btn_existing_seller_cancel);
                Button btn_existing_seller_ok = (Button)findViewById(R.id.btn_existing_seller_ok);
                final ProgressBar existing_seller_progressbar = (ProgressBar)findViewById(R.id.existing_seller_progressbar);

                    //when cancel btn is pressed
                btn_existing_seller_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LL1.setVisibility(View.VISIBLE);
                        existing_seller_loginpage_LL.setVisibility(View.GONE);
                    }
                });
                    //when cancel btn is pressed

                    //when Ok Button is pressed

                btn_existing_seller_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String email = existing_seller_email.getEditText().getText().toString().trim();
                        final String password = existing_seller_password.getEditText().getText().toString().trim();
                        final String pin = existing_seller_pin.getEditText().getText().toString().trim();

                        if (existing_seller_email.getEditText().getText().toString().equals("")){
                            existing_seller_email.setError("!");
                        }
                        if (existing_seller_password.getEditText().getText().toString().equals("")){
                            existing_seller_password.setError("!");
                        }
                        if (existing_seller_pin.getEditText().getText().toString().equals("")){
                            existing_seller_pin.setError("!");
                        }
                        if (!existing_seller_email.getEditText().getText().toString().trim().equals("") && !existing_seller_password.getEditText().getText().toString().trim().equals("") && existing_seller_pin.getEditText().getText().toString().trim().equals("0000")){
                            existing_seller_progressbar.setVisibility(View.VISIBLE);
                            mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {


                                    FirebaseUser currentuser = mAuth.getCurrentUser();
                                    //email-link verification
                                    if(currentuser.isEmailVerified()){
                                        //creating layout existing user profile
                                        final LinearLayout existing_seller_profile_LL = (LinearLayout) findViewById(R.id.existing_seller_profile_LL);
                                        existing_seller_progressbar.setVisibility(View.GONE);
                                        existing_seller_loginpage_LL.setVisibility(View.GONE);
                                        existing_seller_profile_LL.setVisibility(View.VISIBLE);
                                        LL1.setVisibility(View.GONE);
                                        final ProgressBar progressBar_existing_seller_store_photo = (ProgressBar)findViewById(R.id.progressBar_existing_seller_store_photo);
                                        progressBar_existing_seller_store_photo.setVisibility(View.VISIBLE);

                                        //existing seller other details
                                        final TextView tv_existing_seller_store_name = (TextView)findViewById(R.id.tv_existing_seller_store_name);
                                        final TextView tv_existing_seller_store_owner_name = (TextView)findViewById(R.id.tv_existing_seller_store_owner_name);
                                        final TextView tv_existing_seller_location = (TextView)findViewById(R.id.tv_existing_seller_location);
                                        final TextView tv_existing_seller_phone = (TextView)findViewById(R.id.tv_existing_seller_phone);
                                        final TextView tv_existing_seller_pancard = (TextView)findViewById(R.id.tv_existing_seller_pancard);
                                        final TextView tv_existing_seller_pincode = (TextView)findViewById(R.id.tv_existing_seller_pincode);

                                        Button btn_edit_existing_seller_store_name = (Button) findViewById(R.id.btn_edit_existing_seller_store_name);
                                        Button btn_edit_existing_seller_store_owner_name = (Button) findViewById(R.id.btn_edit_existing_seller_store_owner_name);
                                        Button btn_edit_existing_seller_location = (Button) findViewById(R.id.btn_edit_existing_seller_location);
                                        Button btn_edit_existing_seller_phone = (Button) findViewById(R.id.btn_edit_existing_seller_phone);
                                        Button btn_existing_seller_password_change = (Button) findViewById(R.id.btn_existing_seller_password_change);
                                        Button btn_existing_seller_pin_change = (Button) findViewById(R.id.btn_existing_seller_pin_change);
                                        Button btn_existing_seller_log_out = (Button) findViewById(R.id.btn_existing_seller_log_out);
                                        final Button btn_edit_existing_seller_information = (Button) findViewById(R.id.btn_edit_existing_seller_information);
                                        final Button btn_edit_existing_seller_information2 = (Button) findViewById(R.id.btn_edit_existing_seller_information2);
                                        final CardView cv_existing_seller_information = (CardView)findViewById(R.id.cv_existing_seller_information);
                                        btn_edit_existing_seller_information.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                cv_existing_seller_information.setVisibility(View.VISIBLE);
                                                btn_edit_existing_seller_information2.setVisibility(View.VISIBLE);
                                                btn_edit_existing_seller_information.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                        btn_edit_existing_seller_information2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                cv_existing_seller_information.setVisibility(View.GONE);
                                                btn_edit_existing_seller_information2.setVisibility(View.INVISIBLE);
                                                btn_edit_existing_seller_information.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        final String current_user_email = mAuth.getCurrentUser().getEmail();
                                        DocumentReference documentReference = fstore.collection("Seller and store personal data").document(current_user_email);
                                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                           tv_existing_seller_store_name.setText(value.getString("store name"));
                                           tv_existing_seller_store_owner_name.setText("owner name:-" + " "+value.getString("store owner name"));
                                           tv_existing_seller_phone.setText("store contact number:-" + " "+value.getString("store contact number"));
                                           tv_existing_seller_location.setText("store Location:-" + " "+value.getString("store Location"));
                                           tv_existing_seller_pancard.setText("Pancard");//+value.getString("store name"));
                                           tv_existing_seller_pincode.setText("store pincode:-" + " "+value.getString("store pincode"));
                                            }
                                        });
                                        // when existing seller logout button is pressed
                                        btn_existing_seller_log_out.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mAuth.signOut();
                                                existing_seller_profile_LL.setVisibility(View.GONE);
                                                existing_seller_loginpage_LL.setVisibility(View.VISIBLE);
                                                last_page_for_newseller.setVisibility(View.GONE);
                                                new_seller_documentUpload_LL.setVisibility(View.GONE);
                                                new_seller_LL.setVisibility(View.GONE);
                                                LL1.setVisibility(View.GONE);

                                            }
                                        });
                                        // when existing seller logout button is pressed
                                        //existing seller other details
                                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Seller and store personal data/" + current_user_email +"/store image.jpg");
                                        final ImageView existing_seller_store_photo_iv = (ImageView)findViewById(R.id.existing_seller_store_photo_iv);
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Picasso.get().load(uri).fit().into(existing_seller_store_photo_iv);
                                                progressBar_existing_seller_store_photo.setVisibility(View.GONE);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Home.this, "url failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        //timepicker
                                        final TextView tvw=(TextView)findViewById(R.id.textView1);
                                        final EditText storeopentime=(EditText) findViewById(R.id.existingseller_store_open_time);
                                        final EditText storeclosetime = (EditText) findViewById(R.id.existingseller_store_close_time);
                                        //storeopentime.setInputType(InputType.TYPE_NULL);
                                        storeopentime.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final Calendar cldr = Calendar.getInstance();
                                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                                int minutes = cldr.get(Calendar.MINUTE);
                                                // time picker dialog
                                                picker = new TimePickerDialog(Home.this,
                                                        new TimePickerDialog.OnTimeSetListener() {
                                                            @Override
                                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                                                storeopentime.setText(sHour + ":"+ sMinute);
                                                                Map<String,String> user = new HashMap<>();
                                                                user.put("open time" ,storeopentime.getText().toString());
                                                                fstore.collection("Seller and store personal data").document(current_user_email).collection("store time").document("open time").set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(Home.this, "done", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }, hour, minutes, true);
                                                picker.show();
                                            }
                                        });
                                        storeclosetime.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final Calendar cldr = Calendar.getInstance();
                                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                                int minutes = cldr.get(Calendar.MINUTE);
                                                // time picker dialog
                                                picker = new TimePickerDialog(Home.this,
                                                        new TimePickerDialog.OnTimeSetListener() {
                                                            @Override
                                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                                                storeclosetime.setText(sHour + ":"+ sMinute);
                                                                Map<String,String> userclosetime = new HashMap<>();
                                                                userclosetime.put("close time" ,storeclosetime.getText().toString());
                                                                fstore.collection("Seller and store personal data").document(current_user_email).collection("store time").document("close time").set(userclosetime).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(Home.this, "done", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }, hour, minutes, true);
                                                picker.show();
                                            }
                                        });
                                        //timepicker
                                        //creating layout existing user profile
                                    }
                                    else{
                                        existing_seller_email.setError("PLease verify your email");
                                        existing_seller_email.requestFocus();
                                        existing_seller_progressbar.setVisibility(View.GONE);
                                    }
                                    //email-link verification

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    existing_seller_progressbar.setVisibility(View.GONE);
                                    Toast.makeText(Home.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        //when existing seller add item is clicked
                        ImageView existing_seller_Add_item_iv = (ImageView) findViewById(R.id.existing_seller_Add_item_iv);
                        existing_seller_Add_item_iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final ScrollView existing_seller_addItem_LL = (ScrollView) findViewById(R.id.existing_seller_addItem_LL);
                                final LinearLayout existing_seller_profile_LL = (LinearLayout) findViewById(R.id.existing_seller_profile_LL);

                                AlertDialog.Builder item_name_builder = new AlertDialog.Builder(Home.this,R.style.AlertDialogTheme);
                                item_name_builder.setTitle("Item Name");

                                // Set up the input
                                final EditText item_name = new EditText(Home.this);
                                item_name.setInputType(InputType.TYPE_CLASS_TEXT );
                                item_name_builder.setView(item_name);

                                item_name_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String m_Text = item_name.getText().toString().trim();
                                        final EditText existing_seller_item_name = (EditText) findViewById(R.id.existing_seller_item_name);
                                        existing_seller_item_name.setText(m_Text);

                                        existing_seller_addItem_LL.setVisibility(View.VISIBLE);
                                        existing_seller_profile_LL.setVisibility(View.GONE);
                                        existing_seller_loginpage_LL.setVisibility(View.GONE);
                                        last_page_for_newseller.setVisibility(View.GONE);
                                        new_seller_documentUpload_LL.setVisibility(View.GONE);
                                        existing_seller_all_item_LL.setVisibility(View.GONE);
                                        new_seller_LL.setVisibility(View.GONE);
                                        LL1.setVisibility(View.GONE);


                                    }
                                });
                                item_name_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                        existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                        existing_seller_addItem_LL.setVisibility(View.GONE);
                                        existing_seller_profile_LL.setVisibility(View.GONE);
                                        existing_seller_loginpage_LL.setVisibility(View.GONE);
                                        last_page_for_newseller.setVisibility(View.GONE);
                                        new_seller_documentUpload_LL.setVisibility(View.GONE);
                                        new_seller_LL.setVisibility(View.GONE);
                                        LL1.setVisibility(View.GONE);
                                    }
                                });
                                item_name_builder.show();
                                final ImageView item_imageView1 = (ImageView) findViewById(R.id.item_imageView1);
                                final ImageView item_imageView2 = (ImageView) findViewById(R.id.item_imageView2);
                                final ImageView item_imageView3 = (ImageView) findViewById(R.id.item_imageView3);
                                final ImageView item_imageView4 = (ImageView) findViewById(R.id.item_imageView4);

                                item_imageView1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent takePictureIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivityForResult(takePictureIntent, 3000);
                                        }
                                    }
                                });

                                item_imageView2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent takePictureIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivityForResult(takePictureIntent, 3001);
                                        }
                                    }
                                });

                                item_imageView3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent takePictureIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivityForResult(takePictureIntent, 3002);
                                        }
                                    }
                                });

                                item_imageView4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent takePictureIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivityForResult(takePictureIntent, 3003);
                                        }
                                    }
                                });

                                //when existing seller upload item image is clicked
                                //when existing seller add item ok button is clicked
                                Button btn_existing_seller_add_item_ok = (Button) findViewById(R.id.btn_existing_seller_add_item_ok);
                                btn_existing_seller_add_item_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String current_user_email = mAuth.getCurrentUser().getEmail();
                                        EditText existing_seller_item_name = (EditText) findViewById(R.id.existing_seller_item_name);
                                        TextInputLayout existing_seller_item_description = findViewById(R.id.existing_seller_item_description);
                                        TextInputLayout existing_seller_item_cook_time = findViewById(R.id.existing_seller_item_cook_time);
                                        TextInputLayout existing_seller_item_price = findViewById(R.id.existing_seller_item_price);
                                        Spinner veg_spinner = (Spinner) findViewById(R.id.veg_spinner);
                                        Spinner food_category = (Spinner) findViewById(R.id.food_category);

                                        String item_name = existing_seller_item_name.getText().toString().trim();
                                        if (veg_spinner.getSelectedItemId() == 0 && food_category.getSelectedItemId() == 0){
                                            Map<String,String> user = new HashMap<>();
                                            user.put("item_name" ,existing_seller_item_name.getText().toString());
                                            user.put("item_description",existing_seller_item_description.getEditText().getText().toString().trim());
                                            user.put("item_cooktime",existing_seller_item_cook_time.getEditText().getText().toString().trim());
                                            user.put("Item_Price",existing_seller_item_price.getEditText().getText().toString().trim());
                                            user.put("Type","Veg");
                                            user.put("category","Drinks");

                                            fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item")
                                                    .document(item_name).set(user,SetOptions.merge())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Home.this, "done", Toast.LENGTH_SHORT).show();
                                                    LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                                    existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                                    existing_seller_addItem_LL.setVisibility(View.GONE);
                                                    existing_seller_profile_LL.setVisibility(View.GONE);
                                                    existing_seller_loginpage_LL.setVisibility(View.GONE);
                                                    last_page_for_newseller.setVisibility(View.GONE);
                                                    new_seller_documentUpload_LL.setVisibility(View.GONE);
                                                    new_seller_LL.setVisibility(View.GONE);
                                                    LL1.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                        else if(veg_spinner.getSelectedItemId() == 0 && food_category.getSelectedItemId() == 1){
                                            Map<String,String> user = new HashMap<>();
                                            user.put("item_name" ,existing_seller_item_name.getText().toString());
                                            user.put("item_description",existing_seller_item_description.getEditText().getText().toString().trim());
                                            user.put("item_cooktime",existing_seller_item_cook_time.getEditText().getText().toString().trim());
                                            user.put("Item_Price",existing_seller_item_price.getEditText().getText().toString().trim());
                                            user.put("Type","Veg");
                                            user.put("category","Fast Food");

                                            fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Home.this, "done", Toast.LENGTH_SHORT).show();
                                                    LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                                    existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                                    existing_seller_addItem_LL.setVisibility(View.GONE);
                                                    existing_seller_profile_LL.setVisibility(View.GONE);
                                                    existing_seller_loginpage_LL.setVisibility(View.GONE);
                                                    last_page_for_newseller.setVisibility(View.GONE);
                                                    new_seller_documentUpload_LL.setVisibility(View.GONE);
                                                    new_seller_LL.setVisibility(View.GONE);
                                                    LL1.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                        else if(veg_spinner.getSelectedItemId() == 0 && food_category.getSelectedItemId() == 2){
                                            Map<String,String> user = new HashMap<>();
                                            user.put("item_name" ,existing_seller_item_name.getText().toString());
                                            user.put("item_description",existing_seller_item_description.getEditText().getText().toString().trim());
                                            user.put("item_cooktime",existing_seller_item_cook_time.getEditText().getText().toString().trim());
                                            user.put("Item_Price",existing_seller_item_price.getEditText().getText().toString().trim());
                                            user.put("Type","Veg");
                                            user.put("category","Sweets");

                                            fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Home.this, "done", Toast.LENGTH_SHORT).show();
                                                    LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                                    existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                                    existing_seller_addItem_LL.setVisibility(View.GONE);
                                                    existing_seller_profile_LL.setVisibility(View.GONE);
                                                    existing_seller_loginpage_LL.setVisibility(View.GONE);
                                                    last_page_for_newseller.setVisibility(View.GONE);
                                                    new_seller_documentUpload_LL.setVisibility(View.GONE);
                                                    new_seller_LL.setVisibility(View.GONE);
                                                    LL1.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                        else if(veg_spinner.getSelectedItemId() == 0 && food_category.getSelectedItemId() == 3){
                                            Map<String,String> user = new HashMap<>();
                                            user.put("item_name" ,existing_seller_item_name.getText().toString());
                                            user.put("item_description",existing_seller_item_description.getEditText().getText().toString().trim());
                                            user.put("item_cooktime",existing_seller_item_cook_time.getEditText().getText().toString().trim());
                                            user.put("Item_Price",existing_seller_item_price.getEditText().getText().toString().trim());
                                            user.put("Type","Veg");
                                            user.put("category","Bakery");

                                            fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Home.this, "done", Toast.LENGTH_SHORT).show();
                                                    LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                                    existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                                    existing_seller_addItem_LL.setVisibility(View.GONE);
                                                    existing_seller_profile_LL.setVisibility(View.GONE);
                                                    existing_seller_loginpage_LL.setVisibility(View.GONE);
                                                    last_page_for_newseller.setVisibility(View.GONE);
                                                    new_seller_documentUpload_LL.setVisibility(View.GONE);
                                                    new_seller_LL.setVisibility(View.GONE);
                                                    LL1.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                        else if(veg_spinner.getSelectedItemId() == 1 && food_category.getSelectedItemId() == 0){
                                            Map<String,String> user = new HashMap<>();
                                            user.put("item_name" ,existing_seller_item_name.getText().toString());
                                            user.put("item_description",existing_seller_item_description.getEditText().getText().toString().trim());
                                            user.put("item_cooktime",existing_seller_item_cook_time.getEditText().getText().toString().trim());
                                            user.put("Item_Price",existing_seller_item_price.getEditText().getText().toString().trim());
                                            user.put("Type","Non-Veg");
                                            user.put("category","Drinks");

                                            fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Home.this, "done", Toast.LENGTH_SHORT).show();
                                                    LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                                    existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                                    existing_seller_addItem_LL.setVisibility(View.GONE);
                                                    existing_seller_profile_LL.setVisibility(View.GONE);
                                                    existing_seller_loginpage_LL.setVisibility(View.GONE);
                                                    last_page_for_newseller.setVisibility(View.GONE);
                                                    new_seller_documentUpload_LL.setVisibility(View.GONE);
                                                    new_seller_LL.setVisibility(View.GONE);
                                                    LL1.setVisibility(View.GONE);
                                                }
                                            });
                                        }

                                        else if(veg_spinner.getSelectedItemId() == 1 && food_category.getSelectedItemId() == 1){
                                            Map<String,String> user = new HashMap<>();
                                            user.put("item_name" ,existing_seller_item_name.getText().toString());
                                            user.put("item_description",existing_seller_item_description.getEditText().getText().toString().trim());
                                            user.put("item_cooktime",existing_seller_item_cook_time.getEditText().getText().toString().trim());
                                            user.put("Item_Price",existing_seller_item_price.getEditText().getText().toString().trim());
                                            user.put("Type","Non-Veg");
                                            user.put("category","Fast Food");

                                            fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Home.this, "done", Toast.LENGTH_SHORT).show();
                                                    LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                                    existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                                    existing_seller_addItem_LL.setVisibility(View.GONE);
                                                    existing_seller_profile_LL.setVisibility(View.GONE);
                                                    existing_seller_loginpage_LL.setVisibility(View.GONE);
                                                    last_page_for_newseller.setVisibility(View.GONE);
                                                    new_seller_documentUpload_LL.setVisibility(View.GONE);
                                                    new_seller_LL.setVisibility(View.GONE);
                                                    LL1.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                        else if(veg_spinner.getSelectedItemId() == 1 && food_category.getSelectedItemId() == 2){
                                            Map<String,String> user = new HashMap<>();
                                            user.put("item_name" ,existing_seller_item_name.getText().toString());
                                            user.put("item_description",existing_seller_item_description.getEditText().getText().toString().trim());
                                            user.put("item_cooktime",existing_seller_item_cook_time.getEditText().getText().toString().trim());
                                            user.put("Item_Price",existing_seller_item_price.getEditText().getText().toString().trim());
                                            user.put("Type","Non-Veg");
                                            user.put("category","Sweets");

                                            fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Home.this, "done", Toast.LENGTH_SHORT).show();
                                                    LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                                    existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                                    existing_seller_addItem_LL.setVisibility(View.GONE);
                                                    existing_seller_profile_LL.setVisibility(View.GONE);
                                                    existing_seller_loginpage_LL.setVisibility(View.GONE);
                                                    last_page_for_newseller.setVisibility(View.GONE);
                                                    new_seller_documentUpload_LL.setVisibility(View.GONE);
                                                    new_seller_LL.setVisibility(View.GONE);
                                                    LL1.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                        else if(veg_spinner.getSelectedItemId() == 1 && food_category.getSelectedItemId() == 3){
                                            Map<String,String> user = new HashMap<>();
                                            user.put("item_name" ,existing_seller_item_name.getText().toString());
                                            user.put("item_description",existing_seller_item_description.getEditText().getText().toString().trim());
                                            user.put("item_cooktime",existing_seller_item_cook_time.getEditText().getText().toString().trim());
                                            user.put("Item_Price",existing_seller_item_price.getEditText().getText().toString().trim());
                                            user.put("Type","Non-Veg");
                                            user.put("category","Bakery");

                                            fstore.collection("Seller and store personal data")
                                                    .document(current_user_email).collection("store all Item")
                                                    .document(item_name).set(user,SetOptions.merge())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Home.this, "ITEM added", Toast.LENGTH_SHORT).show();
                                                    LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                                    existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                                    existing_seller_addItem_LL.setVisibility(View.GONE);
                                                    existing_seller_profile_LL.setVisibility(View.GONE);
                                                    existing_seller_loginpage_LL.setVisibility(View.GONE);
                                                    last_page_for_newseller.setVisibility(View.GONE);
                                                    new_seller_documentUpload_LL.setVisibility(View.GONE);
                                                    new_seller_LL.setVisibility(View.GONE);
                                                    LL1.setVisibility(View.GONE);
                                                }
                                            });
                                        }

                                        else{
                                            Toast.makeText(Home.this, "Fill select above options)", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                //when existing seller add item ok button is clicked

                                //when existing seller add item cancel button is clicked
                                Button btn_existing_seller_add_item_cancel = (Button) findViewById(R.id.btn_existing_seller_add_item_cancel);
                                btn_existing_seller_add_item_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                        existing_seller_all_item_LL.setVisibility(View.GONE);
                                        existing_seller_addItem_LL.setVisibility(View.GONE);
                                        existing_seller_profile_LL.setVisibility(View.VISIBLE);
                                        existing_seller_loginpage_LL.setVisibility(View.GONE);
                                        last_page_for_newseller.setVisibility(View.GONE);
                                        new_seller_documentUpload_LL.setVisibility(View.GONE);
                                        new_seller_LL.setVisibility(View.GONE);
                                        LL1.setVisibility(View.GONE);
                                    }
                                });
                                //when existing seller add item ok button is clicked
                            }
                        });
                        //when existing seller add item is clicked

                    }
                });
                    //when Ok Button is pressed

                //when existing seller is clicked
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.administer_menu,menu);
//search -0
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(existing_seller_all_item_LL.getVisibility()==View.VISIBLE){
                    startsearch(s);
                }
                else {
                    Toast.makeText(Home.this, "empty search", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(existing_seller_all_item_LL.getVisibility()==View.VISIBLE){
                    startsearch(s);
                }
                else {
                    Toast.makeText(Home.this, "empty search", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        //search -1
        return super.onCreateOptionsMenu(menu);
    }

    private void startsearch(String s) {
        String existing_seller_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Query query1 = fstore
                .collection("Seller and store personal data")
                .document(existing_seller_email).collection("store all Item");

        FirestoreRecyclerOptions<model_add_item_existingseller> options =
        new FirestoreRecyclerOptions.Builder<model_add_item_existingseller>()
                .setQuery(query1.orderBy("item_name").startAt(s).endAt(s +"\uf8ff"),
                        model_add_item_existingseller.class)
                .build();

        myAdapter = new adapter_additem_existingseller(options);
        myAdapter.startListening();
        myrecyclerView.setAdapter(myAdapter);
    }
}