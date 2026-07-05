package sk.zuffa.facebookAlike.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import sk.zuffa.facebookAlike.persistance.Comment
import sk.zuffa.facebookAlike.persistance.repository.CommentRepository
import java.util.UUID

@Service
class CommentService(private val commentRepository: CommentRepository) {
    fun getCommentsForPost(postId: UUID, page: Int, size: Int): Page<Comment> {
        return commentRepository.findByPostId(postId, PageRequest.of(page, size, Sort.by("timestamp").descending()))
    }

    fun createComment(postId: UUID, text: String): Comment {
        val comment = Comment(postId = postId, text = text)
        return commentRepository.save(comment)
    }
}
