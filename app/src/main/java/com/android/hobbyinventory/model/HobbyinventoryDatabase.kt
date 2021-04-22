package com.android.hobbyinventory.model

import androidx.room.Database

//Defines the FriendDatabase, it is a Room Database.
@Database(entities = [BECollection::class,BEItem::class], version=1)
class HobbyinventoryDatabase {
}