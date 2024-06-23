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
    PATH,
    SHOP_TUS,
    SHOP_MERCATOR,
    BUILDING_COMPLEX,
    PARK,
    LANGLE,
    RANGLE,
    SET_LOCATION,
    SEMICOL,
    TRANSLATE,
    ROTATE,
    SET_MARKER,
    BOX,
    LINE,
    POLYGON,
    CIRCLE,
    VAR,
    EQUALS,
    FOR,
    PRINT,
    CALL,
    DISPLAY_MARKERS,
    LSQURE,
    RSQURE,
    PLUS,
    MINUS,
    TIMES,
    DIVIDE,
    INTEGER_DIVIDE,
    POW,

    STRING,
    REAL,
    VARIABLE,

    CIRCLELINE,
    TRUE,







    ;

    fun value(): String =
        when (this) {
            SCHEMA -> "SCHEMA"
            PROCEDURE -> "PROCEDURE"
            LPAREN -> "LPAREN"
            RPAREN -> "RPAREN"
            LCURLY -> "LCURLY"
            RCURLY -> "RCURLY"
            COMMA -> "COMMA"
            CITY -> "CITY"
            COLON -> "COLON"
            BUILDING -> "BUILDING"
            ROAD -> "ROAD"
            RAIL -> "RAIL"
            AQUA -> "AQUA"
            PATH -> "PATH"
            SHOP_TUS -> "SHOP-TUS"
            SHOP_MERCATOR -> "SHOP-MERCATOR"
            BUILDING_COMPLEX -> "BUILDING-COMPLEX"
            PARK -> "PARK"
            LANGLE -> "LANGLE"
            RANGLE -> "RANGLE"
            SET_LOCATION -> "SET_LOCATION"
            SEMICOL -> "SEMICOL"
            TRANSLATE -> "TRANSLATE"
            ROTATE -> "ROTATE"
            SET_MARKER -> "SET_MARKER"
            BOX -> "BOX"
            LINE -> "LINE"
            POLYGON -> "POLYGON"
            CIRCLE -> "CIRCLE"
            VAR -> "VAR"
            EQUALS -> "EQUALS"
            FOR -> "FOR"
            PRINT -> "PRINT"
            CALL -> "CALL"
            DISPLAY_MARKERS -> "DISPLAY_MARKERS"
            LSQURE -> "LSQURE"
            RSQURE -> "RSQURE"
            PLUS -> "PLUS"
            MINUS -> "MINUS"
            TIMES -> "TIMES"
            DIVIDE -> "DIVIDE"
            INTEGER_DIVIDE -> "INTEGER_DIVIDE"
            POW -> "POW"

            STRING -> "STRING"
            REAL -> "REAL"
            VARIABLE -> "VARIABLE"

            CIRCLELINE -> "CIRCLELINE"
            TRUE -> "TRUE"




            else -> throw Error("Invalid symbol")
        }
}