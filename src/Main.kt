import LexAnalysis.ForForeachFFFAutomaton
import LexAnalysis.Lexer

fun main(args: Array<String>) {
    //Lexer(ForForeachFFFAutomaton, "130+-*\"oo1!?\"// / ^[]displayMarkerscallprint for=varCirclePolygonLineBoxsetMarker rotatetranslate;;setLocation<>ParkBuilding-ComplexShop-Mercator Shop-Tus Path Aqua Rail Road Building:City,{}(()SCHEMA) procedureSCHEMASCHEMA".byteInputStream()).printTokens(System.out)
    Lexer(ForForeachFFFAutomaton, "".byteInputStream()).printTokens(System.out)

}