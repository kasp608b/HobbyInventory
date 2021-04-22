package com.android.hobbyinventory.gui

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import com.android.hobbyinventory.R
import com.android.hobbyinventory.model.BEItem
import kotlinx.android.synthetic.main.activity_item_detail.*
import java.io.File

class ItemDetailActivity : AppCompatActivity() {

    var mFile: File? = null
    private lateinit var item: BEItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        if(intent.extras != null) {
            val extras: Bundle = intent.extras!!

            item = extras.getSerializable("item") as BEItem
            tvItemName.text = item.name
            tvDescription.text = item.desc

            if(item.pictureFile != null)
            {
                val mImage = findViewById<ImageView>(R.id.imgItem)
                val file = File(item.pictureFile!!)
                mFile = file
                showImageFromFile(mImage, file)
            }

            btnSave.visibility = View.GONE
            etItemName.visibility = View.GONE
            etDescription.visibility = View.GONE

        }

        sEdit.setOnCheckedChangeListener{
            view, isChecked -> onCheckedChange(view, isChecked)

        }


    }

    private fun onCheckedChange(view: CompoundButton?, checked: Boolean) {
        if(checked)
        {
            btnSave.visibility = View.VISIBLE
            etItemName.visibility = View.VISIBLE
            etDescription.visibility = View.VISIBLE

            tvDescription.visibility = View.GONE
            tvItemName.visibility = View.GONE

        } else {
            btnSave.visibility = View.GONE
            etItemName.visibility = View.GONE
            etDescription.visibility = View.GONE

            tvDescription.visibility = View.VISIBLE
            tvItemName.visibility = View.VISIBLE
        }

    }

    private fun showImageFromFile(img: ImageView, f: File) {
        img.setImageURI(Uri.fromFile(f))
        img.setBackgroundColor(Color.RED)
    }


}