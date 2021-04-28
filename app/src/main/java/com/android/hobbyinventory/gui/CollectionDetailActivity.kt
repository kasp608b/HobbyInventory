package com.android.hobbyinventory.gui

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import com.android.hobbyinventory.R
import com.android.hobbyinventory.model.BECollection
import com.android.hobbyinventory.model.BEItem
import com.android.hobbyinventory.model.CollectionWithItems
import com.android.hobbyinventory.model.HobbyinventoryRepository
import kotlinx.android.synthetic.main.activity_collection_detail.*
import kotlinx.android.synthetic.main.activity_item_detail.*
import java.io.File

class CollectionDetailActivity : AppCompatActivity() { //error
    private lateinit var collection: BECollection
    private var newBool = false
    private var cache: List<BEItem>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_detail)
        refresh()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        refresh()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }



    private fun refresh()
    {
        if(intent.extras != null) {
            val extras: Bundle = intent.extras!!

            collection = extras.getSerializable("collection") as BECollection
            newBool = extras.getSerializable("new") as Boolean
            if(newBool)
            {
                sEdit2.isChecked = true
                onCheckedChange(sEdit2.isChecked)


            }

            tvHeader.text = collection.name
            onCheckedChange(sEdit2.isChecked)

            val mRep = HobbyinventoryRepository.get()
            val nameObserver = Observer<CollectionWithItems>{ CwI ->

                var asArray : Array<BEItem> = emptyArray()
                if(CwI != null) {
                    cache = CwI.items //error
                    asArray = CwI.items.toTypedArray()
                }

                val adapter: ListAdapter = ItemAdapter(
                    this,
                    asArray
                )
                itemList.adapter = adapter
            }

            mRep.getCollectionWithItemsById(collection.id).observe(this, nameObserver)

            itemList.onItemClickListener = AdapterView.OnItemClickListener { _, view, pos,_  -> onListItemClick(view)}

        }

        else if (intent.extras == null)
        {
            sEdit2.isChecked = true
            onCheckedChange(sEdit2.isChecked)
        }

        sEdit2.setOnCheckedChangeListener{ view, isChecked -> onCheckedChange(isChecked)

        }

    }

    private fun onListItemClick(view: View)
    {

        // Toast.makeText(this, "Ye hath Clicked on a collection", Toast.LENGTH_SHORT).show()
        val item = view.tag as BEItem
        Log.d("xyz",item.toString() )

        val intent = Intent(this, ItemDetailActivity::class.java)
        intent.putExtra("item", item)
        intent.putExtra("new", false)
        startActivity(intent)

    }

    private fun onCheckedChange( checked: Boolean) {
        if(checked)
        {
            btnSaveCollection.visibility = View.VISIBLE
            tvCreateButton.visibility = View.GONE
            etHeader.visibility = View.VISIBLE

            tvHeader.visibility = View.GONE


        } else {
            btnSaveCollection.visibility = View.GONE
            tvCreateButton.visibility = View.VISIBLE
            etHeader.visibility = View.GONE

            tvHeader.visibility = View.VISIBLE
        }

    }

    //Goes back to previous screen after closing the current window.
    fun onClickBackCol(view: View) { finish() }

    fun onClickCreateItem(view: View) {

        if(newBool){
            Toast.makeText(
                this,
                "Cannot create item,because collection dosen't exist",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val intent = Intent(this, ItemDetailActivity::class.java)
            val item = BEItem(0,collection.id,"","",null)
            intent.putExtra("item", item)
            intent.putExtra("new", true)
            startActivity(intent)

        }

    }

    // saves item changes and sets the item text fields
    fun onClickSave(view: View) {
        val rep = HobbyinventoryRepository.get()

        if(!etHeader.text.isBlank() && !newBool)
        {
            collection.name = etHeader.text.toString()
            rep.updateCollection(collection)

            tvHeader.text = collection.name


            sEdit2.isChecked = false
            onCheckedChange(sEdit2.isChecked)
        }else if(!etHeader.text.isBlank() && newBool){
            collection.name = etHeader.text.toString()
            rep.insertCollection(collection)

            tvHeader.text = collection.name


            sEdit.isChecked = false
            onCheckedChange(sEdit.isChecked)
        }
        else
        {
            Toast.makeText(
                    this,
                    "Collection cant have blank fields",
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    internal class ItemAdapter(context: Context,
                               private val items: Array<BEItem>
    ) : ArrayAdapter<BEItem>(context, 0, items)
    {

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null)
            {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.item_list_cell, null)

            }

            val resView: View = v1!!

            val itm = items[position]
            val nameView = resView.findViewById<TextView>(R.id.tvNameExt)

            val pictureView = resView.findViewById<ImageView>(R.id.FriendPicture)
            nameView.text = itm.name

            if(itm.pictureFile != null)
            {
                val File = File(itm.pictureFile!!)
                showImageFromFile(pictureView, File)
            }

            resView.tag = itm


            return resView
        }

        // show the image allocated in [f] in imageview [img]. Show meta data in [txt]
        private fun showImageFromFile(img: ImageView,  f: File) {
            img.setImageURI(Uri.fromFile(f))
            img.setBackgroundColor(Color.RED)
            //mImage.setRotation(90);

        }


    }

}