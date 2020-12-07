package com.example.myfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Existing_seller_profile extends AppCompatActivity {

    TextInputLayout store_name,store_owner_name,store_Email_address,store_location,store_phone_number,store_password,store_pincode;
    ImageView top_heading_image,iv_store_photo,iv_store_owner_pancard;
    ExtendedFloatingActionButton btn_floating_additem;
    Button btn_submit_for_new_seller,btn_submit_for_new_seller_documents,btn_upload_store_photo,btn_upload_owner_pancard_photo;
    LinearLayout location_LL,existing_seller_profile_LL,existing_seller_all_item_LL;
    ScrollView existing_seller_addItem_LL;
    ProgressBar progressBar_newseller,progressBar_store_pancard,progressbar_store_photo;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseFirestore fstore;
    StorageReference mStorageRef;
    adapter_additem_existingseller myAdapter;
    ImageView existing_seller_Add_item_iv;
    TextView tvw,store_id,store_image_link,tv_existing_seller_store_name, tv_existing_seller_store_owner_name, tv_existing_seller_location, tv_existing_seller_phone, tv_existing_seller_pancard, tv_existing_seller_pincode ;
    EditText storeopentime,storeclosetime;
    ImageView existing_seller_store_photo_iv,item_imageView1,item_imageView2,item_imageView3,item_imageView4 ;
    SwitchMaterial display_store;
    ArrayList<String> all_Product_ID = new ArrayList<String>();
    RecyclerView myrecyclerView;

    TimePickerDialog picker;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

            final ProgressDialog progressDialog = new ProgressDialog(Existing_seller_profile.this);
            progressDialog.setMessage("Image uploading.........");

            final StorageReference filename = mStorageRef.child("Seller and store personal data/" + email + "/Store All item images/" + item_name + "/" + item);
            filename.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).fit().into(item_imageView);

                            String item_image_url = String.valueOf(uri);

                            Map<String, String> user2 = new HashMap<>();
                            user2.put("imagelink" + item, item_image_url);
                            fstore.collection("Seller and store personal data")
                                    .document(email).collection("store all Item")
                                    .document(item_name).set(user2, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "link not created", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.existing_seller_menu,menu);


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
                    Toast.makeText(Existing_seller_profile.this, "empty search", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(existing_seller_all_item_LL.getVisibility()==View.VISIBLE){
                    startsearch(s);
                }
                else {
                    Toast.makeText(Existing_seller_profile.this, "empty search", Toast.LENGTH_SHORT).show();
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
                .document("islammushtaq@gmail.com").collection("store all Item");

        FirestoreRecyclerOptions<model_add_item_existingseller> options =
                new FirestoreRecyclerOptions.Builder<model_add_item_existingseller>()
                        .setQuery(query1.orderBy("item_name").startAt(s).endAt(s +"\uf8ff"),
                                model_add_item_existingseller.class)
                        .build();

        myAdapter = new adapter_additem_existingseller(options);
        myAdapter.startListening();
        myrecyclerView.setAdapter(myAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case R.id.btn_back_Existingseller:
                if(existing_seller_profile_LL.getVisibility() == View.VISIBLE){
                AlertDialog.Builder builder = new AlertDialog.Builder(Existing_seller_profile.this);
                builder.setTitle("Are You sure");
                builder.setMessage("Log-out from this account");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i =new Intent(getApplicationContext(),Existing_seller_profile.class);
                        startActivity(i);
                        finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                builder.show();
            }
                else if(existing_seller_all_item_LL.getVisibility() == View.VISIBLE){
                    existing_seller_profile_LL.setVisibility(View.VISIBLE);
                    existing_seller_all_item_LL.setVisibility(View.GONE);
                }
                else if(existing_seller_addItem_LL.getVisibility()==View.VISIBLE){
                    Toast.makeText(this, "Press cancel to exit", Toast.LENGTH_SHORT).show();
                }
                return true;

            case  R.id.search:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_seller_profile);


        location_LL = findViewById(R.id.location_LL);
        store_name = findViewById(R.id.store_name);
        iv_store_photo = findViewById(R.id.iv_store_photo);
        iv_store_owner_pancard = findViewById(R.id.iv_store_owner_pancard);
        btn_upload_store_photo = findViewById(R.id.btn_upload_store_photo);
        btn_upload_owner_pancard_photo = findViewById(R.id.btn_upload_owner_pancard_photo);
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
        existing_seller_Add_item_iv = findViewById(R.id.existing_seller_Add_item_iv);
        tvw = findViewById(R.id.textView1);
        store_id = findViewById(R.id.store_id);
        storeopentime= findViewById(R.id.existingseller_store_open_time);
        storeclosetime = findViewById(R.id.existingseller_store_close_time);
        existing_seller_addItem_LL =  findViewById(R.id.existing_seller_addItem_LL);
        existing_seller_profile_LL =  findViewById(R.id.existing_seller_profile_LL);
        existing_seller_store_photo_iv = findViewById(R.id.existing_seller_store_photo_iv);
        item_imageView1 =  findViewById(R.id.item_imageView1);
        item_imageView2 =  findViewById(R.id.item_imageView2);
        item_imageView3 =  findViewById(R.id.item_imageView3);
        item_imageView4 =  findViewById(R.id.item_imageView4);
        btn_floating_additem = findViewById(R.id.btn_floating_additem);
        display_store = findViewById(R.id.display_store);
        store_image_link = findViewById(R.id.store_image_link);
        tv_existing_seller_store_name = findViewById(R.id.tv_existing_seller_store_name);
        tv_existing_seller_store_owner_name = findViewById(R.id.tv_existing_seller_store_owner_name);
        tv_existing_seller_location = findViewById(R.id.tv_existing_seller_location);
        tv_existing_seller_phone = findViewById(R.id.tv_existing_seller_phone);
        tv_existing_seller_pancard = findViewById(R.id.tv_existing_seller_pancard);
        tv_existing_seller_pincode = findViewById(R.id.tv_existing_seller_pincode);


        //setActionBar()
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.mytoolbar);
        // finish


        //Display your store to the market -0
        display_store.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    System.out.println("11");
                    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                    System.out.println("12");
                    String storeid = store_id.getText().toString();
                    System.out.println("14");
                    Map<String,String> user1 = new HashMap<>();
                    user1.put("store Location" ,tv_existing_seller_location.getText().toString().trim());
                    user1.put("store contact number",tv_existing_seller_phone.getText().toString().trim());
                    user1.put("store name",tv_existing_seller_store_name.getText().toString().trim());
                    user1.put("store_image_link",store_image_link.getText().toString());

                    System.out.println("13");
                    fstore.collection("Market Store")
                            .document(storeid)
                            .set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Existing_seller_profile.this, "Store is Live ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                    DocumentReference db = fstore.collection("Market Store")
                            .document(store_id.getText().toString());
                    db.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Existing_seller_profile.this, "Store not live", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Existing_seller_profile.this, "something wrong", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

        //Display your store to the market -1
        //recycler view for existing seller add item -0
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

        /*
        //set username for currentuser -0
        TextInputLayout seller_email = (TextInputLayout) findViewById(R.id.existing_seller_email_ET);
        //TextInputLayout seller_password = (TextInputLayout) findViewById(R.id.existing_seller_password_ET);
        TextInputLayout existing_seller_pin = (TextInputLayout) findViewById(R.id.existing_seller_pin_ET);
        seller_email.getEditText().setText(existing_seller_email);
        //seller_password.getEditText().setText("qwerty1234");
        existing_seller_pin.getEditText().setText("0000");
        //recycler view for existing seller add item -0

         */

        //final LinearLayout existing_seller_profile_LL = (LinearLayout) findViewById(R.id.existing_seller_profile_LL);
        existing_seller_profile_LL.setVisibility(View.VISIBLE);
        final ProgressBar progressBar_existing_seller_store_photo = (ProgressBar)findViewById(R.id.progressBar_existing_seller_store_photo);
        progressBar_existing_seller_store_photo.setVisibility(View.VISIBLE);

        //existing seller other details
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
                store_id.setText(value.getString("Store_ID"));
                store_image_link.setText("img-link-"+value.getString("store_photo_link"));
            }
        });
        // when existing seller logout button is pressed
        btn_existing_seller_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(),Home.class);
                startActivity(i);
            }
        });
        // when existing seller logout button is pressed
        //existing seller other details
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Seller and store personal data/" + current_user_email +"/store image.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(existing_seller_store_photo_iv);
                progressBar_existing_seller_store_photo.setVisibility(View.GONE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "url failed", Toast.LENGTH_SHORT).show();
            }
        });
        //timepicker
        //storeopentime
        storeopentime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(getApplicationContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                storeopentime.setText(sHour + ":"+ sMinute);
                                Map<String,String> user = new HashMap<>();
                                user.put("open time" ,storeopentime.getText().toString());
                                fstore.collection("Seller and store personal data").document(current_user_email).collection("store time").document("open time").set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
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
                picker = new TimePickerDialog(getApplicationContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                storeclosetime.setText(sHour + ":"+ sMinute);
                                Map<String,String> userclosetime = new HashMap<>();
                                userclosetime.put("close time" ,storeclosetime.getText().toString());
                                fstore.collection("Seller and store personal data").document(current_user_email).collection("store time").document("close time").set(userclosetime).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });
        //timepicker
        //creating layout existing user profile

        //when existing seller add item is clicked
        //s1
        //ImageView existing_seller_Add_item_iv = (ImageView) findViewById(R.id.existing_seller_Add_item_iv);
        existing_seller_Add_item_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                existing_seller_profile_LL.setVisibility(View.GONE);
                //when existing seller add item ok button is clicked
            }
        });

        btn_floating_additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //existing seller add item click -0
                AlertDialog.Builder item_name_builder = new AlertDialog.Builder(Existing_seller_profile.this);
                item_name_builder.setTitle("Item Name");

                final EditText item_name = new EditText(Existing_seller_profile.this);
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
                        existing_seller_all_item_LL.setVisibility(View.GONE);

                        //0
                        item_imageView1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent takePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

                                //create Product ID -0
                                String product_ID = Product_ID();
                                //create Product ID -1
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
                                    user.put("Product_Id",product_ID);

                                    fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item")
                                            .document(item_name).set(user, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                                                    LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                                    existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                                    existing_seller_addItem_LL.setVisibility(View.GONE);
                                                    existing_seller_profile_LL.setVisibility(View.GONE);
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
                                    user.put("Product_Id",product_ID);

                                    fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                                            LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                            existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                            existing_seller_addItem_LL.setVisibility(View.GONE);
                                            existing_seller_profile_LL.setVisibility(View.GONE);
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
                                    user.put("Product_Id",product_ID);

                                    fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                                            LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                            existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                            existing_seller_addItem_LL.setVisibility(View.GONE);
                                            existing_seller_profile_LL.setVisibility(View.GONE);
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
                                    user.put("Product_Id",product_ID);

                                    fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                                            LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                            existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                            existing_seller_addItem_LL.setVisibility(View.GONE);
                                            existing_seller_profile_LL.setVisibility(View.GONE);
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
                                    user.put("Product_Id",product_ID);

                                    fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                                            LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                            existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                            existing_seller_addItem_LL.setVisibility(View.GONE);
                                            existing_seller_profile_LL.setVisibility(View.GONE);
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
                                    user.put("Product_Id",product_ID);

                                    fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                                            LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                            existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                            existing_seller_addItem_LL.setVisibility(View.GONE);
                                            existing_seller_profile_LL.setVisibility(View.GONE);
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
                                    user.put("Product_Id",product_ID);

                                    fstore.collection("Seller and store personal data").document(current_user_email).collection("store all Item").document(item_name).set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                                            LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                            existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                            existing_seller_addItem_LL.setVisibility(View.GONE);
                                            existing_seller_profile_LL.setVisibility(View.GONE);
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
                                    user.put("Product_Id",product_ID);

                                    fstore.collection("Seller and store personal data")
                                            .document(current_user_email).collection("store all Item")
                                            .document(item_name).set(user,SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "ITEM added", Toast.LENGTH_SHORT).show();
                                                    LinearLayout existing_seller_all_item_LL = (LinearLayout) findViewById(R.id.existing_seller_all_item_LL);
                                                    existing_seller_all_item_LL.setVisibility(View.VISIBLE);
                                                    existing_seller_addItem_LL.setVisibility(View.GONE);
                                                    existing_seller_profile_LL.setVisibility(View.GONE);
                                                }
                                            });
                                }

                                else{
                                    Toast.makeText(getApplicationContext(), "Fill select above options)", Toast.LENGTH_SHORT).show();
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
                            }
                        });
                        //1
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
                    }
                });
                item_name_builder.show();

                //existing seller add item click -1
            }

            private String Product_ID() {
                Random random = new Random();
                String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@$#%*123456789";
                String s = "";
                while(s.length() <= 15){
                    s = s + String.valueOf(alphabet.charAt(random.nextInt(alphabet.length())));
                }

                int i,count=0;
                for (i=0;i<=all_Product_ID.size();i++){
                    if(i == 0){
                        count = 0;
                        break;
                    }
                    if (all_Product_ID.get(i) == s){
                        count += 1;
                    }
                }
                if (count == 0){
                    all_Product_ID.add(s);
                }
                else{
                    Product_ID();
                }
                return s;
            }
        });

        //oncreate -1
    }
}