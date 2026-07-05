package sk.zuffa.facebookAlike.service

import org.springframework.stereotype.Service
import sk.zuffa.facebookAlike.persistance.Like
import sk.zuffa.facebookAlike.persistance.repository.LikeRepository
import java.util.UUID

@Service
class LikeService(private val likeRepository: LikeRepository) {

    fun getLikesForPost(postId: UUID): Like {
        return likeRepository.findByPostId(postId) ?: likeRepository.save(Like(postId = postId))
    }

    fun likePost(postId: UUID): Like {
        val like = getLikesForPost(postId)
        val updatedLike = like.copy(likes = like.likes + 1)
        return likeRepository.save(updatedLike)
    }

    fun dislikePost(postId: UUID): Like {
        val like = getLikesForPost(postId)
        val updatedLike = like.copy(dislikes = like.dislikes + 1)
        return likeRepository.save(updatedLike)
    }
}
