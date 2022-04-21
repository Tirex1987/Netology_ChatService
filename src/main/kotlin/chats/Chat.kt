package chats

import users.User
import java.time.LocalDateTime

class Chat(
    val firstUserId: Int,
    val secondUserId: Int,
    val id: Int = 0
) {
    private val messages: ArrayList<Message> = ArrayList()
    private var lastReadiedMessageId = 0

    constructor(firstUser: User, secondUser: User) : this(firstUser.id, secondUser.id)

    fun addMessage(userId: Int, text: String) : Int{
        val id = if (messages.size == 0) 1 else messages.last().id + 1
        messages.add(Message(userId, text, LocalDateTime.now(), id = id))
        return messages.last().id
    }

    fun updateMessage(userId: Int, messageId: Int, text: String): Boolean {
        val indexMessage = indexMessageById(messageId)
        if (indexMessage < 0 || messages[indexMessage].userId != userId) {
            return false
        }
        messages[indexMessage].copy(text = text, changed = true).also { messages[indexMessage] = it }
        return true
    }

    fun updateMessage(userId: Int, messageId: Int, message: Message): Boolean {
        return updateMessage(userId, messageId, message.text)
    }

    fun removeMessage(messageId: Int): Boolean {
        val indexMessage = indexMessageById(messageId)
        if (indexMessage < 0) {
            return false
        }
        if (messageId == lastReadiedMessageId) {
            lastReadiedMessageId = if (indexMessage > 0) messages[indexMessage - 1].id else 0
        }
        messages.removeAt(indexMessage)
        return true
    }

    private fun indexMessageById(messageId: Int): Int = messages.indexOf(messages.find { it.id == messageId })

    fun getUnreadMessages(userId: Int): List<Message> =
        messages.filter { it.userId != userId && it.id > lastReadiedMessageId }

    fun open(userId: Int) {
        if (messages.size > 0 && messages.last().userId != userId) {
            lastReadiedMessageId = messages.last().id
        }
    }

    fun getMessages(messageIdFrom: Int, messageIdTo: Int): List<Message> =
        messages.filter { it.id in messageIdFrom..messageIdTo }

    fun getMessages(): List<Message> = messages

    fun countMessages() = messages.size
}