package chats

import org.junit.Test

import org.junit.Assert.*

class ChatServiceTest {

    @Test
    fun createChat_createNew() {
        ChatService.clear()
        val firstUserId = 1
        val secondUserId = 2
        val text = "Text"

        val result = ChatService.createChat(firstUserId, secondUserId, text)

        assertEquals(1, result)
    }

    @Test
    fun createChat_notNew() {
        ChatService.clear()
        val firstUserId = 1
        val secondUserId = 2
        val text = "Text"
        ChatService.createChat(firstUserId, secondUserId, text)

        val result = ChatService.createChat(secondUserId, firstUserId, text)

        assertEquals(1, result)
    }

    @Test
    fun getChatId() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        val result = ChatService.getChatId(firstUserId = 2, secondUserId = 1)

        assertEquals(1, result)
    }

    @Test
    fun getChatId_noChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        val result = ChatService.getChatId(firstUserId = 1, secondUserId = 3)

        assertEquals(0, result)
    }

    @Test
    fun getChats() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")
        ChatService.createChat(firstUserId = 3, secondUserId = 4, text = "Text")
        ChatService.createChat(firstUserId = 3, secondUserId = 1, text = "Text")
        val map1 = mutableMapOf<Int, Chat>()
        map1[1] = ChatService.getChat(1)
        map1[3] = ChatService.getChat(3)

        val result = ChatService.getChats(userId = 1)

        assertEquals(map1, result)
    }

    @Test (expected = ChatNotFoundException::class)
    fun getChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        ChatService.getChat(2)
    }

    @Test
    fun isChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        val result = ChatService.isChat(firstUserId = 2, secondUserId = 1)

        assertTrue(result)
    }

    @Test
    fun removeChat_good() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")
        ChatService.createChat(firstUserId = 3, secondUserId = 4, text = "Text")
        val chat2 = ChatService.getChat(chatId = 2)

        val result = ChatService.removeChat(chatId = 2)

        assertEquals(chat2, result)
    }

    @Test
    fun removeChat_noChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        val result = ChatService.removeChat(chatId = 2)

        assertEquals(null, result)
    }

    @Test
    fun addMessage() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        val result = ChatService.addMessage(userId = 1, chatId = 1, text = "Hello")

        assertEquals(2, result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun addMessage_exception_noChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        ChatService.addMessage(userId = 1, chatId = 2, text = "Hello")
    }

    @Test
    fun updateMessage_isMessage() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        val result = ChatService.updateMessage(userId = 1, chatId = 1, messageId = 1, text = "Hi")

        assertTrue(result)
    }

    @Test
    fun updateMessage_noMessage() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        val result = ChatService.updateMessage(userId = 1, chatId = 1, messageId = 2, text = "Hi")

        assertFalse(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun updateMessage_exception_noChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        ChatService.updateMessage(userId = 1, chatId = 2, messageId = 1, text = "Hi")
    }

    @Test
    fun removeMessage_manyMessages() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")
        ChatService.addMessage(userId = 1, chatId = 1, text = "Hello")

        val result = ChatService.removeMessage(chatId = 1, messageId = 2)

        assertFalse(result)
    }

    @Test
    fun removeMessage_deleteChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        val result = ChatService.removeMessage(chatId = 1, messageId = 1)

        assertTrue(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun removeMessage_exception_noChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        ChatService.removeMessage(chatId = 2, messageId = 1)
    }

    @Test
    fun getUnreadMessages() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")
        val list = ChatService.getMessages(1)

        val result = ChatService.getUnreadMessages(userId = 2, chatId = 1)

        assertEquals(list, result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getUnreadMessages_exception_noChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        ChatService.getUnreadMessages(userId = 2, chatId = 2)
    }

    @Test
    fun getUnreadChats() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")
        val map1 = ChatService.getChats(2)

        val result = ChatService.getUnreadChats(userId = 2)

        assertEquals(map1, result)
    }

    @Test
    fun getUnreadChatsCount() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        val result = ChatService.getUnreadChatsCount(userId = 2)

        assertEquals(1, result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun openChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        ChatService.openChat(userId = 1, chatId = 2)
    }

    @Test
    fun getMessages_from_to() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")
        ChatService.addMessage(userId = 1, chatId = 1, text = "Hello")
        ChatService.addMessage(userId = 1, chatId = 1, text = "Hi")
        ChatService.addMessage(userId = 1, chatId = 1, text = "??")
        val list = ChatService.getMessages(1) as MutableList<Message>
        list.removeAt(3)
        list.removeAt(0)

        val result = ChatService.getMessages(chatId = 1, messageIdFrom = 2, messageIdTo = 3)

        assertEquals(list, result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getMessages_from_to_exception_noChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")
        ChatService.addMessage(userId = 1, chatId = 1, text = "Hello")
        ChatService.addMessage(userId = 1, chatId = 1, text = "Hi")

        ChatService.getMessages(chatId = 2, messageIdFrom = 2, messageIdTo = 3)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getMessages_chatId_exception_noChat() {
        ChatService.clear()
        ChatService.createChat(firstUserId = 1, secondUserId = 2, text = "Text")

        ChatService.getMessages(chatId = 2)
    }
}