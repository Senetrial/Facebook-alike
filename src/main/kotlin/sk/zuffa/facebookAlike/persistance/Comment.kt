package sk.zuffa.facebookAlike.persistance

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "comments")
data class Comment(
    @Id
    val id: UUID = UUID.randomUUID(),
    val postId: UUID,
    val text: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
