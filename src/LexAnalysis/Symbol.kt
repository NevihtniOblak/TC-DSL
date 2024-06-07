package LexAnalysis

enum class Symbol {
    EOF,
    SKIP,
    SCHEMA,
    PROCEDURE,
    LPAREN,
    RPAREN,
    LCURLY,
    RCURLY,
    ;

    fun value(): String =
        when (this) {
            SCHEMA -> "SCHEMA"
            PROCEDURE -> "procedure"
            LPAREN -> "lparen"
            RPAREN -> "rparen"
            LCURLY -> "lcurly"
            RCURLY -> "rcurly"

            else -> throw Error("Invalid symbol")
        }
}