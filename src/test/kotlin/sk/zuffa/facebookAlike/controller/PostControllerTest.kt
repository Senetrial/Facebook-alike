package sk.zuffa.facebookAlike.controller

import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sk.zuffa.facebookAlike.persistance.Post
import sk.zuffa.facebookAlike.service.PostService

@WebMvcTest(PostController::class)
class PostControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var postService: PostService

    @Test
    fun `GET api-posts should return list of posts`() {
        val post = Post(image = byteArrayOf(), text = "Hello World")
        val page = PageImpl(listOf(post))
        whenever(postService.getAllPostsSortedByRecent(0, 3)).thenReturn(page)

        mockMvc.perform(get("/api/posts"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].text").value("Hello World"))
    }

    @Test
    fun `POST api-posts should create and return post`() {
        val post = Post(image = byteArrayOf(1, 2, 3), text = "New Post")
        whenever(postService.createPost(anyOrNull(), any())).thenReturn(post)

        val image = MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, byteArrayOf(1, 2, 3))

        mockMvc.perform(multipart("/api/posts")
            .file(image)
            .param("text", "New Post"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.text").value("New Post"))
    }

    @Test
    fun `POST api-posts should create and return post without image`() {
        val post = Post(image = null, text = "Text Only Post")
        whenever(postService.createPost(anyOrNull(), any())).thenReturn(post)

        mockMvc.perform(multipart("/api/posts")
            .param("text", "Text Only Post"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.text").value("Text Only Post"))
            .andExpect(jsonPath("$.image").value(nullValue()))
    }
}
