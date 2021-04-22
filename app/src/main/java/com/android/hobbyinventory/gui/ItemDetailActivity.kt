package com.android.hobbyinventory.gui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.hobbyinventory.R

class ItemDetailActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE = 101
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        if(intent.extras != null) {
            val extras: Bundle = intent.extras!!
        }


    }
}