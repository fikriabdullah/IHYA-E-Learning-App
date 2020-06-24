package com.example.islamdigitalecosystem;

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.islamdigitalecosystem.Modules.Database.DatabaseHelper
import com.example.islamdigitalecosystem.Modules.ImportFromFile.Literation
import com.example.islamdigitalecosystem.Modules.ImportFromFile.LiterationInteractor

class Main : AppCompatActivity(), Literation {

    val interactor = LiterationInteractor(this, this)
    val databaseHelper = DatabaseHelper(this)

    override fun successInputDatabase() {
        openNextActivity()
    }

    override fun failedInputDatabase() {
        databaseHelper.clearTable()
        interactor.setData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        if (databaseHelper.isDataAvailable()){
            openNextActivity()
        }else{
            databaseHelper.clearTable()
            interactor.setData()
        }
    }

    fun openNextActivity(){
        val intent = Intent(this, ListSurahActivity::class.java)
        startActivity(intent)
        finish()
    }
}
