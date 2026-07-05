package sk.zuffa.facebookAlike.controller

import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sk.zuffa.facebookAlike.persistance.Like
import sk.zuffa.facebookAlike.service.LikeService
import java.util.*

@WebMvcTest(LikeController::class)
class LikeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var likeService: LikeService

    @Test
    fun `should get likes for post`() {
        val postId = UUID.randomUUID()
        val like = Like(postId = postId, likes = 5, dislikes = 1)
        whenever(likeService.getLikesForPost(postId)).thenReturn(like)

        mockMvc.perform(get("/api/likes/$postId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.likes").value(5))
            .andExpect(jsonPath("$.dislikes").value(1))
    }

    @Test
    fun `should like post`() {
        val postId = UUID.randomUUID()
        val like = Like(postId = postId, likes = 1, dislikes = 0)
        whenever(likeService.likePost(postId)).thenReturn(like)

        mockMvc.perform(post("/api/likes/$postId/like"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.likes").value(1))
    }

    @Test
    fun `should dislike post`() {
        val postId = UUID.randomUUID()
        val like = Like(postId = postId, likes = 0, dislikes = 1)
        whenever(likeService.dislikePost(postId)).thenReturn(like)

        mockMvc.perform(post("/api/likes/$postId/dislike"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.dislikes").value(1))
    }
}
