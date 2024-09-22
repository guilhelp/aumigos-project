package com.example.inventory.data

import android.content.Context

interface AppContainer {
    val petsRepository: PetsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val petsRepository: PetsRepository by lazy {
        OfflinePetsRepository(InventoryDatabase.getDatabase(context).petDao())
    }
}
