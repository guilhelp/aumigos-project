package com.example.inventory.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.Pet
import com.example.inventory.data.PetsRepository

class PetEntryViewModel(private val petsRepository: PetsRepository) : ViewModel() {

    var petUiState by mutableStateOf(PetUiState())
        private set

    fun updateUiState(petDetails: PetDetails) {
        petUiState =
            PetUiState(petDetails = petDetails, isEntryValid = validateInput(petDetails))
    }

    suspend fun saveItem() {
        if (validateInput()) {
            petsRepository.insertPet(petUiState.petDetails.toPet())
        }
    }

    private fun validateInput(uiState: PetDetails = petUiState.petDetails): Boolean {
        return with(uiState) {
            nome.isNotBlank() && idade > 0 && foto.isNotBlank()
        }
    }
}

data class PetUiState(
    val petDetails: PetDetails = PetDetails(),
    val isEntryValid: Boolean = false
)

data class PetDetails(
    val id: Int = 0,
    val nome: String = "",
    val idade: Int = 0,
    val foto: String = "",
)

fun PetDetails.toPet(): Pet = Pet(
    id = id,
    nome = nome,
    idade = idade,
    foto = foto,
)

fun Pet.toPetUiState(isEntryValid: Boolean = false): PetUiState = PetUiState(
    petDetails = this.toPetDetails(),
    isEntryValid = isEntryValid
)

fun Pet.toPetDetails(): PetDetails = PetDetails(
    id = id,
    nome = nome,
    idade = idade,
    foto = foto
)
