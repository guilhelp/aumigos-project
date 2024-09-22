package com.example.inventory.ui.item

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme
import kotlinx.coroutines.launch

object PetEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = R.string.edit_item_title
    const val petIdArg = "petId"
    val routeWithArgs = "$route/{$petIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PetEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                viewModel.updateImageUri(it.toString())
            }
        }
    )

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(PetEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            selectedImageUri?.let { uri ->
                Box(
                    modifier = Modifier
                        .size(400.dp) // Tamanho da imagem
                        .clip(MaterialTheme.shapes.extraLarge)
                        .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
                )
            }

            if (viewModel.petUiState.petDetails.foto.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(viewModel.petUiState.petDetails.foto),
                    contentDescription = viewModel.petUiState.petDetails.nome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            PetEntryBody(
                petUiState = viewModel.petUiState,
                onItemValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updatePet()
                        navigateBack()
                    }
                },
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetEditScreenPreview() {
    InventoryTheme {
       PetEditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
    }
}
