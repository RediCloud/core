package net.dustrean.api.redis.codec

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import org.redisson.codec.JsonJacksonCodec

class JsonJacksonKotlinCodec(
    objectMapper: ObjectMapper
) : JsonJacksonCodec(objectMapper) {
    override fun initTypeInclusion(objectMapper: ObjectMapper) {
        objectMapper.activateDefaultTyping(
            BasicPolymorphicTypeValidator.builder().allowIfBaseType(Any::class.java).build(),
            ObjectMapper.DefaultTyping.EVERYTHING
        )
    }
}