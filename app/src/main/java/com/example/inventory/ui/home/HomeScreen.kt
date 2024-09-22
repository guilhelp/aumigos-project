package com.example.inventory.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Pet
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

val pawPrintFontFamily = FontFamily(
    Font(R.font.paw_print)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToPetEntry: () -> Unit,
    navigateToPetUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(HomeDestination.titleRes),
                        style = TextStyle(
                            fontFamily = pawPrintFontFamily,
                            fontSize = 60.sp
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToPetEntry,
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.pet_entry_title),
                    modifier = Modifier
                        .padding(8.dp) // Ajusta o espaçamento dentro do ícone
                        .border(
                            BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary), // Define a borda arredondada
                            shape = RoundedCornerShape(50) // Define a borda totalmente arredondada
                        )
                )
            }

        },
    ) { innerPadding ->
        HomeBody(
            petList = homeUiState.petList,
            onItemClick = navigateToPetUpdate,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}


@Composable
private fun HomeBody(
    petList: List<Pet>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (petList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            InventoryList(
                petList = petList,
                onItemClick = { onItemClick(it.id) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun InventoryList(
    petList: List<Pet>,
    onItemClick: (Pet) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = petList, key = { it.id }) { pet ->
            InventoryItem(pet = pet,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(pet) })
        }
    }
}

@Composable
private fun InventoryItem(
    pet: Pet,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
            .heightIn(min = 250.dp), // Ajuste a altura mínima do card
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column (
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberImagePainter(pet.foto),
                contentDescription = pet.nome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )

            Column(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_large))
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = pet.nome,
                        style = TextStyle(
                            fontWeight = FontWeight.W700,
                            fontSize = 30.sp
                        )
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = pet.idade.toString(),
                        style = TextStyle(
                            fontFamily = pawPrintFontFamily,
                            fontSize = 60.sp
                        ),
                    )
                }

            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    InventoryTheme {
        HomeBody(listOf(
            Pet(1, "Billy", 5, "https://www.equilibrio-petfood.com/wp-content/uploads/2023/02/image-129.png"),
            Pet(2, "Billy", 5, "https://www.equilibrio-petfood.com/wp-content/uploads/2023/02/image-129.png"),
            Pet(3, "Billy", 5, "https://www.equilibrio-petfood.com/wp-content/uploads/2023/02/image-129.png"),
        ), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    InventoryTheme {
        HomeBody(listOf(), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryItemPreview() {
    InventoryTheme {
        InventoryItem(
            Pet(1, "Billy", 5, "teste"),
        )
    }
}
