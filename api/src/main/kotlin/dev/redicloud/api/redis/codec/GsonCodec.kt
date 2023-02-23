package dev.redicloud.api.redis.codec

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import dev.redicloud.api.ICoreAPI
import org.redisson.client.codec.BaseCodec
import org.redisson.client.handler.State
import org.redisson.client.protocol.Decoder
import org.redisson.client.protocol.Encoder
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap


class GsonCodec(val classLoaders: MutableList<ClassLoader>) : BaseCodec() {
    private val gson: Gson = GsonBuilder().addSerializationExclusionStrategy(object : ExclusionStrategy {
        override fun shouldSkipField(f: FieldAttributes?): Boolean {
            return f?.getAnnotation(Expose::class.java)?.serialize == false || f?.getAnnotation(GsonIgnore::class.java) != null
        }

        override fun shouldSkipClass(p0: Class<*>?): Boolean = false
    }).addDeserializationExclusionStrategy(object: ExclusionStrategy {
        override fun shouldSkipField(f: FieldAttributes?): Boolean {
            return f?.getAnnotation(Expose::class.java)?.deserialize == false || f?.getAnnotation(GsonIgnore::class.java) != null
        }

        override fun shouldSkipClass(clazz: Class<*>?): Boolean = false
    }).create()
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

    private val decoder = Decoder { buf: ByteBuf?, state: State? ->
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
                clazz = try {
                    Class.forName(name)
                } catch (e: ClassNotFoundException) {
                    let {
                        val loaders = mutableListOf<ClassLoader>()
                        loaders.addAll(classLoaders)
                        loaders.addAll(ICoreAPI.INSTANCE.moduleManager.getModuleLoaders())
                        for (classLoader in loaders) {
                            try {
                                return@let classLoader.loadClass(name)
                            } catch (ignored: ClassNotFoundException) {
                            }
                        }
                        throw e
                    }
                }
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

annotation class GsonIgnore