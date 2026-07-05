package sk.zuffa.facebookAlike.persistance.repository

import sk.zuffa.facebookAlike.persistance.Comment
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface CommentRepository : MongoRepository<Comment, UUID> {
    fun findByPostId(postId: UUID, pageable: Pageable): Page<Comment>
}
