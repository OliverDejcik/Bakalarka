package com.example.bakalarka.supabase

import ExerciseInsert
import com.example.bakalarka.data.IdResponse
import com.example.bakalarka.data.Training
import com.example.bakalarka.data.TrainingInsert
import com.example.bakalarka.data.User
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import org.mindrot.jbcrypt.BCrypt




suspend fun addUser(username: String, password: String, email: String){
    return withContext(Dispatchers.IO) {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        supabase.from("users").insert(mapOf("username" to username, "password_hash" to hashedPassword, "email" to email))
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