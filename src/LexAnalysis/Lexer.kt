package LexAnalysis

import java.io.InputStream
import java.io.OutputStream


class Lexer(private val automaton: DFA, private val stream: InputStream) {
    private var last: Int? = null
    private var row = 1
    private var column = 1

    private fun updatePosition(code: Int) {
        if (code == NEWLINE) {
            row += 1
            column = 1
        } else {
            column += 1
        }
    }

    fun getToken(): Token {
        val startRow = row
        val startColumn = column
        val buffer = mutableListOf<Char>()

        var code = last ?: stream.read()
        var state = automaton.startState
        while (true) {
            val nextState = automaton.next(state, code)
            if (nextState == ERROR_STATE) break // Longest match

            state = nextState
            updatePosition(code)
            buffer.add(code.toChar())
            code = stream.read()
        }
        last = code // The code following the current lexeme is the first code of the next lexeme

        if (automaton.finalStates.contains(state)) {
            val symbol = automaton.symbol(state)
            return if (symbol == Symbol.SKIP) {
                getToken()
            } else {
                val lexeme = String(buffer.toCharArray())
                Token(symbol, lexeme, startRow, startColumn)
            }
        } else {
            throw Error("Invalid pattern at ${row}:${column}")
        }
    }


    fun printTokens(scanner: Lexer, output: OutputStream) {
        val writer = output.writer(Charsets.UTF_8)

        var token = this.getToken()
        while (token.symbol != Symbol.EOF) {
            writer.append("${token.symbol.name()}(\"${token.lexeme}\") ") // The output ends with a space!            token = this.getToken()
        }
        writer.appendLine()
        writer.flush()
    }

    companion object{
        const val ERROR_STATE = 0
        const val EOF = -1
        const val NEWLINE = '\n'.code
    }

}