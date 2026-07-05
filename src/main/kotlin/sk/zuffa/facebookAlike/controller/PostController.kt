package sk.zuffa.facebookAlike.controller

import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import sk.zuffa.facebookAlike.persistance.Post
import sk.zuffa.facebookAlike.service.PostService

@RestController
@RequestMapping("/api/posts")
class PostController(private val postService: PostService) {

    @GetMapping
    fun getAllPosts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "3") size: Int
    ): Page<Post> {
        return postService.getAllPostsSortedByRecent(page, size)
    }

    @PostMapping
    fun createPost(
        @RequestParam(value = "image", required = false) image: MultipartFile?,
        @RequestParam("text") text: String
    ): Post {
        return postService.createPost(image?.bytes, text)
    }
}
