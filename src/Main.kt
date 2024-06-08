import LexAnalysis.ForForeachFFFAutomaton
import LexAnalysis.Lexer
import SyntaxAnalysis.Recognizer
import java.io.File

fun main(args: Array<String>) {
    val input = File(args[0]).readText()
    val outputFile = File(args[1])

    //Lexer(ForForeachFFFAutomaton, "SCHEMA-for+for130+-*\"oo1!?\"// / ^[]displayMarkerscallprint for=varCirclePolygonLineBoxsetMarker rotatetranslate;;setLocation<>ParkBuilding-ComplexShop-Mercator Shop-Tus Path Aqua Rail Road Building:City,{}(()SCHEMA) procedureSCHEMASCHEMA".byteInputStream()).printTokens(System.out)
    //Lexer(ForForeachFFFAutomaton, "for".byteInputStream()).printTokens(System.out)
    //Lexer(ForForeachFFFAutomaton,  input.byteInputStream()).printTokens(outputFile.outputStream())

    val result = Recognizer(Lexer(ForForeachFFFAutomaton,  input.byteInputStream())).recognizeStart()

    if(result){
        print("The input is a valid program")
    }
    else{
        print("The input is NOT a valid program")
    }

}