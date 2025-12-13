package com.example.bakalarka.supabase

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://cpefjopvvbsbtczkysyp.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNwZWZqb3B2dmJzYnRjemt5c3lwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU0Nzg3MjQsImV4cCI6MjA4MTA1NDcyNH0.UfpYiXfo752luxflOeuJXMxfFLRQvQdtnnuR7Nb0C1k"
) {
    install(Auth)
    install(Postgrest)
    //install other modules
}