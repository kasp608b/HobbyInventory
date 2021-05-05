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


    /**
     * Refresh function grabs data from the database, also reimplements variables when changing view
     */
    private fun refresh() {
        val mRep = HobbyinventoryRepository.get()
        val tempCol: MutableList<BECollection> = mutableListOf()
        val tempItemTotal: MutableList<Int> = mutableListOf()


        val CollectionObserver = Observer<List<CollectionWithItems>>{ c ->
            for (CwI in c)
            {
                tempCol.add(CwI.collection)
                tempItemTotal.add(CwI.items.count())
            }
            val asArray = tempCol.toTypedArray()
            val asArrayInts = tempItemTotal.toTypedArray()
            val adapter: ListAdapter = CollectionsAdapter(
                this,
                asArray,
                asArrayInts
            )
            collectionList.adapter = adapter
        }
        mRep.getCollectionsWithItems().observe(this, CollectionObserver)


        collectionList.onItemClickListener = AdapterView.OnItemClickListener { _, view, pos, _ -> onListItemClick(view)}
    }

    /**
     * When the create button is pressed, forwards data to Collection Detail Activity and tells it the user is creating a new collection
     */
    fun onClickCreate(view: View) {
        val intent = Intent(this, CollectionDetailActivity::class.java)
        val collection = BECollection(0, "DefaultCollection")
        intent.putExtra("collection", collection)
        intent.putExtra("new", true)
        startActivity(intent)
    }

    /**
     * when a collection item is pressed, forward name of collection and gets the matching data of the collection
     */
    fun onListItemClick(view: View) {

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

    /**
     * when the app starts, call refresh function
     */
    override fun onStart() {
        super.onStart()
        refresh()
    }


    /**
     * internal class collection adapter defines the look of the list
     */
    internal class CollectionsAdapter(context: Context,
                                 private val collections: Array<BECollection>,
                                 private val totalItem: Array<Int>,
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
            val f = collections[position]
            val t = totalItem[position]

            val nameView = resView.findViewById<TextView>(R.id.tvNameExt)
            val itemView = resView.findViewById<TextView>(R.id.tvItemTotal)
            nameView.text = f.name
            itemView.text = "- $t"


            resView.tag = collections[position]

            return resView
        }
    }

}