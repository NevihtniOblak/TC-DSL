package LexAnalysis

enum class Symbol {
    EOF,
    SKIP,
    SCHEMA,
    ;

    fun value(): String =
        when (this) {
            SCHEMA -> "SCHEMA"

            else -> throw Error("Invalid symbol")
        }
}