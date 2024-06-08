package SyntaxAnalysis

import LexAnalysis.Lexer
import LexAnalysis.Symbol
import LexAnalysis.Token

class Recognizer(private val lexer: Lexer) {
    private var currentSymbol: Token? = null


    fun recognizeStart(): Boolean {
        currentSymbol = lexer.getToken()
        val result = recognizePROGRAM();

        return when (currentSymbol?.symbol) {
            Symbol.EOF -> result
            else -> false
        }
    }

    private fun recognizeTerminal(symbol: Symbol): Boolean {

        if (currentSymbol?.symbol == symbol) {
            currentSymbol = lexer.getToken()
            return true
        }
        else {
            return false
        }
    }


    fun recognizePROGRAM(): Boolean {
        return true
    }

}