package com.example.bakalarka

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.Screens
import com.example.bakalarka.screens.AddTrainingScreen
import com.example.bakalarka.screens.HomeScreen
import com.example.bakalarka.screens.LoginScreen
import com.example.bakalarka.screens.MainScreen
import com.example.bakalarka.screens.ProfileScreen
import com.example.bakalarka.screens.RegisterScreen
import com.example.bakalarka.screens.SettingScreen
import com.example.bakalarka.screens.StatisticsScreen
import com.example.bakalarka.screens.TrainingScreen
import com.example.bakalarka.ui.theme.BakalarkaTheme
import androidx.compose.material.icons.Icons




class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels
        val density = displayMetrics.density

        // This correctly creates a ViewModel instance scoped to this Activity.
        val viewModel: AppViewModel by viewModels()

        viewModel.setScreenInfo(displayWidth, displayHeight, density)

        enableEdgeToEdge()

        setContent {
            BakalarkaTheme {
                val navController = rememberNavController()

                // Pass the single viewModel instance to your main Composable.
                Bakalarka(navController = navController, viewModel = viewModel)
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bakalarka(navController: NavHostController, viewModel: AppViewModel) {

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val bottomBarScreens = listOf(
        Screens.Home.route,
        Screens.AddTraining.route,
        Screens.Training.route,
        Screens.Statistics.route,
        Screens.Profile.route,
        Screens.Settings.route
    )
    /*
     toto ifko je iba docasne nech nemusim furt pridavat treningy jak keket
     */
    if (viewModel.trainings.isEmpty()){
        viewModel.addTraining("training1",1)
        viewModel.addTraining("training2",2)

        viewModel.addExercise("exercise11","2","training1")

        viewModel.addExercise("exercise12","4","training1")
        viewModel.addExercise("exercise21","3","training2")
    }



    val showBottomBar = currentRoute in bottomBarScreens
    val showTopBar = currentRoute in bottomBarScreens

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text("Moja appka") },
                    navigationIcon = {
                        IconButton(onClick =
                            {
                                navController.navigate(Screens.Profile.route) {
                                    popUpTo(0)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                    },

                    actions = {
                        IconButton(onClick = {
                            navController.navigate(Screens.Settings.route) {
                                popUpTo(0)
                            }
                        }
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
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
                    IconButton(onClick = {
                        navController.navigate(Screens.Home.route) {
                            popUpTo(0)
                        }
                    },
                        Modifier.weight(1f)
                    )
                    {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }


                    IconButton(onClick = {
                        navController.navigate(Screens.AddTraining.route) {
                            popUpTo(0)
                        }
                    }, Modifier.weight(1f)
                    )
                    {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }

                    IconButton(onClick = {
                        navController.navigate(Screens.Training.route) {
                            popUpTo(0)
                        }
                    }, Modifier.weight(1f)
                    )
                    {
                        Icon(painterResource(id = R.drawable.dumbellicon), contentDescription = "Training")
                    }

                    IconButton(onClick = {
                        navController.navigate(Screens.Statistics.route) {
                            popUpTo(0)
                        }
                    }, Modifier.weight(1f)
                    )
                    {
                        Icon(painterResource(id = R.drawable.graphicon), contentDescription = "Stats")
                    }

                }
            }
        }
    ) { innerPadding ->
        // The viewModel instance is passed down to the NavGraph
        NavGraph(navController = navController, viewModel = viewModel)
    }
}


@Composable
fun NavGraph(navController: NavHostController, viewModel: AppViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screens.Main.route
    ) {
        // FIX: Pass the SAME viewModel instance to EVERY screen that needs it.
        composable(Screens.Main.route) { MainScreen(navController, viewModel) }
        composable(Screens.Home.route) { HomeScreen(viewModel) }
        composable(Screens.Login.route) { LoginScreen(navController, viewModel) }
        composable(Screens.Register.route) { RegisterScreen(navController, viewModel) }
        composable(Screens.Training.route) { TrainingScreen(viewModel) }
        composable(Screens.AddTraining.route) { AddTrainingScreen(viewModel) }
        composable(Screens.Statistics.route) { StatisticsScreen(viewModel) }
        composable(Screens.Profile.route) { ProfileScreen(viewModel) }
        composable(Screens.Settings.route) { SettingScreen(viewModel) }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BakalarkaPreview() {
    BakalarkaTheme {
        val navController = rememberNavController()
        // FIX for Preview: Create a temporary instance of the ViewModel for the preview to use.
        // The `viewModel()` delegate will provide a basic, un-scoped instance here.
        val previewViewModel: AppViewModel = viewModel()
        Bakalarka(navController = navController, viewModel = previewViewModel)
    }
}
