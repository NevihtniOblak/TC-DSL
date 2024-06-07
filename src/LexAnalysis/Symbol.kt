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
    COLON,
    BUILDING,
    ROAD,
    RAIL,
    AQUA,


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
            COLON -> "COLON"
            BUILDING -> "BUILDING"
            ROAD -> "ROAD"
            RAIL -> "RAIL"
            AQUA -> "AQUA"


            else -> throw Error("Invalid symbol")
        }
}