package sk.zuffa.facebookAlike.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import sk.zuffa.facebookAlike.persistance.Post
import sk.zuffa.facebookAlike.persistance.repository.PostRepository

@Service
class PostService(private val postRepository: PostRepository) {
    fun getAllPostsSortedByRecent(page: Int, size: Int): Page<Post> {
        return postRepository.findByOrderByTimestampDesc(PageRequest.of(page, size))
    }

    fun createPost(image: ByteArray?, text: String): Post {
        val post = Post(image = image, text = text)
        return postRepository.save(post)
    }
}
