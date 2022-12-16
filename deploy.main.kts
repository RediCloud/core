@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.jcraft:jsch:0.1.55")

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import java.io.File

val jsch = JSch()
jsch.addIdentity("node01-ssh-key", System.getenv("NODE01_SSH_KEY").toByteArray(), null, null)
val session = jsch.getSession("root", "node01.hosting.suqatri.net", 22).apply {
    setConfig("StrictHostKeyChecking", "no")
    connect()
}
val sftp = session.openChannel("sftp").apply {
    connect()
} as ChannelSftp

fun deploy(from: String, to: String) =
    sftp.put(from, to, ChannelSftp.OVERWRITE)

fun Deploy_main.runCommandSync(it: String) {
    (session.openChannel("exec") as ChannelExec).apply {
        setCommand(it)
        connect()
        while (!isClosed) {
            Thread.sleep(100)
        }
        if (exitStatus != 0) {
            throw RuntimeException(
                "Failed to run command \"${it}\": ${
                    inputStream.readAllBytes().decodeToString()
                }"
            )
        }
    }
}
val files = listOf(
    "api-minestom/build/libs/" to "/home/cloudnet/local/templates/Core/minestom/extensions/core-minestom.jar",
    "api-velocity/build/libs/" to "/home/cloudnet/local/templates/Core/velocity/plugins/core-velocity.jar",
    "api-paper/build/libs/" to "/home/cloudnet/local/templates/Core/paper/plugins/core-paper.jar",
    "api-standalone/build/libs/" to "/home/core/standalone/core-standalone.jar",
)
files.forEach {
    runCommandSync("mkdir -p ${it.second.substringBeforeLast("/")}")
    deploy(File(it.first).listFiles { file ->
        file.name.matches("api-(velocity|paper|minestom|standalone).+.jar".toRegex())
    }[0].absolutePath.also { s -> println("Deploying $s to ${it.second}")}, it.second)
}
sftp.disconnect()
session.disconnect()

