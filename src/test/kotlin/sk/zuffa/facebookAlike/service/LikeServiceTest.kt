package sk.zuffa.facebookAlike.service

import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import sk.zuffa.facebookAlike.persistance.Like
import sk.zuffa.facebookAlike.persistance.repository.LikeRepository
import java.util.UUID
import kotlin.test.assertEquals

class LikeServiceTest {

    private val likeRepository: LikeRepository = mock()
    private val likeService = LikeService(likeRepository)

    @Test
    fun `getLikesForPost should return existing like`() {
        val postId = UUID.randomUUID()
        val existingLike = Like(postId = postId, likes = 10)
        whenever(likeRepository.findByPostId(postId)).thenReturn(existingLike)

        val result = likeService.getLikesForPost(postId)

        assertEquals(existingLike, result)
        verify(likeRepository, never()).save(any())
    }

    @Test
    fun `getLikesForPost should create and return new like if not exists`() {
        val postId = UUID.randomUUID()
        whenever(likeRepository.findByPostId(postId)).thenReturn(null)
        whenever(likeRepository.save(any())).thenAnswer { it.getArgument(0) }

        val result = likeService.getLikesForPost(postId)

        assertEquals(postId, result.postId)
        assertEquals(0, result.likes)
        verify(likeRepository).save(any())
    }

    @Test
    fun `likePost should increment likes`() {
        val postId = UUID.randomUUID()
        val existingLike = Like(postId = postId, likes = 5)
        whenever(likeRepository.findByPostId(postId)).thenReturn(existingLike)
        whenever(likeRepository.save(any())).thenAnswer { it.getArgument(0) }

        val result = likeService.likePost(postId)

        assertEquals(6, result.likes)
        verify(likeRepository).save(argThat { likes == 6 })
    }

    @Test
    fun `dislikePost should increment dislikes`() {
        val postId = UUID.randomUUID()
        val existingLike = Like(postId = postId, dislikes = 3)
        whenever(likeRepository.findByPostId(postId)).thenReturn(existingLike)
        whenever(likeRepository.save(any())).thenAnswer { it.getArgument(0) }

        val result = likeService.dislikePost(postId)

        assertEquals(4, result.dislikes)
        verify(likeRepository).save(argThat { dislikes == 4 })
    }
}
