import AST.EnvType
import AST.Environment
import AST.Parser
import LexAnalysis.ForForeachFFFAutomaton
import LexAnalysis.Lexer
import SyntaxAnalysis.Recognizer
import java.io.File

fun main(args: Array<String>) {
    val input = File(args[0]).readText()
    val outputFile = File(args[1])

    //Lexer(ForForeachFFFAutomaton, "SCHEMA-for+for130+-*\"oo1!?\"// / ^[]displayMarkerscallprint for=varCirclePolygonLineBoxsetMarker rotatetranslate;;setLocation<>ParkBuilding-ComplexShop-Mercator Shop-Tus Path Aqua Rail Road Building:City,{}(()SCHEMA) procedureSCHEMASCHEMA".byteInputStream()).printTokens(System.out)
    //Lexer(ForForeachFFFAutomaton, "for".byteInputStream()).printTokens(System.out)
    //Lexer(ForForeachFFFAutomaton,  "print".byteInputStream()).printTokens(outputFile.outputStream())

    //val result = Recognizer(Lexer(ForForeachFFFAutomaton,  input.byteInputStream())).recognizeStart()

    val AST = Parser(Lexer(ForForeachFFFAutomaton,  input.byteInputStream())).parseStart()

    val environment: Environment = mutableMapOf(
        EnvType.VARIABLE to mutableMapOf(),
        EnvType.PROCEDURE to mutableMapOf(),
        EnvType.SCHEMA to mutableMapOf()
    )

    outputFile.writeText(AST.eval(environment))

    /*
    if(result){
        print("The input is a valid program")
    }
    else{
        print("The input is NOT a valid program")
    }

     */


}