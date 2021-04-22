package com.android.hobbyinventory.model

import androidx.room.Embedded
import androidx.room.Relation

data class CollectionWithItems(
    @Embedded val collection: BECollection,
    @Relation(
        parentColumn = "id",
        entityColumn = "collectionid"
    )
    val items: List<BEItem>
)
