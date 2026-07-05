package sk.zuffa.facebookAlike.persistance.repository

import sk.zuffa.facebookAlike.persistance.Post
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface PostRepository : MongoRepository<Post, UUID> {
    fun findByOrderByTimestampDesc(pageable: Pageable): Page<Post>
}