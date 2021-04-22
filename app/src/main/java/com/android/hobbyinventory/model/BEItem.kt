package com.android.hobbyinventory.model

import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BEItem ( @PrimaryKey(autoGenerate = true) var id:Int,
               var collectionid: Int,
               var name: String,
               var desc: String,
               var pictureFile: String?
) : Serializable {
}