package okita.popbubble.server

class Message(val message: String, val event: Event, val author: Author) {

    fun buildMessage(): String {
        return "{ \"message\": \"$message\", \"event\": \"${event}\", \"author\": \"${author}\" }"
    }
}