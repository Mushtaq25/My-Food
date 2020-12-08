package com.example.myfood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class adapter_market_add_stores extends FirestoreRecyclerAdapter<model_market_add_stores,adapter_market_add_stores.myviewholder> {

    public adapter_market_add_stores(@NonNull FirestoreRecyclerOptions<model_market_add_stores> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model_market_add_stores model) {
        System.out.println("115");
        Picasso.get().load(model.getStore_image_link()).fit().into(holder.iv_market_storephoto);

        holder.market_store_name.setText(model.getStore_name());
        holder.market_store_location.setText(model.getStore_Location());
        System.out.println("116");
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("110");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.market_add_stores,parent,false);
        System.out.println("112");
        return new myviewholder(view);
    }
    class myviewholder extends RecyclerView.ViewHolder{

        TextView market_store_name,market_store_location;
        ImageView iv_market_storephoto;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            System.out.println("117");
            market_store_name = itemView.findViewById(R.id.market_store_name);
            market_store_location = itemView.findViewById(R.id.market_store_location);
            iv_market_storephoto = itemView.findViewById(R.id.iv_market_storephoto);
        }
    }
}
