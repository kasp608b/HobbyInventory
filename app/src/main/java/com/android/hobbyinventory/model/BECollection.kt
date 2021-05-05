package com.android.hobbyinventory.model

import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This class defines the BECollection Entity in the database
 * The id in this class is set to auto increment
 */
@Entity
data class BECollection
    (@PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    ) : Serializable{

}