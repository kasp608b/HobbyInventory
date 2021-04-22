package com.android.hobbyinventory.model

import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BECollection ( @PrimaryKey(autoGenerate = true) var id:Int,
    var name: String,
    ) : Serializable{

}