package com.example.doosen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doosen.ui.DetailMahasiswaScreen
import com.example.doosen.ui.LoginScreen
import com.example.doosen.ui.MainScreen
import com.example.doosen.theme.DOOSENTheme
import com.example.doosen.ui.Screen
import com.example.doosen.ui.currentRoute
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

import com.example.doosen.ui.*




@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetoranDosenApp()
        }
    }
}

// ✅ DI LUAR CLASS
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetoranDosenApp() {
    val navController = rememberNavController()
    val vm: AuthViewModel = hiltViewModel()
    val currentRoute = currentRoute(navController) ?: ""
    Log.d("AppStart", "Current route: $currentRoute")


    val showTopBar = currentRoute !in listOf(Screen.Login.route, Screen.DetailMahasiswa.route)
    val showBottomBar = currentRoute in listOf(Screen.Beranda.route, Screen.Profil.route)

    DOOSENTheme {
        Scaffold(
            topBar = {
                if (showTopBar) {
                    TopAppBar(
                        title = { Text(getTopBarTitle(currentRoute)) },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF00695C))
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    BottomNavBar(navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Login.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Login.route) {
                    LoginScreen(nav = navController)
                }
                composable(Screen.Beranda.route) {
                    MainScreen(navController = navController, vm = vm)
                }
                composable(Screen.Profil.route) {
                    MainScreen(navController = navController, vm = vm)
                }
                composable(
                    route = Screen.DetailMahasiswa.route,
                    arguments = listOf(navArgument("nim") { type = NavType.StringType })
                ) { backStackEntry ->
                    val nim = backStackEntry.arguments?.getString("nim") ?: ""
                    DetailMahasiswaScreen(nim = nim, vm = vm)
                }
            }
        }
    }
}

fun getTopBarTitle(route: String?): String {
    return when (route) {
        Screen.Beranda.route -> Screen.Beranda.title
        Screen.Profil.route -> Screen.Profil.title
        else -> "Setoran Hafalan"
    }
}
