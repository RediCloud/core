package net.dustrean.api.utils

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
    var hasteURL = System.getenv("EXCEPTION_HASTE_URL")
    var webhookURL = URL(System.getenv("EXCEPTION_SERVICE_WEB_HOOK_URL"))

    init {
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            val msg =
                StringBuilder("Got Exception in Thread ${thread.name} with Id: ${thread.id} [Daemon: ${if (thread.isDaemon) "yes" else "no"}, Priority: ${thread.priority}]\n\n")

            msg.append("Message: ${exception.message}\n")
            msg.append("Stacktrace: \n")


            exception.stackTrace.forEach {
                msg.append("${it.classLoaderName} -> ${it.className}.${it.methodName} Line: ${it.lineNumber} (File: ${it.fileName}, Module: ${it.moduleName}-${it.moduleVersion})\n")
            }

            val pasteBuilder =
                StringBuilder("_________________________________________________________________\n\n")

            pasteBuilder.append(
                " ____            _                                     _   \n" +
                        "|  _ \\ _   _ ___| |_ _ __ ___  __ _ _ __    _ __   ___| |_ \n" +
                        "| | | | | | / __| __| '__/ _ \\/ _` | '_ \\  | '_ \\ / _ \\ __|\n" +
                        "| |_| | |_| \\__ \\ |_| | |  __/ (_| | | | |_| | | |  __/ |_ \n" +
                        "|____/ \\__,_|___/\\__|_|  \\___|\\__,_|_| |_(_)_| |_|\\___|\\__|"
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

        val request = HttpRequest.newBuilder(webhookURL.toURI())
            .header("Content-type", "application/json")
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