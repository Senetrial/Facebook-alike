package sk.zuffa.facebookAlike.persistance.repository

import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import sk.zuffa.facebookAlike.persistance.Like
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LikeRepositoryTest {

    private val likeRepository: LikeRepository = mock()

    @Test
    fun `should find like by postId`() {
        val postId = UUID.randomUUID()
        val mockLike = Like(postId = postId, likes = 5, dislikes = 2)

        whenever(likeRepository.findByPostId(postId)).thenReturn(mockLike)

        val result = likeRepository.findByPostId(postId)

        assertNotNull(result)
        assertEquals(postId, result.postId)
        assertEquals(5, result.likes)
        assertEquals(2, result.dislikes)
    }
}
