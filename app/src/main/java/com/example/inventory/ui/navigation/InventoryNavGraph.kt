package com.example.inventory.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.home.HomeScreen
import com.example.inventory.ui.item.PetDetailsDestination
import com.example.inventory.ui.item.PetDetailsScreen
import com.example.inventory.ui.item.PetEditDestination
import com.example.inventory.ui.item.PetEditScreen
import com.example.inventory.ui.item.PetEntryDestination
import com.example.inventory.ui.item.PetEntryScreen

@Composable
fun InventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToPetEntry = { navController.navigate(PetEntryDestination.route) },
                navigateToPetUpdate = { id ->
                    Log.d("HomeScreen", "Navigating to edit pet with ID: $id")
                    navController.navigate("${PetDetailsDestination.route}/$id")
                }
            )
        }
        composable(route = PetEntryDestination.route) {
            PetEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = PetDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(PetDetailsDestination.petIdArg) {
                type = NavType.IntType
            })
        ) {
            PetDetailsScreen(
                navigateToEditPet = { navController.navigate("${PetEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = PetEditDestination.routeWithArgs,
            arguments = listOf(navArgument(PetEditDestination.petIdArg) {
                type = NavType.IntType
            })
        ) {
            PetEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
