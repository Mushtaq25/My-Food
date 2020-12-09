package com.example.myfood;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_home extends Fragment {

    @Override
    public void onStart() {
        super.onStart();
        my_adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        my_adapter.stopListening();

    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView market_store_recycler_view1;
    FirebaseFirestore fstore;
    adapter_market_add_stores my_adapter;

    public fragment_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment fragment_home.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_home newInstance(String param1, String param2) {
        fragment_home fragment = new fragment_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        market_store_recycler_view1 = v.findViewById(R.id.market_store_recycler_view1);

        fstore = FirebaseFirestore.getInstance();

        Query query = fstore.collection("Market Store");
        FirestoreRecyclerOptions<model_market_add_stores> options =
                new FirestoreRecyclerOptions.Builder<model_market_add_stores>()
                        .setQuery(query.orderBy("store_Location").startAt("store Location:- Bongaigaon").endAt("store Location:- Bongaigaon"),
                                model_market_add_stores.class)
                        .build();

        market_store_recycler_view1.setLayoutManager(new GridLayoutManager(getContext(),2,RecyclerView.HORIZONTAL,false));
        market_store_recycler_view1.setHasFixedSize(true);
        my_adapter = new adapter_market_add_stores(options);
        market_store_recycler_view1.setAdapter(my_adapter);
        return v;
    }
}