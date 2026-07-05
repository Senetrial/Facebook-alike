package sk.zuffa.facebookAlike.service

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import sk.zuffa.facebookAlike.persistance.Post
import sk.zuffa.facebookAlike.persistance.repository.PostRepository
import kotlin.test.assertEquals

class PostServiceTest {

    private val postRepository: PostRepository = mock()
    private val postService = PostService(postRepository)

    @Test
    fun `getAllPostsSortedByRecent should return posts from repository`() {
        val posts = listOf(Post(image = byteArrayOf(), text = "Post 1"))
        val page = PageImpl(posts)
        val pageRequest = PageRequest.of(0, 3)
        whenever(postRepository.findByOrderByTimestampDesc(pageRequest)).thenReturn(page)

        val result = postService.getAllPostsSortedByRecent(0, 3)

        assertEquals(page, result)
        verify(postRepository).findByOrderByTimestampDesc(pageRequest)
    }

    @Test
    fun `createPost should save post to repository`() {
        val image = byteArrayOf(1, 2, 3)
        val text = "New Post"
        val post = Post(image = image, text = text)
        whenever(postRepository.save(any<Post>())).thenReturn(post)

        val result = postService.createPost(image, text)

        assertEquals(post, result)
        verify(postRepository).save(any<Post>())
    }

    @Test
    fun `createPost should save post without image to repository`() {
        val text = "Text Only Post"
        val post = Post(image = null, text = text)
        whenever(postRepository.save(any<Post>())).thenReturn(post)

        val result = postService.createPost(null, text)

        assertEquals(post, result)
        verify(postRepository).save(any<Post>())
    }
}
