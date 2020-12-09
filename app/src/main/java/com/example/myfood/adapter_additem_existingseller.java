package com.example.myfood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class adapter_additem_existingseller extends FirestoreRecyclerAdapter<model_add_item_existingseller,adapter_additem_existingseller.myviewholder> {


    public  adapter_additem_existingseller(@NonNull FirestoreRecyclerOptions<model_add_item_existingseller> options) {
        super(options);
    }

    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_item,parent,false);
        return new myviewholder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final model_add_item_existingseller model) {

        holder.tv_item_name1.setText("Name - "+model.getItem_name());
        holder.tv_item_description1.setText("Desc - "+model.getItem_description());
        holder.tv_item_price1.setText("Price Rs."+String.valueOf(model.getItem_Price()));
        holder.tv_item_cook_time1.setText("Cook time - "+String.valueOf(model.getItem_cooktime()));
        holder.tv_item_type1.setText("Type - "+model.getType());
        holder.tv_item_category1.setText("Category - "+model.getCategory());
        holder.Product_Id.setText("Product Id- " + model.getProduct_Id());

        Picasso.get().load(model.getImagelinkitem1()).fit().into(holder.iv_item1);
        Picasso.get().load(model.getImagelinkitem2()).fit().into(holder.iv_item2);
        Picasso.get().load(model.getImagelinkitem3()).fit().into(holder.iv_item3);
        Picasso.get().load(model.getImagelinkitem4()).fit().into(holder.iv_item4);

        holder.btn_delete_existing_seller_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext(),R.style.AlertDialogTheme);
                builder.setTitle("Delete ITEM " + model.getItem_name() + "Permanently");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                        String existinguser_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        DocumentReference db = fstore
                                .collection("Seller and store personal data")
                                .document(existinguser_email).collection("store all Item")
                                .document(model.getItem_name());
                        db.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            private String Tag;

                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(Tag,"deleted");
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        //display item -0
        holder.display_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                    String p_id = model.getProduct_Id();
                    Map<String,String> user = new HashMap<>();
                    user.put("item_name" ,model.getItem_name());
                    user.put("item_description",model.getItem_description());
                    user.put("item_cooktime",String.valueOf(model.getItem_cooktime()));
                    user.put("Item_Price",String.valueOf(model.getItem_Price()));
                    user.put("Type",model.getType());
                    user.put("category",model.getCategory());
                    user.put("imagelinkitem1",model.getImagelinkitem1());
                    user.put("imagelinkitem2",model.getImagelinkitem2());
                    user.put("imagelinkitem3",model.getImagelinkitem3());
                    user.put("imagelinkitem4",model.getImagelinkitem4());
                    user.put("Product_Id",p_id);

                    fstore.collection("Market items")
                            .document(p_id)
                            .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(holder.iv_item1.getContext(), "Item is Live ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                    DocumentReference db = fstore.collection("Market items")
                            .document(model.getProduct_Id());
                    db.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(holder.iv_item1.getContext(), "not live", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(holder.iv_item1.getContext(), "something wrong", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

        //display item -1
    }


    class myviewholder extends RecyclerView.ViewHolder{

        ImageView iv_item1,iv_item2,iv_item3,iv_item4;
        TextView tv_item_name1, tv_item_description1, tv_item_price1, tv_item_cook_time1, tv_item_type1, tv_item_category1,Product_Id;
        Button btn_delete_existing_seller_item;
        SwitchMaterial display_item;
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
            Product_Id = itemView.findViewById(R.id.Product_Id);
            display_item = itemView.findViewById(R.id.display_item);

            btn_delete_existing_seller_item = itemView.findViewById(R.id.btn_delete_existing_seller);
        }
    }
}
