package sk.zuffa.facebookAlike

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import sk.zuffa.facebookAlike.persistance.repository.PostRepository
import sk.zuffa.facebookAlike.persistance.repository.CommentRepository
import sk.zuffa.facebookAlike.persistance.repository.LikeRepository

@SpringBootTest(properties = [
    "spring.autoconfigure.exclude=org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration,org.springframework.boot.data.mongodb.autoconfigure.DataMongoAutoConfiguration,de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration"
])
class FacebookAlikeApplicationTests {

    @MockitoBean
    private lateinit var postRepository: PostRepository

    @MockitoBean
    private lateinit var commentRepository: CommentRepository

    @MockitoBean
    private lateinit var likeRepository: LikeRepository

	@Test
	fun contextLoads() {
	}

}
