package AST

enum class Type {
    REAL, STRING, LIST, POINT, BOOLEAN ,undefined
}

data class Value(val type: Type, val value: MutableList<String>) {
}