package AST

enum class Type {
    REAL, STRING, LIST, POINT, undefined
}

data class Value(val type: Type, val value: String) {
}