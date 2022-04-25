import chats.ChatService
import users.User
import users.Users

val users = Users()
var selectUserId = 0

fun main() {
    users.add(User("Ярополк", "Петров", "Luxor"))
    users.add(User("Семен", "Семенов", "Semen"))
    users.add(User("Иван", "Иванов", "Ivan"))

    do {
        ColorText.println("Выберите имя пользователя для входа:", ColorText.FONT_YELLOW)
        users.get().forEach { ColorText.println("${it.value.nickname}, id = ${it.key}", ColorText.FONT_YELLOW) }

        do {
            selectUserId = inputNumber("Введите id пользователя: ", 0)
        } while (!users.get().containsKey(selectUserId))
        ColorText.println("Добрый день, ${users.getNickname(selectUserId)} !", ColorText.FONT_VIOLETTE)

        do {
            ColorText.println(
                "Выберите действие:\n" +
                        "1. Создать новый чат\n" +
                        "2. Вывести все чаты\n" +
                        "3. Вывести чаты с непрочитанными сообщениями\n" +
                        "4. Зайти под другим пользователем", ColorText.FONT_YELLOW
            )
            val selectAction = inputNumber("Введите номер: ", 4)

            when (selectAction) {
                1 -> createNewChat()
                2 -> printAllChats()
                3 -> printUnreadChats()
            }

        } while (selectAction != 4)

    } while (true)

}

fun inputNumber(text: String, max: Int): Int {
    do {
        ColorText.print(text, ColorText.FONT_GREEN)
        try {
            val input = readLine()?.trim()?.toInt() ?: throw NumberFormatException()
            if ((max != 0) && (input <= 0 || input > max)) {
                throw NumberFormatException()
            }
            return input
        } catch (e: NumberFormatException) {
            ColorText.printlnError("Некорректный ввод")
        }
    } while (true)
}

fun createNewChat() {
    ColorText.println("Выберите собеседника:", ColorText.FONT_YELLOW)
    users.get().forEach {
        if (it.key != selectUserId) {
            ColorText.println("${it.value.nickname}, id = ${it.key}", ColorText.FONT_YELLOW)
        }
    }

    var companionId: Int
    do {
        companionId = inputNumber("Введите id пользователя: ", 0)
    } while (!users.get().containsKey(companionId) || companionId == selectUserId)

    if (ChatService.isChat(selectUserId, companionId)) {
        ColorText.printlnError("Чат с пользователем ${users.getNickname(companionId)} уже есть:")
        openChat(ChatService.getChatId(selectUserId, companionId))
        return
    }

    ColorText.print("Ваше сообщение: ", ColorText.FONT_GREEN)
    val text = readLine()?.trim() ?: return
    ChatService.createChat(selectUserId, companionId, text)
    ColorText.println("Чат с пользователем ${users.getNickname(companionId)} создан!", ColorText.FONT_VIOLETTE)
}

fun printAllChats() {
    ColorText.println("Текущие чаты:", ColorText.FONT_YELLOW)
    val currentChats = ChatService.getChats(selectUserId)

    if (currentChats.isEmpty()) {
        ColorText.printlnError("Нет текущих чатов!")
        return
    }

    currentChats.forEach {
        val userId = if (it.value.firstUserId != selectUserId) it.value.firstUserId else it.value.secondUserId
        ColorText.println("${users.getNickname(userId)}, id чата = ${it.key}", ColorText.FONT_YELLOW)
    }

    var chatId: Int
    do {
        chatId = inputNumber("Введите id чата или 0 для возврата: ", 0)
    } while (!currentChats.containsKey(chatId) && chatId != 0)

    if (chatId != 0) {
        openChat(chatId)
    }
}

