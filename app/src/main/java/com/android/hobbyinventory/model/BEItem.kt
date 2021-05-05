package com.android.hobbyinventory.model

import java.io.Serializable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * This is the class that defines the BEItem in the database
 * Here the relations are also defined between the objects
 */
@Entity(foreignKeys = arrayOf(ForeignKey(entity = BECollection::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("collectionid"),
        onDelete = ForeignKey.CASCADE)))
data class BEItem ( @PrimaryKey(autoGenerate = true ) var id:Int,
               var collectionid: Int,
               var name: String,
               var desc: String,
               var pictureFile: String?
) : Serializable {
}