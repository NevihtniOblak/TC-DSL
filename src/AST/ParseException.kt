package AST

import LexAnalysis.Symbol

class ParseException(symbol: Symbol, lexeme: String, row: Int, column: Int) : Exception("PARSE ERROR (${(symbol.value())}, $lexeme) at $row:$column")
