package com.example.bakalarka.supabase

import com.example.bakalarka.data.Training

/**
 * Singleton objekt na uchovávanie informácií o aktuálne vytváranom
 * alebo upravovanom tréningu.
 */
object CurrentTrainingHolder {
    var trainingId: Int? = null

    fun set(id: Int) {
        trainingId = id
    }

    fun clear() {
        trainingId = null
    }
}
