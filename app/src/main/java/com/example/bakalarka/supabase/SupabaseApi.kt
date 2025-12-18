package com.example.bakalarka.supabase

import ExerciseInsert
import androidx.compose.ui.text.font.FontWeight
import com.example.bakalarka.data.Exercise
import com.example.bakalarka.data.Training
import com.example.bakalarka.data.TrainingInsert
import com.example.bakalarka.data.User
import com.example.bakalarka.data.Workout
import com.example.bakalarka.data.WorkoutInsert
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt




suspend fun addUser(username: String, password: String, email: String){
    return withContext(Dispatchers.IO) {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        supabase.from("users").insert(mapOf("username" to username, "password_hash" to hashedPassword, "email" to email))
    }
}
suspend fun addExerciseToWorkoutDB(userId: Int, workoutId: Int, exerciseId: Int, weight: Int,reps: Int,set_number:Int){
    return withContext(Dispatchers.IO) {
        supabase.from("workout_exercises").insert(mapOf("user_id" to userId, "workout_id" to workoutId, "exercise_id" to exerciseId, "weight" to weight, "reps" to reps,"set_number" to set_number))
    }
}


suspend fun addWorkoutToDB(userId: Int,trainingId: Int): Int? {
    return withContext(Dispatchers.IO) {
        val insertData = WorkoutInsert(
            user_id = userId,
            training_id = trainingId
        )
        val createdWorkout = supabase
            .from("workouts")
            .insert(insertData) {
                select()
            }
            .decodeSingleOrNull<Workout>()
        if (createdWorkout != null) {
            println("Workout created with ID: ${createdWorkout.id}")
        } else {
            CurrentTrainingHolder.clear()
        }

        createdWorkout?.id

    }
}

suspend fun addTrainingDB(
    name: String,
    number_of_exercises: Int,
    userId: Int
): Int? {
    return withContext(Dispatchers.IO) {

        val insertData = TrainingInsert(
            user_id = userId,
            name = name,
            number_of_exercises = number_of_exercises
        )

        val createdTraining = supabase
            .from("trainings")
            .insert(insertData) {
                select()
            }
            .decodeSingleOrNull<Training>()

        if (createdTraining != null) {

            println("Training created with ID: ${createdTraining.id}")
        } else {
            CurrentTrainingHolder.clear()
        }

        createdTraining?.id
    }
}





suspend fun addExerciseDB(
    trainingId: Int,
    userId: Int,
    name: String,
    sets: Int,
    orderIndex: Int
) {
    return withContext(Dispatchers.IO) {

        val insertData = ExerciseInsert(
            training_id = trainingId,
            user_id = userId,
            name = name,
            sets_count = sets,
            order_index = orderIndex
        )

        supabase
            .from("exercises")
            .insert(insertData)
    }
}

suspend fun verifyUser(username: String, password: String): Boolean {
    return withContext(Dispatchers.IO) {
        // 1. Získame všetky dáta používateľa podľa e-mailu.
        val userResponse = supabase.from("users")
            .select() // select() bez parametrov znamená "vyber všetky stĺpce"
            {
                filter {
                    eq("username", username)
                }
            }
            .decodeSingleOrNull<User>() // Pokúsime sa dekódovať priamo do dátovej triedy User

        // Ak používateľ existuje, overíme heslo.
        if (userResponse != null) {
            val storedHash = userResponse.password_hash
            // 2. Skontrolujeme, či sa zadané heslo zhoduje s uloženým hashom.
            val isPasswordCorrect = BCrypt.checkpw(password, storedHash)

            if (isPasswordCorrect) {
                // Heslo je správne. Uložíme údaje o používateľovi do CurrentUserHolder.
                CurrentUserHolder.login(userResponse)
                // A vrátime `true` ako signál úspešného prihlásenia.
                return@withContext true
            }
        }

        // Ak používateľ neexistuje alebo je heslo nesprávne, vrátime `false`.
        CurrentUserHolder.logout() // Pre istotu vyčistíme staré dáta
        return@withContext false
    }
}

suspend fun getTrainingsByName(name: String,userId: Int): List<Training> {
    return supabase
        .from("trainings")
        .select {
            filter {
                eq("id", userId)
                ilike("name", "%$name%")
            }
        }
        .decodeList<Training>()
}

suspend fun getExercisesByTrainingId(trainingId: Int): List<Exercise> {
    return supabase
        .from("exercises")
        .select {
            filter {
                eq("training_id", trainingId)
            }
        }
        .decodeList<Exercise>()
}