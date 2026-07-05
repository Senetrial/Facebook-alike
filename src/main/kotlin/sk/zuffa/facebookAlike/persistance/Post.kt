package sk.zuffa.facebookAlike.persistance


import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "posts")
data class Post(
    @Id
    val id: UUID = UUID.randomUUID(),
    val image: ByteArray?,
    val text: String,
    val timestamp: LocalDateTime = LocalDateTime.now()


) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (id != other.id) return false
        if (!image.contentEquals(other.image)) return false
        if (text != other.text) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + text.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
