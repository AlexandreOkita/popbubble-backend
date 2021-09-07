package okita.popbubble.server

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import java.lang.Exception
import java.util.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(WebSockets)
    routing {
        val roomsConnections = mutableMapOf<String, MutableSet<Connection>>()

        webSocket("/chat/{roomId}") {
            val roomId = call.parameters["roomId"]!!
            val thisConnection = Connection(this)

            if (roomsConnections.containsKey(roomId)) {
                roomsConnections[roomId]!! += thisConnection
            } else {
                val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
                connections += thisConnection
                roomsConnections[roomId] = connections
            }

            val roomConnection = roomsConnections[roomId]!!

            try {
                send("You are connected! There are ${roomConnection.count()} users here.")
                roomConnection.forEach {
                    it.session.send("${thisConnection.name} acabou de entrar na sala!")
                }
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
                    roomConnection.filter { it != thisConnection } .forEach {
                        it.session.send(textWithUsername)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                roomConnection -= thisConnection
            }
        }
        get("/") {
            call.respondText("Working")
        }
    }
}
