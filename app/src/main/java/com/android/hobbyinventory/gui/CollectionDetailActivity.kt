package com.android.hobbyinventory.gui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Toast
import com.android.hobbyinventory.R
import com.android.hobbyinventory.model.BECollection
import com.android.hobbyinventory.model.BEItem
import com.android.hobbyinventory.model.CollectionWithItems
import com.android.hobbyinventory.model.HobbyinventoryRepository
import kotlinx.android.synthetic.main.activity_collection_detail.*
import kotlinx.android.synthetic.main.activity_item_detail.*
import java.io.File

class CollectionDetailActivity : AppCompatActivity() {
    private lateinit var collection: BECollection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_detail)

        if(!sEdit2.isChecked && intent.extras != null) {
            val extras: Bundle = intent.extras!!

            collection = extras.getSerializable("item") as BECollection
            tvHeader.text = collection.name


            btnSaveCollection.visibility = View.GONE
            etHeader.visibility = View.GONE

        }
        else if (intent.extras == null)
        {
            sEdit2.isChecked = true
            onCheckedChange(sEdit2.isChecked)
        }

        sEdit2.setOnCheckedChangeListener{ view, isChecked -> onCheckedChange(isChecked)

        }
    }

    private fun onCheckedChange( checked: Boolean) {
        if(checked)
        {
            btnSaveCollection.visibility = View.VISIBLE
            etHeader.visibility = View.VISIBLE

            tvHeader.visibility = View.GONE


        } else {
            btnSaveCollection.visibility = View.GONE
            etHeader.visibility = View.GONE

            tvHeader.visibility = View.VISIBLE
        }

    }
    fun onClickCreateItem(view: View) {
        val intent = Intent(this, ItemDetailActivity::class.java)
        val item = BEItem(0,collection.id,"","","")
        intent.putExtra("item", item)
        startActivity(intent)



    }


}