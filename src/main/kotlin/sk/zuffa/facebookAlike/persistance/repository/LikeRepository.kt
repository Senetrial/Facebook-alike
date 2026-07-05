package sk.zuffa.facebookAlike.persistance.repository

import org.springframework.data.mongodb.repository.MongoRepository
import sk.zuffa.facebookAlike.persistance.Like
import java.util.UUID

interface LikeRepository : MongoRepository<Like, UUID> {
    fun findByPostId(postId: UUID): Like?
}
