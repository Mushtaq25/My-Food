package com.example.myfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class adapter_explore_item extends FirestoreRecyclerAdapter<model_explore_item,adapter_explore_item.myviewholder> {


    public adapter_explore_item(@NonNull FirestoreRecyclerOptions<model_explore_item> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model_explore_item model) {

        holder.tv_explore_item_name.setText(model.getItem_name());
        holder.tv_explore_item_description.setText(model.getItem_description());
        holder.tv_explore_item_price.setText("Rs. "+model.getItem_Price());
        holder.tv_explore_cook_time.setText(model.getItem_cooktime());
        holder.tv_explore_item_category.setText(model.getCategory());
        holder.tv_explore_item_type.setText(model.getType());
        holder.tv_explore_Product_Id.setText(model.getProduct_Id());
        holder.tv_explore_item_img1.setText(model.getImagelinkitem1());
        holder.tv_explore_item_img2.setText(model.getImagelinkitem2());
        holder.tv_explore_item_img3.setText(model.getImagelinkitem3());
        holder.tv_explore_item_img4.setText(model.getImagelinkitem4());

        Picasso.get().load(model.getImagelinkitem1()).fit().into(holder.iv_item_image);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_market_explore_item,parent,false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder{

        ImageView iv_item_image;
        TextView tv_explore_item_img1,tv_explore_item_img2,tv_explore_item_img3,
                tv_explore_item_img4, tv_explore_item_name, tv_explore_item_description, tv_explore_item_price, tv_explore_cook_time
                , tv_explore_item_type, tv_explore_item_category,tv_explore_Product_Id;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            iv_item_image = itemView.findViewById(R.id.iv_explore_item_image);
            tv_explore_item_img1 = itemView.findViewById(R.id.tv_explore_item_img1);
            tv_explore_item_img2 = itemView.findViewById(R.id.tv_explore_item_img2);
            tv_explore_item_img3 = itemView.findViewById(R.id.tv_explore_item_img3);
            tv_explore_item_img4 = itemView.findViewById(R.id.tv_explore_item_img4);
            tv_explore_item_name = itemView.findViewById(R.id.tv_explore_item_name);
            tv_explore_item_description = itemView.findViewById(R.id.tv_explore_item_description);
            tv_explore_item_price = itemView.findViewById(R.id.tv_explore_item_price);
            tv_explore_cook_time = itemView.findViewById(R.id.tv_explore_cook_time);
            tv_explore_item_type = itemView.findViewById(R.id.tv_explore_item_type);
            tv_explore_item_category = itemView.findViewById(R.id.tv_explore_item_category);
            tv_explore_Product_Id = itemView.findViewById(R.id.tv_explore_Product_Id);

        }
    }
}
