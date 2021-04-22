package com.android.hobbyinventory.gui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.hobbyinventory.R
import com.android.hobbyinventory.model.BECollection
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
        refresh()
    }

    private fun refresh() {
        var listofCollections: List<BECollection> = listOf(BECollection(1, "Figures"), BECollection(2, "Manga"))

        val asArray = listofCollections.toTypedArray()
        val adapter: ListAdapter = CollectionsAdapter(this, asArray)
        collectionList.adapter = adapter

        collectionList.onItemClickListener = AdapterView.OnItemClickListener { _, view, pos, _ -> onListItemClick(view)}
    }

    fun onClickCreate(view: View) {}

    fun onListItemClick(view: View) {
        Toast.makeText(this, "Ye hath Clicked on a collection", Toast.LENGTH_SHORT).show()
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
            resView.setBackgroundColor(colours[position % colours.size])
            val f = collections[position]
            val nameView = resView.findViewById<TextView>(R.id.tvNameExt)
            nameView.text = f.name

            return resView
        }
    }
}