package dev.redicloud.api.utils

import com.google.gson.Gson
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object ExceptionHandler {

    private val client = HttpClient.newHttpClient()
    private val gson = Gson()

    var service = "undefined"
    private val hasteURL: String = System.getenv("EXCEPTION_HASTE_URL") ?: "https://hastebin.de"
    private val webhookURL: URL? = if (System.getenv().containsKey("EXCEPTION_SERVICE_WEB_HOOK_URL")) { URL(System.getenv("EXCEPTION_SERVICE_WEB_HOOK_URL")) } else null

    init {
        fun stackTraceToString(exception: Throwable): String {
            val msg = StringBuilder()
            exception.stackTrace.forEach {
                msg.append("${it.classLoaderName} -> ${it.className}.${it.methodName} Line: ${it.lineNumber} (File: ${it.fileName}, Module: ${it.moduleName}-${it.moduleVersion})\n")
            }
            msg.append("Child: \n")
            msg.append(exception.cause?.let { stackTraceToString(it) } ?: "None")
            return msg.toString()
        }
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            exception.printStackTrace()

            val msg =
                StringBuilder("Got Exception in Thread ${thread.name} with Id: ${thread.id} [Daemon: ${if (thread.isDaemon) "yes" else "no"}, Priority: ${thread.priority}]\n\n")

            msg.append("Message: ${exception.message}\n")
            msg.append("Stacktrace: \n")

            msg.append(stackTraceToString(exception))


            val pasteBuilder = StringBuilder("_________________________________________________________________\n\n")

            pasteBuilder.append(
                "     _______                 __   _      ______  __                         __  \n" + "    |_   __ \\\\               |  ] (_)   .' ___  |[  |                       |  ] \n" + "      | |__) |  .---.   .--.| |  __   / .'   \\\\_| | |  .--.   __   _    .--.| |  \n" + "      |  __ /  / /__\\\\\\\\/ /'`\\\\' | [  |  | |        | |/ .'`\\\\ \\\\[  | | | / /'`\\\\' |  \n" + "     _| |  \\\\ \\\\_| \\\\__.,| \\\\__/  |  | |  \\\\ `.___.'\\\\ | || \\\\__. | | \\\\_/ |,| \\\\__/  |  \n" + "    |____| |___|'.__.' '.__.;__][___]  `.____ .'[___]'.__.'  '.__.'_/ '.__.;__] " + "\n\n"
            )
            pasteBuilder.append("Service: $service\n")
            pasteBuilder.append(msg.toString())
            pasteBuilder.append("\n\n_________________________________________________________________")

            val paste = pasteBuilder.toString().haste() //Upload to hastebin

            send(paste.url) //call discord webhook
        }
    }

    /*
    Calls discord`s webhook to send a message
     */
    private fun send(hasteURL: URL) {

        if (webhookURL == null) return

        val request = HttpRequest.newBuilder(webhookURL.toURI()).header("Content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{ \"username\": \"Exception Log\", \"avatar_url\": \"\", \"content\": \"Server Throwed Exception on Service: $service -> ${hasteURL}\" }"))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())

    }

    fun String.haste(hasteServer: String = hasteURL): HasteResult {
        val request =
            HttpRequest.newBuilder(URI("$hasteServer/documents")).POST(HttpRequest.BodyPublishers.ofString(this))
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        val key = gson.fromJson(response.body(), HasteResponse::class.java)?.key
            ?: throw IllegalStateException("Cannot parse Key from Haste Result")

        return HasteResult(URL("$hasteServer/$key"), URL("$hasteServer/raw/$key"), key)
    }

    data class HasteResponse(val key: String)

    data class HasteResult(val url: URL, val rawUrl: URL, val key: String)

}