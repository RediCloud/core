@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.jcraft:jsch:0.1.55")

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import java.io.File
import java.io.FileFilter

val jsch = JSch().getSession("root", "node01.hosting.suqatri.net", 22).apply {
    setConfig("StrictHostKeyChecking", "no")
    connect()
}
val sftp = jsch.openChannel("sftp").apply {
    connect()
} as ChannelSftp

fun deploy(from: String, to: String) =
    sftp.put(from, to, ChannelSftp.OVERWRITE)

val files = listOf(
    "api-minestom/build/libs/" to "/home/cloudnet/local/templates/Core/minestom/extensions/core-minestom.jar",
    "api-velocity/build/libs/" to "/home/cloudnet/local/templates/Core/velocity/plugins/core-velocity.jar",
    "api-paper/build/libs/" to "/home/cloudnet/local/templates/Core/paper/plugins/core-paper.jar"
)

files.forEach {
    deploy(File(it.first).listFiles { file ->
        file.name.matches("api-(velocity|paper|minestom).+.jar".toRegex())
    }[0].absolutePath, it.second)
}
sftp.disconnect()
jsch.disconnect()