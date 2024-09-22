package com.example.inventory.ui.item

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme
import kotlinx.coroutines.launch

object PetEntryDestination : NavigationDestination {
    override val route = "item_entry"
    override val titleRes = R.string.pet_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: PetEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(PetEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        PetEntryBody(
            petUiState = viewModel.petUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be saved in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun PetEntryBody(
    petUiState: PetUiState,
    onItemValueChange: (PetDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        PetInputForm(
            petDetails = petUiState.petDetails,
            onValueChange = onItemValueChange,
            onImageSelected = { uri ->
                uri?.let {
                    onItemValueChange(petUiState.petDetails.copy(foto = it.toString())) // Converte a URI para String
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = petUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@Composable
fun PetInputForm(
    petDetails: PetDetails,
    modifier: Modifier = Modifier,
    onValueChange: (PetDetails) -> Unit = {},
    enabled: Boolean = true,
    onImageSelected: (Uri?) -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                onImageSelected(it)
                selectedImageUri = it
            }
        }
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = petDetails.nome,
            onValueChange = { onValueChange(petDetails.copy(nome = it)) },
            label = { Text(stringResource(R.string.pet_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = petDetails.idade.toString(),
            onValueChange = {
                val idadeValue = it.toIntOrNull() ?: 0
                if (idadeValue in 0..30) {
                    onValueChange(petDetails.copy(idade = idadeValue))
                } else if (it.isEmpty()) {
                    onValueChange(petDetails.copy(idade = 0))
                }
            },
            label = { Text(stringResource(R.string.pet_idade_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        Button(
            modifier = Modifier
                .fillMaxSize(),
            onClick = {
                imagePickerLauncher.launch("image/*") })
        {
            Text(text = stringResource(R.string.select_image))
        }

        selectedImageUri?.let { uri ->
            Box(
                modifier = Modifier
                    .size(400.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
            ) {
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = stringResource(R.string.selected_image),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.extraLarge)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun PetEntryScreenPreview() {
    InventoryTheme {
        PetEntryBody(petUiState = PetUiState(
            PetDetails(
                nome = "Billy", idade = 5, foto = "https://www.equilibrio-petfood.com/wp-content/uploads/2023/02/image-129.png"
            )
        ), onItemValueChange = {}, onSaveClick = {})
    }
}
