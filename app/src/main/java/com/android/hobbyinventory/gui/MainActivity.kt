package com.android.hobbyinventory.gui

import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.android.hobbyinventory.model.CollectionWithItems
import com.android.hobbyinventory.model.HobbyinventoryRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**
     * List of collections
     */
    var collections: List<BECollection>? = null
    val TAG = "xyz"
    val tempCol: MutableList<BECollection> = mutableListOf()
    val tempItemTotal: MutableList<Int> = mutableListOf()
    var varSavedInstanceState: Bundle? = null

    /**
     * sets up the activity on start up and initializes the database
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HobbyinventoryRepository.initialize(this)
        this.varSavedInstanceState = savedInstanceState
        refresh(this.varSavedInstanceState)
    }


    private fun refresh(savedInstanceState: Bundle?) {
        if (savedInstanceState != null)
        {
            val asArray = savedInstanceState.getSerializable("collections") as Array<BECollection>
            val asArrayInts = savedInstanceState.getSerializable("itemTotals") as Array<Int>
            val adapter: ListAdapter = CollectionsAdapter(
                    this,
                    asArray,
                    asArrayInts
            )
            collectionList.adapter = adapter

            collectionList.onItemClickListener = AdapterView.OnItemClickListener { _, view, pos, _ -> onListItemClick(view)}
        } else {
            val mRep = HobbyinventoryRepository.get()
            val CollectionObserver = Observer<List<CollectionWithItems>>{ c ->
                this.tempCol.clear()
                this.tempItemTotal.clear()
                for (CwI in c)
                {
                    this.tempCol.add(CwI.collection)
                    this.tempItemTotal.add(CwI.items.count())
                }
                val asArray = this.tempCol.toTypedArray()
                val asArrayInts = this.tempItemTotal.toTypedArray()
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "list is saved")
        outState.putSerializable("collections", this.tempCol.toTypedArray())
        outState.putSerializable("itemTotals", this.tempItemTotal.toTypedArray())
    }

    fun onClickCreate(view: View) {
        val intent = Intent(this, CollectionDetailActivity::class.java)
        val collection = BECollection(0, "DefaultCollection")
        intent.putExtra("collection", collection)
        intent.putExtra("new", true)
        startActivity(intent)
    }

    fun onListItemClick(view: View) {

        val collection = view.tag as BECollection
        Log.d(TAG,collection.toString() )

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
        refresh(null)
    }


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