package sk.zuffa.facebookAlike.persistance.repository

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import sk.zuffa.facebookAlike.persistance.Post
import kotlin.test.assertEquals

class PostRepositoryTest {

    private val postRepository: PostRepository = mock()

    @Test
    fun `should save and find post by order by timestamp desc`() {
        val posts = mutableListOf<Post>()
        
        whenever(postRepository.save(any<Post>())).thenAnswer { invocation ->
            val post = invocation.getArgument<Post>(0)
            posts.add(post)
            post
        }
        
        whenever(postRepository.findByOrderByTimestampDesc(any<Pageable>())).thenAnswer {
            PageImpl(posts.sortedByDescending { it.timestamp })
        }

        val post1 = Post(image = byteArrayOf(), text = "First post")
        val post2 = Post(image = byteArrayOf(), text = "Second post")
        
        postRepository.save(post1)
        postRepository.save(post2)

        val result = postRepository.findByOrderByTimestampDesc(mock())

        assertEquals(2, result.content.size)
        // post2 should be first because it was created later
        assertEquals("Second post", result.content[0].text)
    }
}
