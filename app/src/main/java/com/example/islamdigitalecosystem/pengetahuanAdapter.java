package com.example.islamdigitalecosystem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;

public class pengetahuanAdapter extends RecyclerView.Adapter<pengetahuanAdapter.MyViewHolder> {
    ArrayList<pengetahuanData> pengetahuanData;
    Context context;
    private static final String TAG = "pengetahuanAdapter";
    private DatabaseReference firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    int hargaMateri;
    boolean isPaid;

    public pengetahuanAdapter(ArrayList<pengetahuanData> pengetahuanData) {
        this.pengetahuanData = pengetahuanData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pengetahuan_list,parent,false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       final String judulMateri = pengetahuanData.get(position).getMateriName();
       holder.setBabList(judulMateri);
       firebaseDatabase = FirebaseDatabase.getInstance().getReference();
       firebaseAuth = FirebaseAuth.getInstance();
       db = FirebaseFirestore.getInstance();

       //set on click listener jika di salah satu elemen di klik
       holder.goMateri.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View view) {

               //retrieve harga materi dan apakah materi berbayar
               db.collection("Materi").document(judulMateri).collection(judulMateri)
                       .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                           materiModelClass materiModels = documentSnapshot.toObject(materiModelClass.class);
                           isPaid = materiModels.isPaidmateri();
                           Log.d(TAG, "materi is paid : " + isPaid);
                           if (isPaid){
                               double hrgMateri = materiModels.getHargaMateri();
                               hargaMateri = ((int) hrgMateri);
                           }else if (!isPaid){
                               break;
                           }
                       }
                   }
               });

               firebaseDatabase.child("UserDatabase").child("Student").child(firebaseAuth.getCurrentUser().getUid())
                       .child("MateriOwned");

                   firebaseDatabase.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           dataSnapshot.getChildren();
                           String isOwned = dataSnapshot.child(judulMateri).getValue(String.class);
                           Log.d(TAG, "materi Owned : " + isOwned);

                           if ((isOwned == null && isPaid)){
                               if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                   ActivityCompat.requestPermissions((Activity)view.getContext(), new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
                               }
                               Log.d(TAG, "materi is paid, and not owned");
                               SdkUIFlowBuilder.init()
                                       .setClientKey("SB-Mid-client-PewuNr7kEHDpz8cu")
                                       .setContext(view.getContext())
                                       .setTransactionFinishedCallback(new TransactionFinishedCallback() {
                                           @Override
                                           public void onTransactionFinished(TransactionResult transactionResult) {
                                               Log.d(TAG, "transaction Finsished!! " + transactionResult.getStatus());

                                           }
                                       }).setMerchantBaseUrl("https://ihya-elearning.herokuapp.com/")
                                       .enableLog(true)
                                       .setLanguage("id")
                                       .buildSDK();

                               FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                               TransactionRequest transactionRequest = new TransactionRequest(String.valueOf(Math.random()), hargaMateri);

                               com.midtrans.sdk.corekit.models.ItemDetails itemDetails = new ItemDetails("Materi Bahasa Arab", hargaMateri, 1 , judulMateri);
                               ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
                               itemDetailsArrayList.add(itemDetails);
                               transactionRequest.setItemDetails(itemDetailsArrayList);

                               CustomerDetails customerDetails = new CustomerDetails();
                               String userIdentifier = firebaseAuth.getCurrentUser().getDisplayName();
                               customerDetails.setCustomerIdentifier(userIdentifier);
                               customerDetails.getBillingAddress();
                               customerDetails.getShippingAddress();
                               customerDetails.getPhone();
                               customerDetails.getEmail();

                               transactionRequest.setCustomerDetails(customerDetails);

                               MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
                               MidtransSDK.getInstance().startPaymentUiFlow(view.getContext());
                           }else if (isOwned == null || !isPaid){ //materi is not owned or materi is free
                               Log.d(TAG, "materi is free or owned");
                               Intent intent = new Intent(context, materiBab1.class);
                               intent.putExtra("babMateriRef", judulMateri);
                               context.startActivity(intent);
                           }else{
                               try {
                                   throw new Exception("Something Went Wrong");
                               } catch (Exception e) {
                                   Log.d(TAG, "Clicking Materi : " + e.getMessage());
                               }
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                           Log.d(TAG, "Database Error : " + databaseError.getMessage());
                       }
                   });
           }
       });
        holder.materiImage.setImageResource(R.drawable.aljabar);
    }

    @Override
    public int getItemCount() {
        return pengetahuanData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView materiImage;
        LinearLayout goMateri;
        TextView textViewName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            materiImage = itemView.findViewById(R.id.ivBabListMateri);
            goMateri = itemView.findViewById(R.id.bablistMateri);
        }

        public void setBabList(String babList){
            textViewName = itemView.findViewById(R.id.textName);
            textViewName.setText(babList);
        }
    }

}
