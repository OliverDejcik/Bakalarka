package com.example.bakalarka.supabase // Alebo iný vhodný balíček

import com.example.bakalarka.data.User

/**
 * Singleton objekt na uchovávanie informácií o aktuálne prihlásenom používateľovi.
 * Dáta sú prístupné odkiaľkoľvek v aplikácii.
 */
object CurrentUserHolder {
    var currentUser: User? = null

    fun login(user: User) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }
}