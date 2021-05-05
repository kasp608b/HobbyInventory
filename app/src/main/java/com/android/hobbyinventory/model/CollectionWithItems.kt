package com.android.hobbyinventory.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 *This class defines the helper entity used to join the collections with their items
 */
data class CollectionWithItems(
    @Embedded val collection: BECollection,
    @Relation(
        parentColumn = "id",
        entityColumn = "collectionid"
    )
    val items: List<BEItem>
)
