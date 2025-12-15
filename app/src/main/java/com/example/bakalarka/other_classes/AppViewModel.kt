package com.example.bakalarka.other_classes

import android.content.res.Resources
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakalarka.supabase.CurrentUserHolder
import com.example.bakalarka.supabase.addExerciseDB
import com.example.bakalarka.supabase.addTrainingDB
import com.example.bakalarka.supabase.addUser
import com.example.bakalarka.supabase.verifyUser
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// Data class to represent an exercise with a name and details.


class AppViewModel : ViewModel() {


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


    //Pridanie treningu do databazy


    // V súbore AppViewModel.kt

// ... (všetky ostatné importy a kód)



    // --- Ostatné funkcie (registrácia, login, atď.) ...

    // --- Logika pre pridávanie tréningov a cvikov ---

    private val _addTrainingSuccess = mutableStateOf<Boolean?>(null)
    val addTrainingSuccess: State<Boolean?> = _addTrainingSuccess

    /**
     * Asynchrónne pridá tréning do databázy a vráti jeho ID.
     * Táto funkcia je 'suspend', takže musí byť volaná z korutiny.
     * @return [Int?] ID novovytvoreného tréningu, alebo null v prípade chyby.
     */
    suspend fun addTraining(name: String, exerciseNum: Int, userId: Int): Int? {
        val deferredTrainingId = viewModelScope.async {
            try {
                addTrainingDB(name, exerciseNum, userId)
            } catch (e: Exception) {
                println("Adding training failed: ${e.message}")
                null
            }
        }
        return deferredTrainingId.await()
    }

    /**
     * Pridá cvik do databázy. Táto funkcia je tiež suspend.
     */
    suspend fun addExercise(trainingId: Int?, userId: Int?, exerciseName: String, sets: String, order: Int) {
        // Kontrola, či máme všetky potrebné údaje
        if (trainingId == null || userId == null) {
            println("Error: Cannot add exercise without trainingId or userId.")
            return // Ukončíme funkciu, ak chýbajú kľúčové ID
        }
        try {
            // Predpokladáme, že máte funkciu addExerciseDB v SupabaseApi.kt
            // Ak nie, musíte ju vytvoriť!
            addExerciseDB(trainingId, userId, exerciseName, sets.toInt(), order)
            println("Exercise '$exerciseName' added to training $trainingId")
        } catch (e: Exception) {
            println("Adding exercise failed: ${e.message}")
            // Tu môžete prípadne signalizovať chybu do UI
        }
    }

    fun resetAddTrainingState() {
        _addTrainingSuccess.value = null
    }

    // ... (zvyšok kódu vo ViewModeli)


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
}
