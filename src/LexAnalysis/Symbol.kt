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
            PATH -> "PATH"
            SHOP_TUS -> "SHOP-TUS"
            SHOP_MERCATOR -> "SHOP-MERCATOR"
            BUILDING_COMPLEX -> "BUILDING-COMPLEX"
            PARK -> "PARK"
            LANGLE -> "LANGLE"
            RANGLE -> "RANGLE"
            SET_LOCATION -> "SET_LOCATION"



            else -> throw Error("Invalid symbol")
        }
}