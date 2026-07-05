package sk.zuffa.facebookAlike.controller

import tools.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import sk.zuffa.facebookAlike.persistance.Comment
import sk.zuffa.facebookAlike.service.CommentService
import java.util.*

@WebMvcTest(CommentController::class)
class CommentControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var commentService: CommentService

    @Test
    fun `GET api-comments should return list of comments for post`() {
        val postId = UUID.randomUUID()
        val comment = Comment(postId = postId, text = "Nice post!")
        val page = PageImpl(listOf(comment))
        whenever(commentService.getCommentsForPost(postId, 0, 1)).thenReturn(page)

        mockMvc.perform(get("/api/comments").param("postId", postId.toString()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].text").value("Nice post!"))
    }

    @Test
    fun `POST api-comments should create and return comment`() {
        val postId = UUID.randomUUID()
        val comment = Comment(postId = postId, text = "I agree")
        whenever(commentService.createComment(any(), any())).thenReturn(comment)

        val request = CommentController.CreateCommentRequest(postId, "I agree")

        mockMvc.perform(post("/api/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.text").value("I agree"))
    }
}
