package com.example.myfood;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.Adapter;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Market extends AppCompatActivity {

    RecyclerView market_store_recycler_view;
    FirebaseFirestore fstore;
    adapter_market_add_stores my_adapter;

    @Override
    protected void onStart() {
        super.onStart();
        my_adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        my_adapter.stopListening();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        //setActionBar()
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.mytoolbar);
        // finish

        market_store_recycler_view = findViewById(R.id.market_recycler_view);
        fstore = FirebaseFirestore.getInstance();

        Query query = fstore.collection("Market Store");
        FirestoreRecyclerOptions<model_market_add_stores> options =
                new FirestoreRecyclerOptions.Builder<model_market_add_stores>()
                        .setQuery(query.orderBy("store_Location").startAt("store Location:- Bongaigaon").endAt("store Location:- Bongaigaon"),
                                model_market_add_stores.class)
                        .build();
        market_store_recycler_view.setLayoutManager(new GridLayoutManager(this,2,RecyclerView.HORIZONTAL,false));
        market_store_recycler_view.setHasFixedSize(true);
        my_adapter = new adapter_market_add_stores(options);
        market_store_recycler_view.setAdapter(my_adapter);
        //oncreate -1
    }
}