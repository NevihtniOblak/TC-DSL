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
    COMMA,
    CITY,
    ;

    fun value(): String =
        when (this) {
            SCHEMA -> "SCHEMA"
            PROCEDURE -> "PROCEDURE"
            LPAREN -> "LPAREN"
            RPAREN -> "rparen"
            LCURLY -> "RPAREN"
            RCURLY -> "RCURLY"
            COMMA -> "COMMA"
            CITY -> "CITY"

            else -> throw Error("Invalid symbol")
        }
}