package net.dustrean.api.redis.codec

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.redisson.client.codec.BaseCodec
import org.redisson.client.handler.State
import org.redisson.client.protocol.Decoder
import org.redisson.client.protocol.Encoder
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

class KotlinxSerializationCodec : BaseCodec() {
    private val json: Json = Json
    private val classMap: MutableMap<String, Class<*>?> = ConcurrentHashMap()

    private val encoder = Encoder { `in`: Any ->
        val out = ByteBufAllocator.DEFAULT.buffer()
        try {
            val os = ByteBufOutputStream(out)
            os.writeUTF(json.encodeToString(`in`))
            os.writeUTF(`in`.javaClass.name)
            return@Encoder os.buffer()
        } catch (e: IOException) {
            out.release()
            throw e
        } catch (e: Exception) {
            out.release()
            throw IOException(e)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val decoder =
        Decoder { buf: ByteBuf?, state: State? ->
            ByteBufInputStream(buf).use { stream ->
                val string = stream.readUTF()
                val type = stream.readUTF()
                val clazz = getClassFromType(type)
                return@Decoder json.decodeFromString(serializer(clazz), string)
            }
        }


    fun getClassFromType(name: String): Class<*> {
        var clazz = classMap[name]
        if (clazz == null) {
            try {
                clazz = Class.forName(name)
                classMap[name] = clazz
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
        if (clazz != null) return clazz
        throw RuntimeException("Could not find a class named: $name")
    }

    override fun getValueDecoder(): Decoder<Any> {
        return decoder
    }

    override fun getValueEncoder(): Encoder {
        return encoder
    }

    override fun getClassLoader(): ClassLoader {
        return if (json.javaClass.classLoader != null)
            json.javaClass.classLoader
        else super.getClassLoader()
    }
}