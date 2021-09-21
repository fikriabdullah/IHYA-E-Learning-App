package com.example.islamdigitalecosystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class editKuisAdapter extends RecyclerView.Adapter<editKuisAdapter.MyViewHolder> {
    ArrayList <Question> questionArrayList;
    Context context;

    public editKuisAdapter(ArrayList<Question>questionArrayList){
        this.questionArrayList = questionArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_kuis_single_item, parent, false);
        context = parent.getContext();

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        String soalData = questionArrayList.get(position).getQuestion();
        holder.setQuestion(soalData);

        String opt1 = questionArrayList.get(position).getOpt1();
        holder.setOpt1(opt1);

        String opt2 = questionArrayList.get(position).getOpt2();
        holder.setOpt2(opt2);

        String opt3 = questionArrayList.get(position).getOpt3();
        holder.setOpt3(opt3);

        String opt4 = questionArrayList.get(position).getOpt4();
        holder.setOpt4(opt4);

        String imgDwnldUrl = questionArrayList.get(position).getImgDwnldUrl();
        holder.setImg(imgDwnldUrl);

        String crAnswer = questionArrayList.get(position).getCrAnswer();
        holder.setcrAnswer(crAnswer);

        String audioDwnldUrl = questionArrayList.get(position).getAudioDwnldUrl();
        holder.setImg(audioDwnldUrl);

    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView IVQuestion;
        EditText etQuestion, etOpt1, etOpt2, etOpt3, etOpt4, etcrAnswer;
        Button btnSaveEdit, btnChangeImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnSaveEdit = itemView.findViewById(R.id.BTNSaveedit);
            btnChangeImg = itemView.findViewById(R.id.BTNEditImg);
        }

        public void setImg(String imgDwnldUrl){
            IVQuestion = itemView.findViewById(R.id.IVPertanyaan);
            IVQuestion.setImageResource(R.drawable.ic_play_audio);
        }

        public void setQuestion(String questionData){
            etQuestion = itemView.findViewById(R.id.etPertanyaan);
            etQuestion.setText(questionData);
        }
        public void setOpt1(String Opt1){
            etOpt1 = itemView.findViewById(R.id.etOpt1);
            etOpt1.setText(Opt1);
        }
        public void setOpt2(String Opt2){
            etOpt2 = itemView.findViewById(R.id.etOpt2);
            etOpt2.setText(Opt2);
        }
        public void setOpt3(String Opt3){
            etOpt3 = itemView.findViewById(R.id.etOpt3);
            etOpt3.setText(Opt3);
        }
        public void setOpt4(String Opt4){
            etOpt4 = itemView.findViewById(R.id.etOpt4);
            etOpt4.setText(Opt4);
        }
        public void setcrAnswer(String crAnswer){
            etcrAnswer = itemView.findViewById(R.id.etCrOpt);
            etcrAnswer.setText(crAnswer);
        }

    }
}
