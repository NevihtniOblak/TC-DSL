package SyntaxAnalysis

import LexAnalysis.Lexer
import LexAnalysis.Symbol
import LexAnalysis.Token

class Recognizer(private val lexer: Lexer) {
    private var currentSymbol: Token? = null


    fun recognizeStart(): Boolean {
        currentSymbol = lexer.getToken()
        //println("Current symbol: "+currentSymbol)
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
        var v1 = recognizePREDEF()
        var v2 = recognizeCITY()
        return v1 && v2
    }


    fun recognizePREDEF(): Boolean {
        if(currentSymbol!!.symbol in setOf()){
            var v1 = recognizePROCEDURE()
            var v2 = recognizePREDEF()
            return v1 && v2
        }
        else if(currentSymbol!!.symbol in setOf()){
            var v1 = recognizeSCHEMAS()
            var v2 = recognizePREDEF()
            return v1 && v2
        }
        else{
            //epsilon
            return true
        }
    }

    fun recognizeSCHEMAS(): Boolean {
        var v1 = recognizeTerminal(Symbol.SCHEMA)

        var v2 = recognizeSCHEMAS2()
        var v3 = recognizeSCHEMAS()

        return v1 && v2 && v3
    }

    fun recognizeSCHEMAS2(): Boolean {
        if(currentSymbol!!.symbol in setOf()){
            var v1 = recognizeINFRASTRUCTURE()
            return v1
        }
        else if(currentSymbol!!.symbol in setOf()){
            var v1 = recognizeSPECS()
            return v1
        }
        else{
            return false
        }
}

    fun recognizePROCEDURE(): Boolean {
        var v1 = recognizeTerminal(Symbol.PROCEDURE)
        var v2 = recognizeTerminal(Symbol.VARIABLE)
        var v3 = recognizeTerminal(Symbol.LPAREN)
        var v4 = recognizeARGUMENTS()
        var v5 = recognizeTerminal(Symbol.RPAREN)
        var v6 = recognizeTerminal(Symbol.LCURLY)
        var v7 = recognizeCOMPONENTS()
        var v8 = recognizeTerminal(Symbol.RCURLY)

        return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8
    }

    fun recognizeARGUMENTS(): Boolean {

        if(currentSymbol!!.symbol in setOf()){
            var v1 = recognizeTerminal(Symbol.VARIABLE)
            var v2 = recognizeARGUMENTS2()
            return v1 && v2
        }
        else{
            //epsilon
            return true
        }
    }

    fun recognizeARGUMENTS2(): Boolean {

        if(currentSymbol!!.symbol in setOf()){
            var v1 = recognizeTerminal(Symbol.COMMA)
            var v2 = recognizeTerminal(Symbol.VARIABLE)
            var v3 = recognizeARGUMENTS2()
            return v1 && v2 && v3
        }
        else{
            //epsilon
            return true
        }
    }


    fun recognizeCITY(): Boolean {
        var v1 = recognizeTerminal(Symbol.CITY)
        var v2 = recognizeTerminal(Symbol.COLON)
        var v3 = recognizeTerminal(Symbol.STRING)
        var v4 = recognizeTerminal(Symbol.LPAREN)
        var v5 = recognizeCOMPONENTS()
        var v6 = recognizeTerminal(Symbol.RPAREN)

        return v1 && v2 && v3 && v4 && v5 && v6
    }

    fun recognizeCOMPONENTS(): Boolean {
        return true
    }

    fun recognizeINFRASTRUCTURE(): Boolean {
        return true
    }

    fun recognizeINFNAMES(): Boolean {
        return true
    }

    fun recognizeCONTAINERS(): Boolean {
        return true
    }

    fun recognizeCONTNAMES(): Boolean {
        return true
    }

    fun recognizeREF(): Boolean {
        return true
    }

    fun recognizeTAG(): Boolean {
        return true
    }

    fun recognizeRENDER(): Boolean {
        return true
    }

    fun recognizeRENDERCONT(): Boolean {
        return true
    }

    fun recognizeEFFECT(): Boolean {
        return true
    }

    fun recognizeCOMMANDS(): Boolean {
        return true
    }

    fun recognizeSPECS(): Boolean {
        return true
    }

    fun recognizePOLYARGS(): Boolean {
        return true
    }

    fun recognizePOLYARGS2(): Boolean {
    return true
    }

    fun recognizeSTMTS(): Boolean {
        return true
    }

    fun recognizeASSIGNS(): Boolean {
        return true
    }

    fun recognizeCONSTRUCTNAMES(): Boolean {
        return true
    }

    fun recognizeEXP(): Boolean {
        return true
    }

    fun recognizeADDITIVE(): Boolean {
        return true
    }

    fun recognizeADDITIVE2(): Boolean {
    return true
    }

    fun recognizeMULTIPLICATIVE(): Boolean {
        return true
    }

    fun recognizeMULTIPLICATIVE2(): Boolean {
    return true
    }

    fun recognizeEXPONENTIAL(): Boolean {
        return true
    }

    fun recognizeEXPONENTIAL2(): Boolean {
    return true
    }

    fun recognizeUNARY(): Boolean {
        return true
    }

    fun recognizePRIMARY(): Boolean {
        return true
    }

    fun recognizePRIMARY1(): Boolean {
        return true
    }

    fun recognizePRIMARY2(): Boolean {
        return true
    }

    fun recognizeDATA(): Boolean {
        return true
    }

    fun recognizeLIST(): Boolean {
        return true
    }

    fun recognizeLISTITEM(): Boolean {
        return true
    }

    fun recognizeLISTITEM2(): Boolean {
    return true
    }

}