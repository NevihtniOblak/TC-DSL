import LexAnalysis.ForForeachFFFAutomaton
import LexAnalysis.Lexer

fun main(args: Array<String>) {
    Lexer(ForForeachFFFAutomaton, "Building:City,{}(()SCHEMA) procedureSCHEMASCHEMA".byteInputStream()).printTokens(System.out)
}