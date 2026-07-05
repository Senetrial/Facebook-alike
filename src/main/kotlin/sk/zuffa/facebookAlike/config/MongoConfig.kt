package sk.zuffa.facebookAlike.config

import com.mongodb.MongoClientSettings
import org.bson.UuidRepresentation
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

@Configuration
class MongoConfig : AbstractMongoClientConfiguration() {

    override fun getDatabaseName(): String = "facebookAlike"

    override fun autoIndexCreation(): Boolean = true

    override fun mongoClientSettings(): MongoClientSettings {
        return MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build()
    }
}
