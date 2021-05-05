package com.android.hobbyinventory.gui

import android.content.Context
import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.android.hobbyinventory.R
import com.android.hobbyinventory.model.BECollection
import com.android.hobbyinventory.model.BEItem
import com.android.hobbyinventory.model.CollectionWithItems
import com.android.hobbyinventory.model.HobbyinventoryRepository
import kotlinx.android.synthetic.main.activity_collection_detail.*
import kotlinx.android.synthetic.main.collection_list_cell.*
import java.io.File

class CollectionDetailActivity : AppCompatActivity() { //error
    val REQUEST_CODE_ANSWER = 10
    private lateinit var collection: BECollection
    private var newBool = false
    private var returnFromItemBool = false
    private var cache: List<BEItem>? = null

    /**
     * sets up the activity on start up and initializes the database
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_detail)
        refresh()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * when the app starts, call refresh function
     */
    override fun onStart() {
        super.onStart()
        refresh()
    }

    /**
     * returns the user to the previous activity with an intent of which collection should be displayed
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }



    /**
     * Refresh function grabs data from the database, also reimplements variables when changing view
     */
    private fun refresh()
    {
        if(!returnFromItemBool)
        {
            if(intent.extras != null)
            {
                val extras: Bundle = intent.extras!!

                collection = extras.getSerializable("collection") as BECollection
                newBool = extras.getSerializable("new") as Boolean
                if(newBool)
                {
                    sEditCollection.isChecked = true
                    onCheckedChange(sEditCollection.isChecked)
                }

                tvHeader.text = collection.name
                onCheckedChange(sEditCollection.isChecked)

                val mRep = HobbyinventoryRepository.get()
                val nameObserver = Observer<CollectionWithItems>{ CwI ->

                    var asArray : Array<BEItem> = emptyArray()
                    if(CwI != null)
                    {
                        cache = CwI.items
                        asArray = CwI.items.toTypedArray()
                        tvItemTotalCollectionList.text = CwI.items.size.toString()

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
        }

        else if (intent.extras == null)
        {
            sEditCollection.isChecked = true
            onCheckedChange(sEditCollection.isChecked)
        }

        sEditCollection.setOnCheckedChangeListener{ view, isChecked -> onCheckedChange(isChecked)

        }

    }

    /**
     * starts the item detail activity
     * sets the intent to the clicked item and a boolean to explain that the user clicked an existing item
     */
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

    /**
     * changes the view between edit 0and display mode
     */
    private fun onCheckedChange( checked: Boolean) {
        if(checked)
        {
            btnSaveCollection.visibility = View.VISIBLE
            tvCreateButton.visibility = View.GONE
            etHeader.visibility = View.VISIBLE
            etHeader.setText(collection.name)

            tvHeader.visibility = View.GONE



        } else {
            btnSaveCollection.visibility = View.GONE
            tvCreateButton.visibility = View.VISIBLE
            etHeader.visibility = View.GONE

            tvHeader.visibility = View.VISIBLE
        }

    }


    /** starts the item detail activity
     * sets the intent to a new item and a boolean to explain that the user creates a new item
     */
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
            startActivityForResult(intent, REQUEST_CODE_ANSWER)

        }

    }

    /**
     * sets up the list of items on a collection
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ANSWER) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    val collectionId = data.extras?.get("collectionId") as Int
                    onCheckedChange(sEditCollection.isChecked)
                    newBool = false
                    returnFromItemBool = true

                    val mRep = HobbyinventoryRepository.get()
                    val nameObserver = Observer<CollectionWithItems>{ CwI ->

                        var asArray : Array<BEItem> = emptyArray()
                        if(CwI != null) {
                            collection =  CwI.collection
                            tvHeader.text = collection.name
                            cache = CwI.items //error
                            asArray = CwI.items.toTypedArray()
                            tvItemTotalCollectionList.text = CwI.items.size.toString()
                        }

                        val adapter: ListAdapter = ItemAdapter(
                                this,
                                asArray
                        )
                        itemList.adapter = adapter
                    }

                    mRep.getCollectionWithItemsById(collectionId).observe(this, nameObserver)

                    itemList.onItemClickListener = AdapterView.OnItemClickListener { _, view, pos,_  -> onListItemClick(view)}

                }
            }
        }

    }

    /**
     * saves collection changes and sets the collection text fields
     */
    fun onClickSave(view: View) {
        val rep = HobbyinventoryRepository.get()

        if(!etHeader.text.isBlank() && !newBool)
        {
            collection.name = etHeader.text.toString()
            rep.updateCollection(collection)

            tvHeader.text = collection.name


            sEditCollection.isChecked = false
            onCheckedChange(sEditCollection.isChecked)
        }else if(!etHeader.text.isBlank() && newBool){
            newBool = false
            collection.name = etHeader.text.toString()

           var id = rep.insertCollection(collection).toString()

            collection.id = id.toInt()

            tvHeader.text = collection.name


            sEditCollection.isChecked = false
            onCheckedChange(sEditCollection.isChecked)
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

    /**
     * deletes the collection and sends the user back to the main activity
     */
    fun onClickDelete(view: View) {

        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.yes,
                        DialogInterface.OnClickListener { dialog, id ->
                            // User clicked OK button
                            val rep = HobbyinventoryRepository.get()
                            rep.deleteCollection(collection)
                            finish()
                        })
                setNegativeButton(R.string.no,
                        DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog

                        })
            }

            builder.setMessage(R.string.dialog_confirm_deleteCollection)

            // Create the AlertDialog
            builder.create()
        }

        if(newBool){
            Toast.makeText(
                    this,
                    "nothing to delete",
                    Toast.LENGTH_LONG
            ).show()
        } else
        {
            alertDialog?.show()
        }

    }

    /**
     * defines how the collection list is displayed
     */
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
            //mImage.setRotation(90);

        }


    }

}