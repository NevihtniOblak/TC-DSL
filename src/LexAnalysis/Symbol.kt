package LexAnalysis

enum class Symbol {
    EOF,
    SKIP,
    FOR,
    FOREACH,
    FFF;

    fun value(): String =
        when (this) {
            FOR -> "for"
            FOREACH -> "foreach"
            FFF -> "fff"
            else -> throw Error("Invalid symbol")
        }
}