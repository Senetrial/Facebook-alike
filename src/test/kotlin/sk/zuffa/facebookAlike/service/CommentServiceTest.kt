package sk.zuffa.facebookAlike.service

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import sk.zuffa.facebookAlike.persistance.Comment
import sk.zuffa.facebookAlike.persistance.repository.CommentRepository
import java.util.UUID
import kotlin.test.assertEquals

class CommentServiceTest {

    private val commentRepository: CommentRepository = mock()
    private val commentService = CommentService(commentRepository)

    @Test
    fun `getCommentsForPost should return comments from repository`() {
        val postId = UUID.randomUUID()
        val comments = listOf(Comment(postId = postId, text = "Comment 1"))
        val page = PageImpl(comments)
        val pageRequest = PageRequest.of(0, 1, Sort.by("timestamp").descending())
        whenever(commentRepository.findByPostId(postId, pageRequest)).thenReturn(page)

        val result = commentService.getCommentsForPost(postId, 0, 1)

        assertEquals(page, result)
        verify(commentRepository).findByPostId(postId, pageRequest)
    }

    @Test
    fun `createComment should save comment to repository`() {
        val postId = UUID.randomUUID()
        val text = "New Comment"
        val comment = Comment(postId = postId, text = text)
        whenever(commentRepository.save(any<Comment>())).thenReturn(comment)

        val result = commentService.createComment(postId, text)

        assertEquals(comment, result)
        verify(commentRepository).save(any<Comment>())
    }
}
