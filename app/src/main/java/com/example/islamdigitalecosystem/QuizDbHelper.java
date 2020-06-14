package com.example.islamdigitalecosystem;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import com.example.islamdigitalecosystem.QuizContract.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private Context context;
    String CREATE_TABLE = "CREATE TABLE " +
            QuestionTable.Table_Name + " ( " +
            QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            QuestionTable.COLUMN_QUESTION + " TEXT, " +
            QuestionTable.COLUMN_OPT1 + " TEXT, " +
            QuestionTable.COLUMN_OPT2 + " TEXT, " +
            QuestionTable.COLUMN_OPT3 + " TEXT, " +
            QuestionTable.COLUMN_OPT4 + " TEXT, " +
            QuestionTable.COLUMN_ANSWER_NR + " INTEGER )";
    private static final String DATABASE_NAME = "QuizDb.db";
    public static final int DATABASE_VERSION = 1;
    private  SQLiteDatabase sqLiteDatabase;
    byte [] bytesImg;

    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.sqLiteDatabase = db;
        sqLiteDatabase.execSQL(CREATE_TABLE);
        fillQuestionTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionTable.Table_Name);
        onCreate(sqLiteDatabase);
    }


    public void fillQuestionTable(){
        Question q1 = new Question("A is Correct" ,"A", "B", "C", "D", 1);
        addQuestion(q1);
    }



    private void addQuestion(Question question){

        ContentValues cv = new ContentValues();
        cv.put(QuizContract.QuestionTable.COLUMN_QUESTION, question.getQuestion());
        //cv.put(QuizContract.QuestionTable.COLUMN_IMAGE, question.getImageByteArray());
        cv.put(QuizContract.QuestionTable.COLUMN_OPT1, question.getOpt1());
        cv.put(QuizContract.QuestionTable.COLUMN_OPT2, question.getOpt2());
        cv.put(QuizContract.QuestionTable.COLUMN_OPT3, question.getOpt3());
        cv.put(QuizContract.QuestionTable.COLUMN_OPT4, question.getOpt4());
        cv.put(QuizContract.QuestionTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        sqLiteDatabase.insert(QuizContract.QuestionTable.Table_Name, null, cv);
    }

    public List<Question> getAllQuestion(){
        List<Question> questionList = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + QuizContract.QuestionTable.Table_Name, null);
        if (c.moveToFirst()){
            do{
            Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)));
                //question.setImageByteArray(c.getBlob(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_IMAGE)));
                question.setOpt1(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPT1)));
                question.setOpt2(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPT2)));
                question.setOpt3(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPT3)));
                question.setOpt4(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPT4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
