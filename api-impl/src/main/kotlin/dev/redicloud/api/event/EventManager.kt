package dev.redicloud.api.event

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import dev.redicloud.api.packet.PacketManager
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class EventManager : IEventManager {

    companion object {
        lateinit var INSTANCE: EventManager
    }

    init {
        INSTANCE = this
        PacketManager.INSTANCE.registerPacket(EventPacket())
    }

    private val byListenerAndPriority = mutableMapOf<Class<*>, MutableMap<Byte, MutableMap<Any, Array<Method>>>>()
    private val byEventBaked = mutableMapOf<Class<*>, Array<EventInvoker>>()
    private val listenerLock = Mutex()
    private val logger = LoggerFactory.getLogger(EventManager::class.java)

    override suspend fun registerListener(listener: Any) {
        listenerLock.withLock {
            val handler = findHandlers(listener)
            handler.forEach { (event, methods) ->
                val prioritiesMap = byListenerAndPriority.computeIfAbsent(event) { mutableMapOf() }
                methods.forEach { (priority, method) ->
                    val currentPriorityMap = prioritiesMap.computeIfAbsent(priority) { mutableMapOf() }
                    currentPriorityMap[listener] = method.toTypedArray()
                }
                bakeHandlers(event)
            }
        }
    }

    override suspend fun registerListenerMethode(eventClass: Class<out CoreEvent>, method: Method, prio: Byte) {
        listenerLock.withLock {
            val handlers = mutableMapOf<Class<*>, MutableMap<Byte, MutableSet<Method>>>()
            val prioritiesMap = handlers.computeIfAbsent(eventClass) { mutableMapOf() }
            val priority = prioritiesMap.computeIfAbsent(prio) { mutableSetOf() }
            priority.add(method)

            val prioritiesMap1 = byListenerAndPriority.computeIfAbsent(eventClass) { mutableMapOf() }
            val currentPriorityMap = prioritiesMap1.computeIfAbsent(prio) { mutableMapOf() }
            currentPriorityMap[method] = arrayOf(method)
            bakeHandlers(eventClass)
        }
    }

    override suspend fun unregisterListener(listener: Any) {
        listenerLock.withLock {
            val handler = findHandlers(listener)
            handler.forEach { (event, methods) ->
                val prioritiesMap = byListenerAndPriority[event]
                if (prioritiesMap != null) {
                    methods.forEach { (priority, _) ->
                        val currentPriorityMap = prioritiesMap[priority]
                        if (currentPriorityMap != null) {
                            currentPriorityMap.remove(listener)
                            if (currentPriorityMap.isEmpty()) prioritiesMap.remove(priority)
                        }
                    }
                }
                bakeHandlers(event)
            }
        }
    }

    override fun callEvent(event: CoreEvent) {
        callEvent0(event, false)
    }

    fun callEvent0(event: CoreEvent, forceLocal: Boolean) {
        if (event.type == EventType.LOCAL || forceLocal) {
            val handlers = byEventBaked[event::class.java] ?: return
            handlers.forEach { invoker ->
                val time = System.nanoTime()
                try {
                    invoker.invoke(event)
                } catch (e: IllegalAccessException) {
                    throw IllegalAccessException("Cannot access method ${invoker.method.name} in ${invoker.method.declaringClass.name}")
                } catch (e: IllegalArgumentException) {
                    throw IllegalArgumentException("Cannot invoke method ${invoker.method.name} in ${invoker.method.declaringClass.name}")
                } catch (e: InvocationTargetException) {
                    throw InvocationTargetException(
                        e,
                        "Cannot invoke method ${invoker.method.name} in ${invoker.method.declaringClass.name}"
                    )
                }

                val elapsed = System.nanoTime() - time
                if (elapsed > 50000000) {
                    logger.warn("Event ${event::class.java.name} took ${elapsed / 1000000}ms to process")
                }
            }
            return
        }
        callEvent0(event, true)

    }

    private fun findHandlers(listener: Any): MutableMap<Class<*>, MutableMap<Byte, MutableSet<Method>>> {
        val handlers = mutableMapOf<Class<*>, MutableMap<Byte, MutableSet<Method>>>()
        val methods = buildSet {
            addAll(listener::class.java.declaredMethods)
            addAll(listener::class.java.declaredMethods)
        }
        methods.forEach { method ->
            val annotation = method.getAnnotation(CoreListener::class.java) ?: return@forEach
            val parameters = method.parameterTypes
            if (parameters.size != 1) return@forEach
            val prioritiesMap = handlers.computeIfAbsent(parameters[0]) { mutableMapOf() }
            val priority = prioritiesMap.computeIfAbsent(annotation.priority.toByte()) { mutableSetOf() }
            priority.add(method)
        }

        return handlers
    }

    private fun bakeHandlers(eventClass: Class<*>) {
        val handlersByPriority: MutableMap<Byte, MutableMap<Any, Array<Method>>>? = byListenerAndPriority[eventClass]

        if (handlersByPriority == null) {
            byEventBaked.remove(eventClass)
            return
        }

        val handlerList = ArrayList<EventInvoker>(handlersByPriority.size * 2)
        var value = Byte.MIN_VALUE

        do {
            val handlersByListener: MutableMap<Any, Array<Method>> = handlersByPriority[value] ?: continue
            handlersByListener.forEach { (listener, methods) ->
                methods.forEach { method ->
                    handlerList.add(EventInvoker(listener, method))
                }
            }
        } while (value++ < Byte.MAX_VALUE)
        byEventBaked.put(eventClass, handlerList.toTypedArray())
    }
}