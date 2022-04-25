package chats

object ChatService {
    private val chats = mutableMapOf<Int, Chat>()
    private var lastChatId = 0

    fun createChat(firstUserId: Int, secondUserId: Int, text: String): Int {
        val chatId = getChatId(firstUserId, secondUserId)
        if (chatId != 0) {
            return chatId
        }
        chats[++lastChatId] = Chat(firstUserId, secondUserId, id = lastChatId)
        addMessage(firstUserId, lastChatId, text)
        return lastChatId
    }

    fun getChatId(firstUserId: Int, secondUserId: Int): Int {
        chats.asSequence()
            .find { it.value.firstUserId == firstUserId && it.value.secondUserId == secondUserId
                    || it.value.firstUserId == secondUserId && it.value.secondUserId == firstUserId }
            ?.let { return it.key }
        return 0
    }

    fun getChats(userId: Int): Map<Int, Chat> = chats.filter {
        it.value.firstUserId == userId || it.value.secondUserId == userId
    }

    fun getChat(chatId: Int): Chat = chats[chatId] ?: throw ChatNotFoundException("Chat id=$chatId not found")

    fun isChat(firstUserId: Int, secondUserId: Int): Boolean = getChatId(firstUserId, secondUserId) != 0

    fun removeChat(chatId: Int): Chat? = chats.remove(chatId)

    fun addMessage(userId: Int, chatId: Int, text: String): Int = chats[chatId]?.addMessage(userId, text)
        ?: throw ChatNotFoundException("Chat id=$chatId not found")

    fun updateMessage(userId: Int, chatId: Int, messageId: Int, text: String): Boolean =
        chats[chatId]?.updateMessage(userId, messageId, text)
            ?: throw ChatNotFoundException("Chat id=$chatId not found")

    fun removeMessage(chatId: Int, messageId: Int): Boolean {
        chats[chatId]?.removeMessage(messageId) ?: throw ChatNotFoundException("Chat id=$chatId not found")
        if ((chats[chatId]?.countMessages() ?: 1) > 0) {
            return false
        }
        chats.remove(chatId)
        return true
    }

    fun getUnreadMessages(userId: Int, chatId: Int): List<Message> =
        chats[chatId]?.getUnreadMessages(userId) ?: throw ChatNotFoundException("Chat id=$chatId not found")

    private fun isUnreadMessagesInChat(userId: Int, chatId: Int) = getUnreadMessages(userId, chatId).isNotEmpty()

    fun getUnreadChats(userId: Int): Map<Int, Chat> = getChats(userId).filter {
        isUnreadMessagesInChat(userId, it.key)
    }

    fun getUnreadChatsCount(userId: Int) : Int = getUnreadChats(userId).size

    fun openChat(userId: Int, chatId: Int) {
        chats[chatId]?.open(userId) ?: throw ChatNotFoundException("Chat id=$chatId not found")
    }

    fun getMessages(chatId: Int, messageIdFrom: Int, messageIdTo: Int) =
        chats[chatId]?.getMessages(messageIdFrom, messageIdTo)
            ?: throw ChatNotFoundException("Chat id=$chatId not found")

    fun getMessages(chatId: Int) =
        chats[chatId]?.getMessages() ?: throw ChatNotFoundException("Chat id=$chatId not found")

    fun clear() {
        chats.clear()
        lastChatId = 0
    }
}