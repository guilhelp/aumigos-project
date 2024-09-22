package com.example.inventory.ui.item

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.PetsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val petsRepository: PetsRepository,
) : ViewModel() {

    private val petId: Int = checkNotNull(savedStateHandle[PetDetailsDestination.petIdArg])

    val uiState: StateFlow<PetDetailsUiState> =
        petsRepository.getPetStream(petId)
            .filterNotNull()
            .onEach { pet ->
                Log.d("PetDetailsViewModel", "Pet retrieved: $pet")
            }
            .map {
                PetDetailsUiState(petDetails = it.toPetDetails())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PetDetailsUiState()
            )


    suspend fun deletePet() {
        petsRepository.deletePet(uiState.value.petDetails.toPet())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class PetDetailsUiState(
    val petDetails: PetDetails = PetDetails()
)
