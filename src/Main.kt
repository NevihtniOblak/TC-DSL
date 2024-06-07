import LexAnalysis.ForForeachFFFAutomaton
import LexAnalysis.Lexer

fun main(args: Array<String>) {
    Lexer(ForForeachFFFAutomaton, "SCHEMA SCHEMASCHEMA".byteInputStream()).printTokens(System.out)
}