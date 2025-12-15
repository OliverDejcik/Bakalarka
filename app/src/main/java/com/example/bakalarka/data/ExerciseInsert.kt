import kotlinx.serialization.Serializable


@Serializable
data class ExerciseInsert(
    val training_id: Int,
    val user_id: Int,
    val name: String,
    val sets_count: Int,
    val order_index: Int
)
