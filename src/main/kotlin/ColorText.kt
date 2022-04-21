object ColorText {
    const val FONT_BLACK = 30
    const val FONT_RED = 31
    const val FONT_GREEN = 32
    const val FONT_YELLOW = 33
    const val FONT_BLUE = 34
    const val FONT_VIOLETTE = 35
    const val FONT_TURQUOISE = 36
    const val FONT_GRAY = 37
    const val FONT_WHITE = 38

    const val BK_BLACK = 40
    const val BK_RED = 41
    const val BK_GREEN = 42
    const val BK_YELLOW = 43
    const val BK_BLUE = 44
    const val BK_VIOLETTE = 45
    const val BK_TURQUOISE = 46
    const val BK_GRAY = 47
    const val BK_WHITE = 48

    fun print(text: String, colorFont: Int, colorBk: Int) {
        print("\u001B[$colorFont;$colorBk" + "m$text\u001B[0m")
    }

    fun println(text: String, colorFont: Int, colorBk: Int) {
        println("\u001B[$colorFont;$colorBk" + "m$text\u001B[0m")
    }

    fun print(text: String, colorFont: Int) {
        print("\u001B[$colorFont" + "m$text\u001B[0m")
    }

    fun println(text: String, color: Int) {
        println("\u001B[$color" + "m$text\u001B[0m")
    }

    fun printlnError(text: String) {
        println("\u001B[31m$text\u001B[0m")
    }
}