package sk.zuffa.facebookAlike.controller

import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import sk.zuffa.facebookAlike.persistance.Comment
import sk.zuffa.facebookAlike.service.CommentService
import java.util.UUID

@RestController
@RequestMapping("/api/comments")
class CommentController(private val commentService: CommentService) {

    @GetMapping
    fun getCommentsForPost(
        @RequestParam postId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "1") size: Int
    ): Page<Comment> {
        return commentService.getCommentsForPost(postId, page, size)
    }

    @PostMapping
    fun createComment(@RequestBody request: CreateCommentRequest): Comment {
        return commentService.createComment(request.postId, request.text)
    }

    data class CreateCommentRequest(
        val postId: UUID,
        val text: String
    )
}
