package com.android.hobbyinventory.model

import androidx.room.Database
import androidx.room.RoomDatabase

//Defines the FriendDatabase, it is a Room Database.
@Database(entities = [BECollection::class,BEItem::class], version=1)
abstract class HobbyinventoryDatabase : RoomDatabase() {

    //Return the HobbyinventoryDao
    abstract fun HobbyinventoryDao(): HobbyinventoryDao
}