package com.android.hobbyinventory.gui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.hobbyinventory.R

class ItemDetailActivity : AppCompatActivity() {

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        if(intent.extras != null) {
            val extras: Bundle = intent.extras!!
        }


    }
}