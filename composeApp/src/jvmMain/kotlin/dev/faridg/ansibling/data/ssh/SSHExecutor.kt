package dev.faridg.ansibling.data.ssh

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import dev.faridg.ansibling.domain.Device
import dev.faridg.ansibling.domain.StatusType

class SSHExecutor(
    device: Device
) {

    private val session: Session

    init {
        val jsch = JSch()
        session = jsch.getSession(
            device.username,
            device.ip,
            device.port
        )
        session.setPassword(device.password)
        session.setConfig("StrictHostKeyChecking", "no")
        session.connect(10_000)
    }

    suspend fun executeCommand(
        command: String,
        onOutput: suspend (String, StatusType) -> Unit
    ) {
        val channel = session.openChannel("exec") as ChannelExec
        channel.setPty(true)
        channel.setCommand(command)

        val stdout = channel.inputStream
        val stderr = channel.errStream

        channel.connect()

        stdout.bufferedReader().useLines { lines ->
            lines.forEach {
                onOutput(it, StatusType.SUCCESS)
            }
        }

        stderr.bufferedReader().useLines { lines ->
            lines.forEach {
                onOutput(it, StatusType.ERROR)
            }
        }

        while (!channel.isClosed) {
            Thread.sleep(50)
        }

        val exitStatus = channel.exitStatus
        channel.disconnect()

        if (exitStatus == 0) {
            onOutput("✔ Command succeeded", StatusType.INFO)
        } else {
            throw RuntimeException("Command failed ($exitStatus)")
        }
    }

    fun close() {
        session.disconnect()
    }
}
