import LexAnalysis.ForForeachFFFAutomaton
import LexAnalysis.Lexer

fun main(args: Array<String>) {
    Lexer(ForForeachFFFAutomaton, "<>ParkBuilding-ComplexShop-Mercator Shop-Tus Path Aqua Rail Road Building:City,{}(()SCHEMA) procedureSCHEMASCHEMA".byteInputStream()).printTokens(System.out)
}