package net.dustrean.api.redis.codec

import com.google.gson.Gson
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import org.redisson.client.codec.BaseCodec
import org.redisson.client.handler.State
import org.redisson.client.protocol.Decoder
import org.redisson.client.protocol.Encoder
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap


class GsonCodec : BaseCodec() {
    private val gson: Gson = Gson()
    private val classMap: MutableMap<String, Class<*>?> = ConcurrentHashMap()

    fun mapClass(clazz: Class<*>) {
        classMap[clazz.name] = clazz
    }

    private val encoder = Encoder { `in`: Any ->
        val out = ByteBufAllocator.DEFAULT.buffer()
        try {
            val os = ByteBufOutputStream(out)
            os.writeUTF(gson.toJson(`in`))
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

    private val decoder =
        Decoder { buf: ByteBuf?, state: State? ->
            ByteBufInputStream(buf).use { stream ->
                val string = stream.readUTF()
                val type = stream.readUTF()
                return@Decoder gson.fromJson(string, getClassFromType(type))
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
        return if (gson.javaClass.classLoader != null) {
            gson.javaClass.classLoader
        } else super.getClassLoader()
    }
}