package sk.zuffa.facebookAlike.persistance

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "likes")
data class Like(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Indexed(unique = true)
    val postId: UUID,
    val likes: Int = 0,
    val dislikes: Int = 0
)
