package com.example.bakalarka.other_classes

import android.content.res.Resources
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakalarka.data.Exercise
import com.example.bakalarka.data.Training
import com.example.bakalarka.screens.SetData
import com.example.bakalarka.supabase.addExerciseDB
import com.example.bakalarka.supabase.addExerciseToWorkoutDB
import com.example.bakalarka.supabase.addTrainingDB
import com.example.bakalarka.supabase.addUser
import com.example.bakalarka.supabase.addWorkoutToDB
import com.example.bakalarka.supabase.getExercisesByTrainingId
import com.example.bakalarka.supabase.getTrainingsByName
import com.example.bakalarka.supabase.getTrainingsByUserId
import kotlinx.coroutines.async
import kotlinx.coroutines.launch



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




    private val _addTrainingSuccess = mutableStateOf<Boolean?>(null)
    val addTrainingSuccess: State<Boolean?> = _addTrainingSuccess


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


    suspend fun addExercise(trainingId: Int?, userId: Int?, exerciseName: String, sets: String, order: Int) {

        if (trainingId == null || userId == null) {
            println("Error: Cannot add exercise without trainingId or userId.")
            return
        }
        try {

            addExerciseDB(trainingId, userId, exerciseName, sets.toInt(), order)
            println("Exercise '$exerciseName' added to training $trainingId")
        } catch (e: Exception) {
            println("Adding exercise failed: ${e.message}")

        }
    }

    fun resetAddTrainingState() {
        _addTrainingSuccess.value = null
    }

    private val _addWorkoutSuccess = mutableStateOf<Boolean?>(null)
    val addWorkoutSuccess: State<Boolean?> = _addWorkoutSuccess


    suspend fun addExerciseToWorkout(
        userId: Int,
        workoutId: Int,
        exerciseId: Int,
        setDataSet: List<SetData>,
        sets_count: Int
    ) {
        try {
            for (i in 0 until sets_count) {

                val reps = setDataSet[i].reps.toIntOrNull()
                val weight = setDataSet[i].weight.toIntOrNull()

                if (reps == null || weight == null) {
                    println("Invalid input in set ${i + 1}")
                    return
                }

                addExerciseToWorkoutDB(
                    userId,
                    workoutId,
                    exerciseId,
                    weight,
                    reps,
                    i + 1
                )
            }

            println("Workout exercises inserted successfully")

        } catch (e: Exception) {
            println("addExerciseToWorkout failed: ${e.message}")
        }
    }



    suspend fun addWorkout(userID:Int,trainingID:Int): Int? {
        val deferredWorkoutId = viewModelScope.async {
            try {
                addWorkoutToDB(userID,trainingID)
            } catch (e: Exception) {
                println("Adding workout failed: ${e.message}")
                null
            }
        }
        return deferredWorkoutId.await()
    }

    private val _trainings = mutableStateOf<List<Training>>(emptyList())
    val trainings: State<List<Training>> = _trainings

    fun loadTrainingsByName(name: String, userId: Int) {
        _trainings.value = emptyList()

        viewModelScope.launch {
            try {
                _trainings.value = getTrainingsByName(name, userId)
            } catch (e: Exception) {
                _trainings.value = emptyList()
            }
        }
    }

    fun loadTrainingsByUserId(userId: Int) {
        _trainings.value = emptyList()
        viewModelScope.launch {
            try {
                _trainings.value = getTrainingsByUserId(userId)
            } catch (e: Exception) {
                _trainings.value = emptyList()
            }
        }
    }


    private val _exercises = mutableStateOf<List<Exercise>>(emptyList())
    val exercises: State<List<Exercise>> = _exercises

    fun loadExercisesByTrainingId(trainingId: Int) {
        viewModelScope.launch {
            try {
                _exercises.value = getExercisesByTrainingId(trainingId)
            } catch (e: Exception) {
                _exercises.value = emptyList()
            }
        }
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
}
