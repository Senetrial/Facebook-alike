package sk.zuffa.facebookAlike.controller

import org.springframework.web.bind.annotation.*
import sk.zuffa.facebookAlike.persistance.Like
import sk.zuffa.facebookAlike.service.LikeService
import java.util.UUID

@RestController
@RequestMapping("/api/likes")
class LikeController(private val likeService: LikeService) {

    @GetMapping("/{postId}")
    fun getLikes(@PathVariable postId: UUID): Like {
        return likeService.getLikesForPost(postId)
    }

    @PostMapping("/{postId}/like")
    fun likePost(@PathVariable postId: UUID): Like {
        return likeService.likePost(postId)
    }

    @PostMapping("/{postId}/dislike")
    fun dislikePost(@PathVariable postId: UUID): Like {
        return likeService.dislikePost(postId)
    }
}
