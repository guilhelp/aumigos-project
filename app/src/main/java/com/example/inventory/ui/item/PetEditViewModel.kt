package com.example.inventory.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.PetsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PetEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val petsRepository: PetsRepository
) : ViewModel() {

    var petUiState by mutableStateOf(PetUiState())
        private set

    private val petId: Int = checkNotNull(savedStateHandle[PetEditDestination.petIdArg])

    init {
        viewModelScope.launch {
            petUiState = petsRepository.getPetStream(petId)
                .filterNotNull()
                .first()
                .toPetUiState(true)

        }
    }

    suspend fun updatePet() {
        if (validateInput(petUiState.petDetails)) {
            petsRepository.updatePet(petUiState.petDetails.toPet())
        }
    }

    fun updateUiState(petDetails: PetDetails) {
        petUiState = PetUiState(petDetails = petDetails, isEntryValid = validateInput(petDetails))
    }

    fun updateImageUri(imageUri: String) {
        petUiState = petUiState.copy(
            petDetails = petUiState.petDetails.copy(foto = imageUri)
        )
    }

    private fun validateInput(uiState: PetDetails = petUiState.petDetails): Boolean {
        return with(uiState) {
            nome.isNotBlank() && idade > 0 && foto.isNotBlank()
        }
    }
}
