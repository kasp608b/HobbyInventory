package com.android.hobbyinventory.model

import androidx.lifecycle.LiveData
import androidx.room.*

//This class defines all DB operations, also an interface.
@Dao
interface HobbyinventoryDao {
    //Collections
    //..............................................................................................................................
    //Gets all BECollection objects.
    @Query("SELECT * from BECollection order by id")
    fun getAllCollections(): LiveData<List<BECollection>>

    //Gets specific Collection object by the given ID.
    @Query("SELECT * from BECollection where id = (:id)")
    fun getCollectionById(id: Int): LiveData<BECollection>

    //Inserts the given Collection object.
    @Insert
    fun insertCollection(p: BECollection)

    //Updates the given Collection object.
    @Update
    fun updateCollection(p: BECollection)

    //Deletes the given Collection object.
    @Delete
    fun deleteCollection(p: BECollection)

    //Deletes all Collections.
    @Query("DELETE from BECollection")
    fun deleteAllCollections()

    //Items
    //...............................................................................................................................
    @Query("SELECT * from BEItem order by id")
    fun getAllItems(): LiveData<List<BEItem>>

    //Gets specific Collection object by the given ID.
    @Query("SELECT * from BEItem where id = (:id)")
    fun getItemById(id: Int): LiveData<BEItem>

    //Inserts the given Collection object.
    @Insert
    fun insertItem(p: BEItem)

    //Updates the given Collection object.
    @Update
    fun updateItem(p: BEItem)

    //Deletes the given Collection object.
    @Delete
    fun deleteItem(p: BEItem)

    //Deletes all Collections.
    @Query("DELETE from BEItem")
    fun deleteAllItems()

    //CollectionWithItems
    //...............................................................................................................................
    @Transaction
    @Query("SELECT * FROM BECollection")
    fun getCollectionsWithItems(): List<CollectionWithItems>

    @Transaction
    @Query("SELECT * FROM BECollection where id = (:id)")
    fun getCollectionWithItemsById(id: Int): LiveData<BECollection>



}