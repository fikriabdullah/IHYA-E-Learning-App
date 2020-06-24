package com.example.islamdigitalecosystem;

import android.os.BaseBundle
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.islamdigitalecosystem.Entities.Models.Quran
import com.example.islamdigitalecosystem.Modules.Database.DatabaseHelper
import com.example.islamdigitalecosystem.Modules.Database.DatabasePresenter
import com.example.islamdigitalecosystem.Modules.Database.DatabaseView
import com.example.islamdigitalecosystem.Support.Utils.Adapter
import com.example.islamdigitalecosystem.ViewHolders.VerseViewHolder
import kotlinx.android.synthetic.main.activity_list_verse.*

class ListVerseActivity : AppCompatActivity(), DatabaseView{


    lateinit var  adapter: Adapter<Quran, VerseViewHolder>
    lateinit var bundle: Bundle
    var presenter = DatabasePresenter(DatabaseHelper(this), this)
    var surahTitle = ""
    var surahId = 1
    var verseId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_verse)
        setToolbar()

        bundle = intent.extras!!
        surahId = bundle.getInt("surahId")
        verseId = bundle.getInt("verseId")

        presenter.getDataBySurahId(surahId)

        text_title_surah.text = surahTitle

    }

    fun setToolbar(){
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun successGetDataBySurahId(list: List<Quran>) {
        setList(list)
    }

    fun setList(list: List<Quran>) {


        val manager = LinearLayoutManager(this)
        adapter = object : Adapter<Quran, VerseViewHolder>(R.layout.list_verse, VerseViewHolder::class.java,
                Quran::class.java, list) {
            override fun bindView(holder: VerseViewHolder, tipeData: Quran, position: Int) {
                holder.onBind(tipeData)

            }
        }

        list_verse.layoutManager = manager
        list_verse.adapter = adapter
    }
}