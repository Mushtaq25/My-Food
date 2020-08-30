package com.example.myfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    ImageView btn_logout;
    ImageButton Zafir,btn_home,btn_user,btn_Go;
    TextView tv_Not_available;
    Spinner district;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_home = findViewById(R.id.btnhome);
        btn_user = findViewById(R.id.btn_user);
        btn_logout = findViewById(R.id.logout);
        btn_Go = findViewById(R.id.btn_Go);
        Zafir = findViewById(R.id.Zafir);
        tv_Not_available = findViewById(R.id.tv_Not_available);


        addListenerOnSpinnerItemSelection();


        // Pressing Home button start
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this,Home.class);
                startActivity(i);
            }
        });
        // finish


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
                    Zafir.setVisibility(View.VISIBLE);
                    tv_Not_available.setVisibility(View.INVISIBLE);

                }
                else {
                    System.out.println("No it is NO");
                    tv_Not_available.setVisibility(View.VISIBLE);
                    Zafir.setVisibility(View.INVISIBLE);
                }
            }
        });// finish


    }

    private void addListenerOnSpinnerItemSelection() {
        district = (Spinner) findViewById(R.id.district);
        district.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

}