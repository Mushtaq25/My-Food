package com.example.myfood;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_explore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_explore extends Fragment {
    @Override
    public void onStart() {
        super.onStart();
        my_explore_adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        my_explore_adapter.stopListening();

    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView market_all_item_recyclerview;
    adapter_explore_item my_explore_adapter;
    FirebaseFirestore fstore;

    public fragment_explore() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment fragment_explore.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_explore newInstance(String param1, String param2) {
        fragment_explore fragment = new fragment_explore();
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
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        market_all_item_recyclerview = v.findViewById(R.id.market_all_item_recyclerview);
        fstore = FirebaseFirestore.getInstance();

        Query query = fstore
                .collection("Market items");
        FirestoreRecyclerOptions<model_explore_item> option = new FirestoreRecyclerOptions.Builder<model_explore_item>()
                .setQuery(query, model_explore_item.class)
                .build();

        market_all_item_recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        market_all_item_recyclerview.setLayoutManager(layoutManager);
        my_explore_adapter = new adapter_explore_item(option);
        market_all_item_recyclerview.setAdapter(my_explore_adapter);

        return v;
    }
}