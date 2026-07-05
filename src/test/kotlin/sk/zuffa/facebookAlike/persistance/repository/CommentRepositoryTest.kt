package sk.zuffa.facebookAlike.persistance.repository

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import sk.zuffa.facebookAlike.persistance.Comment
import java.util.*
import kotlin.test.assertEquals

class CommentRepositoryTest {

    private val commentRepository: CommentRepository = mock()

    @Test
    fun `should find comments by postId`() {
        val storage = mutableListOf<Comment>()
        
        whenever(commentRepository.saveAll(any<Iterable<Comment>>())).thenAnswer { invocation ->
            val comments = invocation.getArgument<Iterable<Comment>>(0)
            storage.addAll(comments)
            comments
        }
        
        whenever(commentRepository.findByPostId(any(), any())).thenAnswer { invocation ->
            val postId = invocation.getArgument<UUID>(0)
            PageImpl(storage.filter { it.postId == postId })
        }

        val postId1 = UUID.randomUUID()
        val postId2 = UUID.randomUUID()
        
        val comment1 = Comment(postId = postId1, text = "Comment on post 1")
        val comment2 = Comment(postId = postId1, text = "Another comment on post 1")
        val comment3 = Comment(postId = postId2, text = "Comment on post 2")

        commentRepository.saveAll(listOf(comment1, comment2, comment3))

        val result = commentRepository.findByPostId(postId1, mock())

        assertEquals(2, result.content.size)
        result.content.forEach { assertEquals(postId1, it.postId) }
    }
}
