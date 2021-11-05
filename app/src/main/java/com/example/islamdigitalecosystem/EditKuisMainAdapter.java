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
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.AlgorithmConstraints;
import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class EditKuisMainAdapter extends RecyclerView.Adapter<EditKuisMainAdapter.MyViewHolder> {
    ArrayList <Question> questionArrayList;
    Context context;
    MediaPlayer mediaPlayer;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG ="EditKuisMainAdapter : ";

    public EditKuisMainAdapter(ArrayList<Question> questionArrayList){
        this.questionArrayList = questionArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_single_item_view_edit_quiz, parent, false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String question = questionArrayList.get(position).getQuestion();
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

        String imgDwnldUrl = questionArrayList.get(position).getImgDwnldUrl();
        final String audioDwnldUrl = questionArrayList.get(position).getAudioDwnldUrl();
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
    }


    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPlacehld, audioPlacehld;
        Context context;
        private EditText soal, pil1, pil2, pil3, pil4, pilBenar;
        private Button changeMedia;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            audioPlacehld = itemView.findViewById(R.id.imageOrVoiceEdit2);
            imgPlacehld = itemView.findViewById(R.id.imageOrVoiceEdit);
            context = itemView.getContext();
            changeMedia = itemView.findViewById(R.id.btChangeMedia);

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
