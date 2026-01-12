package com.example.bakalarka.other_classes

import android.content.res.Resources
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakalarka.data.Exercise
import com.example.bakalarka.data.Training
import com.example.bakalarka.data.WorkoutExercise
import com.example.bakalarka.screens.SetData
import com.example.bakalarka.supabase.addExerciseDB
import com.example.bakalarka.supabase.addExerciseToWorkoutDB
import com.example.bakalarka.supabase.addTrainingDB
import com.example.bakalarka.supabase.addUser
import com.example.bakalarka.supabase.addWorkoutToDB
import com.example.bakalarka.supabase.getExercisesByTrainingId
import com.example.bakalarka.supabase.getTrainingsByName
import com.example.bakalarka.supabase.getTrainingsByUserId
import com.example.bakalarka.supabase.getWorkoutExercisesByExerciseId
import com.example.bakalarka.supabase.removeExerciseById
import com.example.bakalarka.supabase.updateExerciseNameReps
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.OffsetDateTime


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


    fun addExercise(
        trainingId: Int?,
        userId: Int?,
        exerciseName: String,
        sets: String,
        order: Int,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        if (trainingId == null || userId == null) {
            onError?.invoke("Invalid training or user")
            return
        }

        val setsInt = sets.toIntOrNull()
        if (setsInt == null) {
            onError?.invoke("Sets must be a number")
            return
        }

        viewModelScope.launch {
            try {
                addExerciseDB(
                    trainingId,
                    userId,
                    exerciseName,
                    setsInt,
                    order
                )
                onSuccess?.invoke()
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Unknown error")
            }
        }
    }

    fun updateExercise(
        exerciseId: Int,
        name: String,
        reps: Int,
        trainingId: Int
    ) {
        viewModelScope.launch {
            updateExerciseNameReps(exerciseId, name, reps)
            loadExercisesByTrainingId(trainingId)
        }
    }

    fun deleteExercise(
        exerciseId: Int,
        trainingId: Int
    ) {
        viewModelScope.launch {
            removeExerciseById(exerciseId, trainingId)
            loadExercisesByTrainingId(trainingId)
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

    private val _workouEexercises = mutableStateOf<List<WorkoutExercise>>(emptyList())
    val workoutExercises: State<List<WorkoutExercise>> = _workouEexercises

    fun loadWorkoutExercisesByExerciseId(exerciseId: Int) {
        viewModelScope.launch {
            try {
                _workouEexercises.value = getWorkoutExercisesByExerciseId(exerciseId)
            } catch (e: Exception) {
                _workouEexercises.value = emptyList()
            }
        }
    }

    private val _chartData = mutableStateOf<List<Pair<Float, Float>>>(emptyList())
    val chartData: State<List<Pair<Float, Float>>> = _chartData

    data class SimpleSet(
        val workoutId: Int,
        val reps: Int,
        val weight: Float,
        val createdAt: String
    )

    data class Workout1RM(
        val workoutId: Int,
        val oneRM: Double,
        val createdAt: String
    )

    data class ChartPoint(
        val date: LocalDate,
        val value: Double
    )

    fun calculate1RM(weight: Float, reps: Int): Double {
        return weight * (1 + reps / 30.0)
    }



    fun build30Day1RMChart(sets: List<WorkoutExercise>,daysString: String = "") {

        val today = LocalDate.now()
        val fromDate = when (daysString) {
            "Last 14 days" -> today.minusDays(14)
            "Last 30 days" -> today.minusDays(30)
            "Last 180 days" -> today.minusDays(180)
            "Last 360 days" -> today.minusDays(360)
            else -> today.minusDays(30) // default
        }




        val chartPoints = sets
            // 1. zjednodušenie
            .map {
                SimpleSet(
                    workoutId = it.workout_id,
                    reps = it.reps,
                    weight = it.weight,
                    createdAt = it.created_at
                )
            }
            // 2. group by workout
            .groupBy { it.workoutId }
            // 3. max 1RM z workoutu
            .mapNotNull { (_, workoutSets) ->
                val bestSet = workoutSets.maxByOrNull {
                    calculate1RM(it.weight, it.reps)
                } ?: return@mapNotNull null

                val date = OffsetDateTime
                    .parse(bestSet.createdAt)
                    .toLocalDate()

                if (date < fromDate) return@mapNotNull null

                val oneRM = calculate1RM(bestSet.weight, bestSet.reps)

                // X = dni od dnes (pre graf)
                val x = date.toEpochDay().toFloat()
                val y = oneRM.toFloat()

                x to y
            }
            // 4. zoradenie
            .sortedBy { it.first }

        _chartData.value = chartPoints
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