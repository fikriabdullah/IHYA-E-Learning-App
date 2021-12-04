package com.example.islamdigitalecosystem;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class EditKuisMainAdapter extends RecyclerView.Adapter<EditKuisMainAdapter.MyViewHolder> {
    ArrayList<Question> questionArrayList;
    Context context;
    MediaPlayer mediaPlayer;
    private Singleton babRef;
    private static final String TAG ="EditKuisMainAdapter : ";
    FirebaseFirestore db;

    public EditKuisMainAdapter(ArrayList<Question> questionArrayList){
        this.questionArrayList = questionArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_single_item_view_edit_quiz, parent, false);
        context = parent.getContext();
        Log.d(TAG, "OnCreate : ");
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        db = FirebaseFirestore.getInstance();

        final String question = questionArrayList.get(position).getQuestion();
        holder.setQuestionData(question);

        String pil1 = questionArrayList.get(position).getOpt1();
        holder.setPil1(pil1);

        String pil2 = questionArrayList.get(position).getOpt2();
        holder.setPil2(pil2);

        String pil3 = questionArrayList.get(position).getOpt3();
        holder.setPil3(pil3);

        String pil4 = questionArrayList.get(position).getOpt4();
        holder.setPil4(pil4);

        String pilBenar = questionArrayList.get(position).getCrAnswer();
        holder.setPilBenar(pilBenar);

        final String imgDwnldUrl = questionArrayList.get(position).getImgDwnldUrl();
        final String audioDwnldUrl = questionArrayList.get(position).getAudioDwnldUrl();
        babRef = Singleton.getInstance();
        final String babReference = babRef.getBabReference();
        Log.d(TAG, "BabReference : " + babReference);

        if (imgDwnldUrl != null){
            holder.audioPlacehld.setVisibility(View.INVISIBLE);
            holder.loadImage(imgDwnldUrl);
            Log.d(TAG, "ImgDwnldUrl : " + imgDwnldUrl);
        }else if (audioDwnldUrl != null){
            holder.imgPlacehld.setVisibility(View.INVISIBLE);
            holder.audioPlacehld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "AudioDwnldUrl: " + audioDwnldUrl);
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioAttributes(
                            new AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                    );
                    try {
                        mediaPlayer.setDataSource(audioDwnldUrl);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }catch (IOException au){
                        Log.d(TAG, "Media Player error : " + au.getCause());
                    }
                }
            });
        }else {
            Log.d(TAG, "failed getting media dwnldUrl  :" + audioDwnldUrl + "\n" + imgDwnldUrl);
        }

        holder.changeMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "position : " + holder.getLayoutPosition());
                Intent changeMedia = new Intent(context, changeMedia.class);
                int pos = holder.getLayoutPosition();
                changeMedia.putExtra("imgDwnldUrl", imgDwnldUrl);
                changeMedia.putExtra("AudioDwnldUrl", audioDwnldUrl);
                changeMedia.putExtra("documentRef", pos);
                context.startActivity(changeMedia);
            }
        });

        holder.saveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                String questionNew = holder.soal.getText().toString();
                String pil1New = holder.pil1.getText().toString();
                String pil2New = holder.pil2.getText().toString();
                String pil3New = holder.pil3.getText().toString();
                String pil4New = holder.pil4.getText().toString();
                String pilCrNew = holder.pilBenar.getText().toString();

                int postn = holder.getLayoutPosition()+1;
                String Position = String.valueOf(postn);

                Log.d(TAG, "babReference : " + babReference + "\n" + "document Reference : " + Position);

                try {
                    db.collection("quiz").document(babReference).collection(babReference)
                            .document("Question" + Position)
                            .update("question", questionNew,
                                    "opt1", pil1New,
                                    "opt2", pil2New,
                                    "opt3", pil3New,
                                    "opt4", pil4New,
                                    "crAnswer", pilCrNew).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Success");
                            Toast.makeText(context, "Update Saved!!", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Failed : " + e.getMessage());
                        }
                    });
                }catch (Exception e){
                    Log.d(TAG, "Error : " + e.getMessage());
                    Toast.makeText(context, "Update document Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.deleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int questPos = holder.getLayoutPosition()+1;
                final String questPostn = String.valueOf(questPos);
                Log.d(TAG, "Documetn Ref :" + babReference + "\n" + "Position" + questPostn);

                try {
                    db.collection("quiz").document(babReference).collection(babReference)
                            .document("Question"+questPostn).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Question : " + questPostn + "\nDocument Deleted");
                                    Toast.makeText(context, "Document Deleted!!, Pull down to Refresh Page", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Delete failed : " +e.getMessage());
                        }
                    });
                }catch (Exception e){
                    Log.d(TAG, "Delete Question Failed" + e.getMessage());
                    Toast.makeText(context, "Delete Question Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPlacehld, audioPlacehld;
        Context context;
        private EditText soal, pil1, pil2, pil3, pil4, pilBenar;
        private Button changeMedia, saveQuestion, deleteQuestion;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            audioPlacehld = itemView.findViewById(R.id.imageOrVoiceEdit2);
            imgPlacehld = itemView.findViewById(R.id.imageOrVoiceEdit);
            context = itemView.getContext();
            changeMedia = itemView.findViewById(R.id.btChangeMedia);
            saveQuestion = itemView.findViewById(R.id.btSaveEdit);
            deleteQuestion = itemView.findViewById(R.id.btDelQuestion);
        }
        public void setQuestionData(String questionData){
            soal = itemView.findViewById(R.id.etEditQuestMain);
            soal.setText(questionData);
        }

        public void setPil1(String pil1Data){
            pil1 = itemView.findViewById(R.id.etChoice1);
            pil1.setText(pil1Data);
        }

        public void setPil2(String pil2Data){
            pil2 = itemView.findViewById(R.id.etChoice2);
            pil2.setText(pil2Data);
        }

        public void setPil3(String pil3Data){
            pil3 = itemView.findViewById(R.id.etChoice3);
            pil3.setText(pil3Data);
        }

        public void setPil4(String pil4Data){
            pil4 = itemView.findViewById(R.id.etChoice4);
            pil4.setText(pil4Data);
        }

        public void setPilBenar(String pilBenarData){
            pilBenar = itemView.findViewById(R.id.etCrAnswerEdit);
            pilBenar.setText(pilBenarData);
        }

        public void loadImage(String imgDwnldUrl){
            imgPlacehld = itemView.findViewById(R.id.imageOrVoiceEdit);
            Log.d(TAG, "ImgDwnldUrl : " + imgDwnldUrl);
            imgPlacehld.setClickable(false);
            Picasso.with(context).load(imgDwnldUrl).into(imgPlacehld);
        }
    }
}