fun openChat(chatId: Int) {
    val messages = ChatService.getMessages(chatId)

    val nick = if (ChatService.getChat(chatId).firstUserId != selectUserId)
        users.getNickname(ChatService.getChat(chatId).firstUserId)
    else users.getNickname(ChatService.getChat(chatId).secondUserId)

    val unreadMessages = ChatService.getUnreadMessages(selectUserId, chatId)
    val unreadMessagesId = if (unreadMessages.isNotEmpty()) unreadMessages[0].id else 0

    ChatService.openChat(selectUserId, chatId)
    messages.forEach {
        if (unreadMessagesId == it.id) {
            ColorText.println("Новые сообщения:", ColorText.FONT_VIOLETTE)
        }
        val color = if (selectUserId == it.userId) ColorText.FONT_TURQUOISE else ColorText.FONT_BLUE
        val name = if (selectUserId == it.userId) "Я" else nick
        ColorText.println("$name: $it", color)
    }

    do {
        ColorText.println(
            "Выберите действие:\n" +
                    "1. Отправить сообщение\n" +
                    "2. Редактировать сообщение\n" +
                    "3. Удалить сообщение\n" +
                    "4. Удалить чат\n" +
                    "5. Вывести чат\n" +
                    "6. Возврат в меню", ColorText.FONT_YELLOW
        )
        val chatAction = inputNumber("Введите номер: ", 6)

        when (chatAction) {
            1 -> {
                ColorText.print("Введите сообщение: ", ColorText.FONT_GREEN)
                val idMessage = ChatService.addMessage(selectUserId, chatId, readLine() ?: return)
                ColorText.println("Сообщение отправлено! id = $idMessage", ColorText.FONT_YELLOW)
            }
            2 -> editMessage(chatId)
            3 -> {
                if (removeMessage(chatId)) {
                    ColorText.println("Чат удалён!", ColorText.FONT_VIOLETTE)
                    return
                }
            }
            4 -> {
                ChatService.removeChat(chatId)
                ColorText.println("Чат удален!", ColorText.FONT_VIOLETTE)
                return
            }
            5 -> {
                openChat(chatId)
                return
            }
        }
    } while (chatAction != 6)
}

fun editMessage(chatId: Int) {
    val messages = ChatService.getMessages(chatId)
    var messageId: Int
    do {
        messageId = inputNumber("Введите id сообщения или 0 для отмены: ", 0)
        val message = messages.find { it.id == messageId }
        if (message != null && message.userId != selectUserId) {
            ColorText.printlnError("Невозможно редактировать сообщение другого пользователя")
        }
    } while ((message == null || message.userId != selectUserId) && messageId != 0)

    if (messageId == 0) {
        return
    }

    ColorText.print("Введите исправленное сообщение: ", ColorText.FONT_GREEN)
    val text = readLine() ?: return
    ChatService.updateMessage(selectUserId, chatId, messageId, text)
    ColorText.println("Сообщение изменено!", ColorText.FONT_VIOLETTE)
}

fun printUnreadChats() {
    ColorText.println("Чаты с непрочитанными сообщениями: ", ColorText.FONT_YELLOW)
    val unreadChats = ChatService.getUnreadChats(selectUserId)

    if (unreadChats.isEmpty()) {
        ColorText.printlnError("Нет чатов с новыми сообщениями!")
        return
    }

    unreadChats.forEach {
        val userId = if (it.value.firstUserId != selectUserId) it.value.firstUserId else it.value.secondUserId
        ColorText.println("${users.getNickname(userId)}, id чата = ${it.key}", ColorText.FONT_YELLOW)
    }

    var chatId: Int
    do {
        chatId = inputNumber("Введите id чата или 0 для возврата: ", 0)
    } while (!unreadChats.containsKey(chatId) && chatId != 0)

    if (chatId != 0) {
        openChat(chatId)
    }
}

fun removeMessage(chatId: Int): Boolean {
    val messages = ChatService.getMessages(chatId)
    var messageId: Int
    do {
        messageId = inputNumber("Введите id сообщения или 0 для отмены: ", 0)
        val message = messages.find { it.id == messageId }
    } while (message == null && messageId != 0)

    if (messageId == 0) {
        return false
    }
    ColorText.println("Сообщение удалено!", ColorText.FONT_VIOLETTE)
    return ChatService.removeMessage(chatId, messageId)
}