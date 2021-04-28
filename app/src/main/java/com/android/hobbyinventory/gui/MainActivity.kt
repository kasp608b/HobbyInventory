package com.android.hobbyinventory.gui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import com.android.hobbyinventory.R
import com.android.hobbyinventory.model.BECollection
import com.android.hobbyinventory.model.BEItem
import com.android.hobbyinventory.model.CollectionWithItems
import com.android.hobbyinventory.model.HobbyinventoryRepository
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    /**
     * List of collections
     */
    var collections: List<BECollection>? = null

    /**
     * sets up the activity on start up and initializes the database
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HobbyinventoryRepository.initialize(this)

        refresh()
    }

    private fun refresh() {
        //var listofCollections: List<BECollection> = listOf(BECollection(1, "Figures"), BECollection(2, "Manga"))
        val mRep = HobbyinventoryRepository.get()

        //mRep.insertCollection(BECollection(0, "Figures"))
        //mRep.insertCollection(BECollection(0, "Manga"))

       //mRep.insertItem(BEItem(0, 2,"Gyo","",null))

       //mRep.insertItem(BEItem(0, 1,"smurf","",null))






        val CollectionObserver = Observer<List<BECollection>>{ c ->
            collections = c;
            val asArray = c.toTypedArray()
            val adapter: ListAdapter = CollectionsAdapter(
                this,
                asArray
            )
            collectionList.adapter = adapter
        }
        mRep.getAllCollection().observe(this, CollectionObserver)


        collectionList.onItemClickListener = AdapterView.OnItemClickListener { _, view, pos, _ -> onListItemClick(view)}
    }

    fun onClickCreate(view: View) {
        val intent = Intent(this, CollectionDetailActivity::class.java)
        val collection = BECollection(0, "DefaultCollection")
        intent.putExtra("collection", collection)
        intent.putExtra("new", true)
        startActivity(intent)
    }

    fun onListItemClick(view: View) {
       // Toast.makeText(this, "Ye hath Clicked on a collection", Toast.LENGTH_SHORT).show()
        val collection = view.tag as BECollection
        Log.d("xyz",collection.toString() )

        val intent = Intent(this, CollectionDetailActivity::class.java)
        intent.putExtra("collection", collection)
        intent.putExtra("new", false)
        startActivity(intent)

    /*
        var items: List<BEItem>? = null

        val ItemsObserver = Observer<CollectionWithItems>{ c ->
            items = c.items

            Log.d("xyz",items.toString() )

        }
        mRep.getCollectionWithItemsById(collection.id).observe(this, ItemsObserver)
    */
    }

    override fun onStart() {
        super.onStart()
        refresh()
    }

    internal class CollectionsAdapter(context: Context,
                                 private val collections: Array<BECollection>
    ) : ArrayAdapter<BECollection>(context, 0, collections)
    {
        private val colours = intArrayOf(
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#CCCCCC")
        )

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.collection_list_cell, null)

            }
            val resView: View = v1!!
            //resView.setBackgroundColor(colours[position % colours.size])
            val f = collections[position]
            val nameView = resView.findViewById<TextView>(R.id.tvNameExt)
            nameView.text = f.name

            resView.tag = collections[position]

            return resView
        }
    }

}