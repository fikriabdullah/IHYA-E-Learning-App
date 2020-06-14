package com.example.islamdigitalecosystem;

import android.provider.BaseColumns;

public final class QuizContract {
    private QuizContract(){
    }

    public static class QuestionTable implements BaseColumns {

        public static final String Table_Name = "quiz_question";
        public static final String COLUMN_QUESTION = "column_question";
        public static final String COLUMN_OPT1 = "column_opt1";
        public static final String COLUMN_OPT2 = "column_opt2";
        public static final String COLUMN_OPT3 = "column_opt3";
        public static final String COLUMN_OPT4 = "column_opt4";
        public static final String COLUMN_ANSWER_NR = "column_answer_Nr";
        public static final String COLUMN_IMAGE = "question_image";
    }
}
