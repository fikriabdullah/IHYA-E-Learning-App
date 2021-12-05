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

import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Objects;
import java.util.Random;

public class pengetahuanAdapter extends RecyclerView.Adapter<pengetahuanAdapter.MyViewHolder> {
    ArrayList<pengetahuanData> pengetahuanData;
    Context context;
    private static final String TAG = "pengetahuanAdapter";
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
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
       firebaseDatabase = FirebaseDatabase.getInstance();
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

               //retrive is materi owned
               databaseReference = firebaseDatabase.getReference().child("UserDatabase")
                       .child("Student").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
               databaseReference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       dataSnapshot.getChildren();
                       boolean isOwned = dataSnapshot.child("MateriOwned").child(judulMateri).exists();
                       Log.d(TAG, "materi Owned : " + isOwned);
                       Log.d(TAG, "judul materi " + judulMateri);


                       if ((!isOwned) && (!isPaid)){//materi berbayar, belum lunas
                           //bayar ke midtrans
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
                                           if (transactionResult.getResponse() != null) {
                                               switch (transactionResult.getStatus()) {
                                                   case TransactionResult.STATUS_SUCCESS:
                                                       Toast.makeText(view.getContext(), "Transaction Finished. ID: " + transactionResult.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                                                       Log.d(TAG, "transaction Status : " + transactionResult.getResponse().getTransactionId());

                                                       databaseReference.child("UserDatabase").child("Student").child(firebaseAuth.getCurrentUser().getUid())
                                                               .child("MateriOwned").child(judulMateri).setValue(judulMateri)
                                                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                   @Override
                                                                   public void onSuccess(Void unused) {
                                                                       Log.d(TAG, "transaction saved!!");
                                                                       Intent intent = new Intent(view.getContext(), materiBab1.class);
                                                                       intent.putExtra("babMateriRef", judulMateri);
                                                                       context.startActivity(intent);
                                                                   }
                                                               }).addOnFailureListener(new OnFailureListener() {
                                                           @Override
                                                           public void onFailure(@NonNull Exception e) {
                                                               Log.d(TAG, "sasving transaction failed "+e.getMessage());
                                                               Toast.makeText(view.getContext(), "Saving To Database Failed", Toast.LENGTH_LONG).show();
                                                           }
                                                       });
                                                       break;
                                                   case TransactionResult.STATUS_PENDING:
                                                       //Toast.makeText(view.getContext(), "Transaction Pending. ID: " + transactionResult.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                                                       Log.d(TAG, "transaction Status : " + transactionResult.getResponse().getTransactionId());

                                                       databaseReference.child("UserDatabase").child("Student").child(firebaseAuth.getCurrentUser().getUid())
                                                               .child("MateriOwned").child(judulMateri).setValue(judulMateri)
                                                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                   @Override
                                                                   public void onSuccess(Void unused) {
                                                                       Log.d(TAG, "transaction saved!!");
                                                                       Intent intent = new Intent(view.getContext(), materiBab1.class);
                                                                       intent.putExtra("babMateriRef", judulMateri);
                                                                       context.startActivity(intent);
                                                                   }
                                                               }).addOnFailureListener(new OnFailureListener() {
                                                           @Override
                                                           public void onFailure(@NonNull Exception e) {
                                                               Log.d(TAG, "sasving transaction failed "+e.getMessage());
                                                               Toast.makeText(view.getContext(), "Saving To Database Failed", Toast.LENGTH_LONG).show();
                                                           }
                                                       });
                                                       break;

                                                   case TransactionResult.STATUS_FAILED:
                                                       Toast.makeText(view.getContext(), "Transaction Failed. ID: " + transactionResult.getResponse().getTransactionId() + ". Message: " + transactionResult.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                                                       Log.d(TAG, "transaction Status : " + transactionResult.getResponse().getTransactionId());
                                                       break;
                                               }
                                               transactionResult.getResponse().getValidationMessages();
                                           } else if (transactionResult.isTransactionCanceled()) {
                                               Toast.makeText(view.getContext(), "Transaction Canceled", Toast.LENGTH_LONG).show();
                                               Log.d(TAG, "Transaction Canceled");
                                           } else {
                                               if (transactionResult.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                                                   Toast.makeText(view.getContext(), "Transaction Invalid", Toast.LENGTH_LONG).show();
                                                   Log.d(TAG, "Transaction Invalid");
                                               } else {
                                                   Toast.makeText(view.getContext(), "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
                                                   Log.d(TAG, "Transaction Finished with failure.");
                                               }
                                           }

                                       }
                                   }).setMerchantBaseUrl("https://ihya-elearning.herokuapp.com/")
                                   .enableLog(true)
                                   .setLanguage("id")
                                   .buildSDK();

                           Random random = new Random();
                           long orderID = random.nextLong();
                           FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                           TransactionRequest transactionRequest = new TransactionRequest(String.valueOf(orderID), hargaMateri);

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

                       }else if ((isOwned) || (isPaid)){ //materi is has ben lunas
                           Log.d(TAG, "materi is lunas");
                           Intent intent = new Intent(context, materiBab1.class);
                           intent.putExtra("babMateriRef", judulMateri);
                           context.startActivity(intent);
                       }else if ((!isOwned) && (isPaid)){ //materi tidak berbayar
                           Log.d(TAG, "materi is free");
                           Intent intent = new Intent(context, materiBab1.class);
                           intent.putExtra("babMateriRef", judulMateri);
                           context.startActivity(intent);
                       }
                       else{
                           try {
                               throw new Exception("Something Went Wrong");
                           } catch (Exception e) {
                               Toast.makeText(view.getContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                               Log.d(TAG, "Clicking Materi : " + e.getMessage());
                           }
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

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
