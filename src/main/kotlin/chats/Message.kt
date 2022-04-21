package chats

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Message(
    val userId: Int,
    val text: String,
    val dateTime: LocalDateTime,
    val id: Int,
    val changed: Boolean = false
){
    private val dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH-mm-ss")

    override fun toString(): String {
        return "$text\n(${dateTime.format(dtf)}" + (if (changed) ", изменено" else "") + ", id=$id)"
    }
}