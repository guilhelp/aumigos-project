package com.example.inventory.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventory.InventoryApplication
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.item.PetDetailsViewModel
import com.example.inventory.ui.item.PetEditViewModel
import com.example.inventory.ui.item.PetEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            PetEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.petsRepository
            )
        }

        initializer {
            PetEntryViewModel(inventoryApplication().container.petsRepository)
        }


        initializer {
            PetDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.petsRepository
            )
        }


        initializer {
            HomeViewModel(inventoryApplication().container.petsRepository)
        }
    }
}

fun CreationExtras.inventoryApplication(): InventoryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)
