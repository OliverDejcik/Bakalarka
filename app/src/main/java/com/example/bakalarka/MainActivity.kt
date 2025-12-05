package com.example.bakalarka

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
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
import com.example.bakalarka.screens.SettingsScreen
import com.example.bakalarka.screens.StatisticsScreen
import com.example.bakalarka.screens.TrainingScreen
import com.example.bakalarka.ui.theme.BakalarkaTheme


class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayMetrics = DisplayMetrics()
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels
        val density = displayMetrics.density

        val viewModel: AppViewModel by viewModels()



        viewModel.setScreenInfo(displayWidth, displayHeight, density)


        enableEdgeToEdge()

        setContent {
            BakalarkaTheme {

                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    Bakalarka(navController = navController) // verzia bez navigácie
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bakalarka(navController: NavHostController) {

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

    val showBottomBar = currentRoute in bottomBarScreens
    val showTopBar = currentRoute in bottomBarScreens

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text("Moja appka") },
                    navigationIcon = {
                        IconButton(onClick =
                            { navController.navigate(Screens.Profile.route){
                                popUpTo(0)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                    },

                    actions = {
                        IconButton(onClick = {
                            navController.navigate(Screens.Settings.route){
                                popUpTo(0)
                            }
                            }
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors( containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )

                )
            }
        },

        bottomBar = {
            if (showBottomBar){
                NavigationBar (
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ){
                    IconButton(onClick = {
                        navController.navigate(Screens.Home.route){
                            popUpTo(0)
                        }
                    },
                        Modifier.weight(1f)
                    )
                    {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }


                    IconButton(onClick = {
                        navController.navigate(Screens.AddTraining.route){
                            popUpTo(0)
                        }
                    }, Modifier.weight(1f)
                    )
                    {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }

                    IconButton(onClick = {
                        navController.navigate(Screens.Training.route){
                            popUpTo(0)
                        }
                    },Modifier.weight(1f)
                    )
                    {
                        Icon(painterResource(id = R.drawable.dumbellicon), contentDescription = "Training")
                    }

                    IconButton(onClick = {
                        navController.navigate(Screens.Statistics.route){
                            popUpTo(0)
                        }
                    },Modifier.weight(1f)
                    )
                    {
                        Icon(painterResource(id = R.drawable.graphicon), contentDescription = "Stats")
                    }

                }
            }
        }
    ) { innerPadding ->

        NavGraph(navController = navController)
    }
}




@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Main.route
    ) {
        composable(Screens.Main.route) { MainScreen(navController)}
        composable(Screens.Home.route) { HomeScreen() }
        composable(Screens.Login.route) { LoginScreen(navController) }
        composable(Screens.Register.route) { RegisterScreen(navController) }
        composable(Screens.Training.route) { TrainingScreen() }
        composable(Screens.AddTraining.route) { AddTrainingScreen() }
        composable(Screens.Statistics.route) { StatisticsScreen() }
        composable(Screens.Profile.route) { ProfileScreen() }
        composable(Screens.Settings.route) { SettingsScreen() }
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BakalarkaPreview() {
    BakalarkaTheme {
        val navController = rememberNavController()
        Bakalarka(navController = navController)
    }
}
