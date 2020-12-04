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
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

public class adapter_additem_existingseller extends FirestoreRecyclerAdapter<model_add_item_existingseller,adapter_additem_existingseller.myviewholder> {



    public adapter_additem_existingseller(@NonNull FirestoreRecyclerOptions<model_add_item_existingseller> options) {
        super(options);
    }

    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_item,parent,false);
        return new myviewholder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model_add_item_existingseller model) {

        holder.tv_item_name1.setText("Name "+model.getItem_name());
        holder.tv_item_description1.setText("Desc "+model.getItem_description());
        holder.tv_item_price1.setText("Price Rs."+String.valueOf(model.getItem_Price()));
        holder.tv_item_cook_time1.setText("Cook time "+String.valueOf(model.getItem_cooktime()));
        holder.tv_item_type1.setText("Type "+model.getType());
        holder.tv_item_category1.setText("Category "+model.getCategory());

        Picasso.get().load(model.getImagelinkitem1()).fit().into(holder.iv_item1);
        Picasso.get().load(model.getImagelinkitem2()).fit().into(holder.iv_item2);
        Picasso.get().load(model.getImagelinkitem3()).fit().into(holder.iv_item3);
        Picasso.get().load(model.getImagelinkitem4()).fit().into(holder.iv_item4);

    }


    class myviewholder extends RecyclerView.ViewHolder{

        ImageView iv_item1,iv_item2,iv_item3,iv_item4;
        TextView tv_item_name1, tv_item_description1, tv_item_price1, tv_item_cook_time1, tv_item_type1, tv_item_category1;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            iv_item1 = itemView.findViewById(R.id.iv_item_image1);
            iv_item2 = itemView.findViewById(R.id.iv_item_image2);
            iv_item3 = itemView.findViewById(R.id.iv_item_image3);
            iv_item4 = itemView.findViewById(R.id.iv_item_image4);
            tv_item_name1 = itemView.findViewById(R.id.tv_item_name);
            tv_item_description1 = itemView.findViewById(R.id.tv_item_description);
            tv_item_price1 = itemView.findViewById(R.id.tv_item_price);
            tv_item_cook_time1 = itemView.findViewById(R.id.tv_item_cook_time);
            tv_item_type1 = itemView.findViewById(R.id.tv_item_type);
            tv_item_category1 = itemView.findViewById(R.id.tv_item_category);
        }
    }
}
