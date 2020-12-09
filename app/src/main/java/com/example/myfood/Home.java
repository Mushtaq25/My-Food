package com.example.myfood;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
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
import android.widget.FrameLayout;
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
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.Random;


public class Home extends AppCompatActivity {
    ImageButton btn_Go;
    TextInputLayout store_name,store_owner_name,store_Email_address,store_location,store_phone_number,store_password,store_pincode;
    ImageView top_heading_image,iv_store_photo,iv_store_owner_pancard;
    TextView tv_Not_available,tv_Zafir,fill_up_the_form_tv;
    Spinner district;
    Button btn_submit_for_new_seller,btn_submit_for_new_seller_documents,btn_upload_store_photo,btn_upload_owner_pancard_photo;
    LinearLayout LL1,location_LL,new_seller_LL,new_seller_documentUpload_LL,last_page_for_newseller;
    ScrollView existing_seller_loginpage_LL;
    ProgressBar progressBar_newseller,progressBar_store_pancard,progressbar_store_photo;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseFirestore fstore;
    StorageReference mStorageRef;
    adapter_additem_existingseller myAdapter;
    FirebaseAuth.AuthStateListener mAuthListener;
    ArrayList<String> all_Store_ID = new ArrayList<String>();

    BottomNavigationView bottom_bar;

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

    }




    //upload seller store photo
    private void upload_store_imageTO_firebase(Uri imageuri) {
        if (mAuth.getCurrentUser() != null) {
            final String email = mAuth.getCurrentUser().getEmail();
            final StorageReference filename = mStorageRef.child("Seller and store personal data/" + email + "/store image.jpg");
            filename.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).fit().into(iv_store_photo);
                            progressbar_store_photo.setVisibility(View.GONE);

                            String item_image_url = String.valueOf(uri);
                            Map<String,String> user = new HashMap<>();
                            user.put("store_photo_link",item_image_url);
                            fstore.collection("Seller and store personal data")
                                    .document(email).set(user,SetOptions.merge());
                        }
                    });
                }
            });
        }
    }
    //upload seller store photo
//upload seller pancard photo -0
    private void upload_pancard_imageTO_firebase(Uri imageuri) {
        if (mAuth.getCurrentUser() != null) {
            String email = mAuth.getCurrentUser().getEmail();
            final StorageReference filename = mStorageRef.child("Seller and store personal data/" + email + "/store pancard.jpg");
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
        }
    }// upload seller pancard photo -1

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);

    }
    //private  FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private  CollectionReference collectionReference = db.collection("Seller and store personal data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        addListenerOnSpinnerItemSelection();
        bottom_bar = findViewById(R.id.bottom_bar);

        //setActionBar()
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.mytoolbar);
        // finish

//bottom bar -0

        //getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new fragment_home()).commit();
        bottom_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment temp = null;
                switch (item.getItemId()){
                    case R.id.home :
                        temp =new fragment_home();
                        break;
                    case R.id.explore :
                        temp =new fragment_explore();
                        break;
                    case R.id.carts :
                        temp =new fragment_shopping_cart();
                        break;
                    case R.id.user_account :
                        temp =new fragment_user_account();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,temp)
                        .commit();

                return true;
            }
        });
        //bottom bar -1

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

                if (!store_Email_address.getEditText().getText().toString().equals("") && !store_password.getEditText().getText().toString().equals("")&& !store_pincode.getEditText().getText().toString().equals("")&& !store_name.getEditText().getText().toString().equals("")&& !store_owner_name.getEditText().getText().toString().equals("")&& !store_location.getEditText().getText().toString().equals("")) {

                    progressBar_newseller.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Home.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(Home.this, "user created", Toast.LENGTH_SHORT).show();

                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                String store_email = currentUser.getEmail();

                                //write seller personal information into the database
                                String Store_ID = StoreID();
                                Map<String,String> user = new HashMap<>();
                                user.put("user-ID",user_ID);
                                user.put("store name" ,store_name.getEditText().getText().toString().trim());
                                user.put("store owner name" ,store_owner_name.getEditText().getText().toString().trim());
                                user.put("store email address" ,store_Email_address.getEditText().getText().toString().trim());
                                user.put("store Location" ,store_location.getEditText().getText().toString().trim());
                                user.put("store contact number" ,store_phone_number.getEditText().getText().toString());
                                user.put("store pincode" ,store_pincode.getEditText().getText().toString().trim());
                                user.put("Store_ID",Store_ID);

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

            private String StoreID() {
                Random random = new Random();
                String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@$#%*123456789";
                String s = "";
                while(s.length() <= 15){
                    s = s + String.valueOf(alphabet.charAt(random.nextInt(alphabet.length())));
                }

                int i,count=0;
                for (i=0;i<=all_Store_ID.size();i++){
                    if(i == 0){
                        count = 0;
                        break;
                    }
                    if (all_Store_ID.get(i) == s){
                        count += 1;
                    }
                }
                if (count == 0){
                    all_Store_ID.add(s);
                }
                else{
                    StoreID();
                }
                return s;
            }
        });

        //create new-seller with email and password when submit button is clicked
//FINISH (NEW SELLER REGISTRATION)


        // Pressing Go button start
        btn_Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Home.this,
                        "Your Location is Set to : " + String.valueOf(district.getSelectedItem()),Toast.LENGTH_SHORT).show();
                System.out.println(district.getSelectedItem());
                if (district.getSelectedItemId() == 0)
                {
                    tv_Zafir.setVisibility(View.VISIBLE);
                    tv_Not_available.setVisibility(View.INVISIBLE);
                    /*Intent i  = new Intent(Home.this,Market.class);
                    startActivity(i);
                    */

                }
                else {
                    tv_Not_available.setVisibility(View.VISIBLE);
                    tv_Zafir.setVisibility(View.INVISIBLE);
                }
            }
        });// Pressing Go button start

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    System.out.println("User logged in");
                    TextInputLayout seller_email = (TextInputLayout) findViewById(R.id.existing_seller_email_ET);
                    TextInputLayout seller_password = (TextInputLayout) findViewById(R.id.existing_seller_password_ET);
                    TextInputLayout existing_seller_pin = (TextInputLayout) findViewById(R.id.existing_seller_pin_ET);
                    seller_email.getEditText().setText("islammushtaq@gmail.com");
                    seller_password.getEditText().setText("qwerty1234");
                    existing_seller_pin.getEditText().setText("0000");
                }
                else{
                    TextInputLayout seller_email = (TextInputLayout) findViewById(R.id.existing_seller_email_ET);
                    TextInputLayout seller_password = (TextInputLayout) findViewById(R.id.existing_seller_password_ET);
                    TextInputLayout existing_seller_pin = (TextInputLayout) findViewById(R.id.existing_seller_pin_ET);
                    seller_email.getEditText().setText("islammushtaq@gmail.com");
                    seller_password.getEditText().setText("qwerty1234");
                    existing_seller_pin.getEditText().setText("0000");
                    System.out.println("User not logged in");
                }
            }
        };



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
                                        Intent i = new Intent(getApplicationContext(),Existing_seller_profile.class);
                                        startActivity(i);
                                        //s
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

        return super.onCreateOptionsMenu(menu);
    }

}