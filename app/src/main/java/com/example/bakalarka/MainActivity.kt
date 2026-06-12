package com.example.bakalarka

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.Screens
import com.example.bakalarka.screens.*
import com.example.bakalarka.supabase.CurrentUserHolder
import com.example.bakalarka.ui.theme.BakalarkaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val viewModel: AppViewModel by viewModels()
        viewModel.setScreenInfo(
            displayMetrics.widthPixels,
            displayMetrics.heightPixels,
            displayMetrics.density
        )

        enableEdgeToEdge()

        setContent {
            BakalarkaTheme {
                val navController = rememberNavController()
                Bakalarka(navController = navController, viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bakalarka(
    navController: NavHostController,
    viewModel: AppViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarScreens = listOf(
        Screens.Home.route,
        Screens.AddTraining.route,
        Screens.Training.route,
        Screens.Statistics.route,
        Screens.Profile.route,
        Screens.Settings.route
    )

    val showBottomBar = currentRoute in bottomBarScreens
    val showTopBar = currentRoute in bottomBarScreens

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(CurrentUserHolder.getUsername().toString())},
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.Profile.route) {
                                    popUpTo(0)
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.Settings.route) {
                                    popUpTo(0)
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },

        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.navigate(Screens.Home.route) {
                                popUpTo(0)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }

                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.navigate(Screens.AddTraining.route) {
                                popUpTo(0)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }

                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.navigate(Screens.Training.route) {
                                popUpTo(0)
                            }
                        }
                    ) {
                        Icon(
                            painterResource(id = R.drawable.dumbellicon),
                            contentDescription = "Training"
                        )
                    }

                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.navigate(Screens.Statistics.route) {
                                popUpTo(0)
                            }
                        }
                    ) {
                        Icon(
                            painterResource(id = R.drawable.graphicon),
                            contentDescription = "Statistics"
                        )
                    }
                   /* IconButton(
                        modifier = Modifier.weight(0.5f),
                        onClick = {
                            navController.navigate(Screens.Testing.route) {
                                popUpTo(0)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Add")
                    }*/
                }
            }
        }
    ) { innerPadding ->

        NavGraph(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Login.route,
        modifier = modifier
    ) {
        composable(Screens.Login.route) {
            LoginScreen(navController, viewModel)
        }
        composable(Screens.Register.route) {
            RegisterScreen(navController, viewModel)
        }
        composable(Screens.Home.route) {
            HomeScreen(viewModel)
        }
        composable(Screens.Training.route) {
            TrainingScreen(viewModel)
        }
        composable(Screens.AddTraining.route) {
            AddTrainingScreen(viewModel)
        }
        composable(Screens.Statistics.route) {
            StatisticsScreen(viewModel)
        }
        composable(Screens.Profile.route) {
            ProfileScreen(navController, viewModel)
        }
        composable(Screens.Settings.route) {
            SettingScreen(viewModel)
        }
        composable(Screens.Testing.route) {
            ScreenTest(viewModel)
        }
    }
}
