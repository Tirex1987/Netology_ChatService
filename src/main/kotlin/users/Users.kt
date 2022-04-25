package users

class Users {
    private val users = mutableMapOf<Int, User>()
    private var lastId = 0

    fun add(user: User): Int = users.let {
        it[++lastId] = user.copy(id = lastId)
        lastId
    }

    fun get(id: Int): User = users[id] ?: throw UserNotFoundException("User id=$id not found")


    fun get() = users

    fun getNickname(id: Int) = get(id).nickname

    fun remove(id: Int) = users.remove(id)

    fun count() = users.size
}

data class User(
    val name: String,
    val surname: String,
    val nickname: String,
    val id: Int = 0
)