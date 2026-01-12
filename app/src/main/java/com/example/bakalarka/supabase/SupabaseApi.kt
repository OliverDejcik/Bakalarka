package com.example.bakalarka.supabase

import ExerciseInsert
import androidx.compose.ui.text.font.FontWeight
import com.example.bakalarka.data.Exercise
import com.example.bakalarka.data.Training
import com.example.bakalarka.data.TrainingInsert
import com.example.bakalarka.data.User
import com.example.bakalarka.data.Workout
import com.example.bakalarka.data.WorkoutExercise
import com.example.bakalarka.data.WorkoutInsert
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put





suspend fun addUser(username: String, password: String, email: String){
    return withContext(Dispatchers.IO) {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        supabase.from("users").insert(mapOf("username" to username, "password_hash" to hashedPassword, "email" to email))
    }
}
suspend fun addExerciseToWorkoutDB(
    userId: Int,
    workoutId: Int,
    exerciseId: Int,
    weight: Int,
    reps: Int,
    setNumber: Int
) {
    return withContext(Dispatchers.IO) {
        try {
            val result = supabase
                .from("workout_exercises")
                .insert(
                    mapOf(
                        "user_id" to userId,
                        "workout_id" to workoutId,
                        "exercise_id" to exerciseId,
                        "weight" to weight,
                        "reps" to reps,
                        "set_number" to setNumber
                    )
                ) {
                    select() // ⬅️ DONÚTI SUPABASE VRÁTIŤ RESPONSE
                }

            println("Workout exercise inserted: $result")

        } catch (e: Exception) {
            println("INSERT FAILED: ${e.message}")
            throw e
        }
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

suspend fun verifyUserByIdAndPass(userId: Int, password: String): Boolean {
    return withContext(Dispatchers.IO) {
        val userResponse = supabase.from("users")
            .select()
            {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingleOrNull<User>()

        if (userResponse != null) {
            val storedHash = userResponse.password_hash
            val isPasswordCorrect = BCrypt.checkpw(password, storedHash)

            if (isPasswordCorrect) {
                return@withContext true
            }
        }
        return@withContext false
    }
}

suspend fun changeUserPassword(userId: Int, newPassword: String) {
    return withContext(Dispatchers.IO) {// 1. Zahashujeme nové heslo pomocou BCrypt
        val newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt())

        // 2. Použijeme operáciu `update` na zmenu záznamu v tabuľke "users"
        supabase.from("users")
            .update(
                mapOf("password_hash" to newHashedPassword) // Hodnota, ktorú meníme
            ) {
                // 3. Pomocou filtra špecifikujeme, KTORÝ záznam sa má zmeniť
                filter {
                    eq("id", userId)
                }
            }
    }
}

suspend fun getTrainingsByName(name: String,userId: Int): List<Training> {
    return supabase
        .from("trainings")
        .select {
            filter {
                eq("user_id", userId)
                ilike("name", "%$name%")
            }
        }
        .decodeList<Training>()
}

suspend fun getWorkoutExercisesByExerciseId(exerciseId: Int): List<WorkoutExercise> {
    return supabase
        .from("workout_exercises")
        .select {
            filter {
                eq("exercise_id", exerciseId)
            }
        }
        .decodeList<WorkoutExercise>()
}

suspend fun getTrainingsByUserId(userId: Int): List<Training> {
    return supabase
        .from("trainings")
        .select {
            filter {
                eq("user_id", userId)
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

suspend fun updateExerciseNameReps(
    exerciseId: Int,
    newName: String,
    newReps: Int
) {
    supabase
        .from("exercises")
        .update(
            buildJsonObject {
                put("name", newName)
                put("sets_count", newReps)
            }
        ) {
            filter {
                eq("id", exerciseId)
            }
        }
}

suspend fun updateTrainingName(
    trainingId: Int,
    newName: String
){
    supabase
        .from("trainings")
        .update(
            buildJsonObject {
                put("name", newName)
            }
        ) {
            filter {
                eq("id", trainingId)
            }
        }
}

suspend fun removeExerciseById(exerciseId: Int, trainingId: Int){
    supabase
        .from("exercises")
        .delete {
            filter {
                eq("id", exerciseId)
            }
        }

    supabase
        .from("trainings")
        .update(
            buildJsonObject {
                put("number_of_exercises", -1)
            }
        ){
            filter {
                eq("id", trainingId)
            }
        }

}
