package com.android.hobbyinventory.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.concurrent.Executors

class HobbyinventoryRepository private constructor(private val context: Context) {

    private val database: HobbyinventoryDatabase = Room.databaseBuilder(context.applicationContext,
        HobbyinventoryDatabase::class.java,
        "HobbyinventoryDatabase").build()

    //Gets the given instance of HobbyinventoryDao
    private val HobbyinventoryDao = database.HobbyinventoryDao()

    //Gets all BECollection objects.
    fun getAllCollection(): LiveData<List<BECollection>> = HobbyinventoryDao.getAllCollections()

    //Gets specific Collection object by the given ID.
    fun getCollectionById(id: Int) = HobbyinventoryDao.getCollectionById(id)

    //Executor tracks threads.
    private val executor = Executors.newSingleThreadExecutor()

    //Inserts the given Collection object.
    fun insert(p: BECollection) {
        executor.execute{ HobbyinventoryDao.insertCollection(p) }
    }

    //Updates the given Collection object.
    fun update(p: BECollection) {
        executor.execute { HobbyinventoryDao.updateCollection(p) }
    }

    //Deletes the given Collection object.
    fun delete(p: BECollection) {
        executor.execute { HobbyinventoryDao.deleteCollection(p) }
    }

    //Deletes all Collections.
    fun clearCollections() {
        executor.execute { HobbyinventoryDao.deleteAllCollections() }
    }

    //Items
    //..........................................................................................................................

    //Gets all BEItems objects.
    fun getAllItems(): LiveData<List<BEItem>> = HobbyinventoryDao.getAllItems()

    //Gets specific Item object by the given ID.
    fun getItemById(id: Int) = HobbyinventoryDao.getItemById(id)

    //Inserts the given Collection object.
    fun insert(p: BEItem) {
        executor.execute{ HobbyinventoryDao.insertItem(p) }
    }

    //Updates the given Collection object.
    fun update(p: BEItem) {
        executor.execute { HobbyinventoryDao.updateItem(p) }
    }

    //Deletes the given Collection object.
    fun delete(p: BEItem) {
        executor.execute { HobbyinventoryDao.deleteItem(p) }
    }

    //Deletes all Collections.
    fun clearItems() {
        executor.execute { HobbyinventoryDao.deleteAllItems() }
    }
    //CollectionWithItems
    //...............................................................................................................................

    //Get all Collections with items
    fun getCollectionsWithItems() {
        executor.execute { HobbyinventoryDao.getCollectionsWithItems() }
    }

    //Get specific collection with items by the given id
    fun  getCollectionWithItemsById(id: Int){
        executor.execute{HobbyinventoryDao.getCollectionWithItemsById(id)}

    }

    //Tracks whether or not an instance has been created already.
    companion object {
        private var Instance: HobbyinventoryRepository? = null

        //When called, checks if an instance already exists, if not, create one.
        fun initialize(context: Context) {
            if (Instance == null)
                Instance = HobbyinventoryRepository(context)
        }

        //If an instance is not equal to null, return the instance.
        fun get(): HobbyinventoryRepository {
            if (Instance != null) return Instance!!
            throw IllegalStateException("Person repo not initialized")
        }
    }


}