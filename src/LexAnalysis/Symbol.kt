package LexAnalysis

enum class Symbol {
    EOF,
    SKIP,
    SCHEMA,
    PROCEDURE,
    LPAREN,
    RPAREN,
    ;

    fun value(): String =
        when (this) {
            SCHEMA -> "SCHEMA"
            PROCEDURE -> "procedure"
            LPAREN -> "lparen"
            RPAREN -> "rparen"

            else -> throw Error("Invalid symbol")
        }
}