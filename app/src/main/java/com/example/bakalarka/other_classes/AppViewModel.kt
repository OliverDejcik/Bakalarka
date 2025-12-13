package com.example.bakalarka.other_classes

import android.content.res.Resources
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakalarka.data.User
import com.example.bakalarka.supabase.CurrentUserHolder
import com.example.bakalarka.supabase.addUser
import com.example.bakalarka.supabase.verifyUser
import kotlinx.coroutines.launch

// Data class to represent an exercise with a name and details.
data class Exercise(val name: String, val sets: String, val training: String)
data class Training(val name: String, val exerciseNum: Int)


class AppViewModel : ViewModel() {

    // --- REGISTRÁCIA ---
    // Stav, ktorý bude pozorovať Composable funkcia (RegisterScreen)
    private val _registrationSuccess = mutableStateOf<Boolean?>(null)
    val registrationSuccess: State<Boolean?> = _registrationSuccess

    fun registerUser(name: String, pass: String, email: String) {
        viewModelScope.launch {
            try {
                // Volanie suspend funkcie z databázovej vrstvy
                addUser(name, pass, email)
                _registrationSuccess.value = true // Registrácia úspešná
            } catch (e: Exception) {
                // Ak Supabase alebo sieť vráti chybu, zachytíme ju tu
                println("Registration failed: ${e.message}")
                _registrationSuccess.value = false // Registrácia neúspešná
            }
        }
    }

    // Resetuje stav po tom, čo naň UI zareaguje
    fun resetRegistrationState() {
        _registrationSuccess.value = null
    }


    // --- PRIHLÁSENIE ---
    private val _loginSuccess = mutableStateOf<Boolean?>(null)
    val loginSuccess: State<Boolean?> = _loginSuccess

    fun loginUser(email: String, pass: String) {
        viewModelScope.launch {
            try {
                val success = verifyUser(email, pass)
                _loginSuccess.value = success
                if (success) {
                    // Po úspešnom prihlásení môžeme získať dáta z CurrentUserHolder
                    println("User logged in: ${CurrentUserHolder.currentUser?.username}")
                }
            } catch (e: Exception) {
                println("Login failed: ${e.message}")
                _loginSuccess.value = false
            }
        }
    }

    fun resetLoginState() {
        _loginSuccess.value = null
    }

    // --- Add training screen ---
    var exercises = mutableListOf<Exercise>()
        private set

    var trainings = mutableListOf<Training>()
        private set

    fun addExercise(name: String, sets: String, training: String) {
        exercises.add(Exercise(name, sets, training))
    }

    fun addTraining(name: String, exerciseNum: Int) {
        trainings.add(Training(name, exerciseNum))
    }


    // --- VEĽKOSTI OBRAZOVKY ---
    var screenWidth = 0
        private set

    var screenHeight = 0
        private set

    var density = 1f
        private set

    fun setScreenInfo(width: Int, height: Int, density: Float) {
        screenWidth = width
        screenHeight = height
        this.density = density
    }

    fun getScreenHeightPX(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun pxToDp(px: Int): Float = px / density


    // --- TRAINING SCREEN VECI ---
    fun getTrainingsj(): List<Training> {
        return trainings
    }

    fun getTrainingExerciseNum(name: String): Int? {
        return trainings.find { it.name == name }?.exerciseNum
    }
}
