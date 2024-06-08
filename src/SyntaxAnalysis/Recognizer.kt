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
        return true
    }


    fun recognizePREDEF(): Boolean {
        return true
    }

    fun recognizeSCHEMAS(): Boolean {
        return true
    }

    fun recognizeSCHEMAS2(): Boolean {
    return true
}

    fun recognizePROCEDURE(): Boolean {
        return true
    }

    fun recognizeARGUMENTS(): Boolean {
        return true
    }

    fun recognizeARGUMENTS2(): Boolean {
    return true
    }

    fun recognizeCITY(): Boolean {
        return true
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