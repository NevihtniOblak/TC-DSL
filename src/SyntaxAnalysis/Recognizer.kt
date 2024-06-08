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


    //RAZPOZNAVALNE FUNKCIJE


    fun recognizePROGRAM(): Boolean {
        var v1 = recognizePREDEF()
        var v2 = recognizeCITY()
        return v1 && v2
    }


    fun recognizePREDEF(): Boolean {
        if(currentSymbol!!.symbol in setOf(Symbol.PROCEDURE)){
            var v1 = recognizePROCEDURE()
            var v2 = recognizePREDEF()
            return v1 && v2
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.SCHEMA)){
            var v1 = recognizeSCHEMAS()
            var v2 = recognizePREDEF()
            return v1 && v2
        }
        else if (currentSymbol!!.symbol in setOf(Symbol.CITY)){
            //epsilon
            return true
        }
        else{
            return false
        }
    }

    fun recognizeSCHEMAS(): Boolean {
        var v1 = recognizeTerminal(Symbol.SCHEMA)

        var v2 = recognizeSCHEMAS2()
        var v3 = recognizeSCHEMAS()

        return v1 && v2 && v3
    }

    fun recognizeSCHEMAS2(): Boolean {
        if(currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA,
                Symbol.PATH, Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)){
            var v1 = recognizeINFRASTRUCTURE()
            return v1
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON, Symbol.CIRCLE, Symbol.CIRCLELINE)){
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

        if(currentSymbol!!.symbol in setOf(Symbol.VARIABLE)){
            var v1 = recognizeTerminal(Symbol.VARIABLE)
            var v2 = recognizeARGUMENTS2()
            return v1 && v2
        }
        else if (currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            //epsilon
            return true
        }
        else{
            return false
        }
    }

    fun recognizeARGUMENTS2(): Boolean {

        if(currentSymbol!!.symbol in setOf(Symbol.COMMA)){
            var v1 = recognizeTerminal(Symbol.COMMA)
            var v2 = recognizeTerminal(Symbol.VARIABLE)
            var v3 = recognizeARGUMENTS2()
            return v1 && v2 && v3
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            //epsilon
            return true
        }
        else{
            return false
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

        if(currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA, Symbol.PATH,
                Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)){
            var v1 = recognizeINFRASTRUCTURE()
            var v2 = recognizeCOMPONENTS()

            return v1 && v2
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.BUILDING_COMPLEX, Symbol.PARK)){
            var v1 = recognizeCONTAINERS()
            var v2 = recognizeCOMPONENTS()

            return v1 && v2
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)){
            var v1 = recognizeSTMTS()
            var v2 = recognizeCOMPONENTS()

            return v1 && v2
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.RCURLY, Symbol.RPAREN)){
            //epsilon
            return true
        }
        else{
            return false
        }
    }

    fun recognizeINFRASTRUCTURE(): Boolean {
        var v1 = recognizeINFNAMES()
        var v2 = recognizeTAG()
        var v3 = recognizeTerminal(Symbol.COLON)
        var v4 = recognizeTAG()
        var v5 = recognizeTerminal(Symbol.LPAREN)
        var v6 = recognizeRENDER()
        var v7 = recognizeTerminal(Symbol.RPAREN)
        var v8 = recognizeTerminal(Symbol.LCURLY)
        var v9 = recognizeEFFECT()
        var v10 = recognizeTerminal(Symbol.RCURLY)

        return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10
    }

    fun recognizeINFNAMES(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.BUILDING)) {
            var v1 = recognizeTerminal(Symbol.BUILDING)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.ROAD)) {
            var v1 = recognizeTerminal(Symbol.ROAD)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.RAIL)) {
            var v1 = recognizeTerminal(Symbol.RAIL)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.AQUA)) {
            var v1 = recognizeTerminal(Symbol.AQUA)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.PATH)) {
            var v1 = recognizeTerminal(Symbol.PATH)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.SHOP_TUS)) {
            var v1 = recognizeTerminal(Symbol.SHOP_TUS)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.SHOP_MERCATOR)) {
            var v1 = recognizeTerminal(Symbol.SHOP_MERCATOR)
            return v1
        } else {
            return false
        }
    }


    fun recognizeCONTAINERS(): Boolean {
        var v1 = recognizeCONTNAMES()
        var v2 = recognizeTAG()
        var v3 = recognizeTerminal(Symbol.COLON)
        var v4 = recognizeTAG()
        var v5 = recognizeTerminal(Symbol.LPAREN)
        var v6 = recognizeRENDERCONT()
        var v7 = recognizeTerminal(Symbol.RPAREN)
        var v8 = recognizeTerminal(Symbol.LCURLY)
        var v9 = recognizeEFFECT()
        var v10 = recognizeTerminal(Symbol.RCURLY)

        return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10

    }


    fun recognizeCONTNAMES(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.BUILDING_COMPLEX)) {
            val v1 = recognizeTerminal(Symbol.BUILDING_COMPLEX)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.PARK)) {
            val v1 = recognizeTerminal(Symbol.PARK)
            return v1
        }
        else{
            return false
        }
    }


    fun recognizeTAG(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.LANGLE)) {
            val v1 = recognizeTerminal(Symbol.LANGLE)
            val v2 = recognizeEXP()
            val v3 = recognizeTerminal(Symbol.RANGLE)
            return v1 && v2 && v3
        } else if( currentSymbol!!.symbol in setOf( Symbol.LPAREN)){
            // EPSILON case
            return true
        }
        else{
            return false
        }
    }

    fun recognizeREF(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.LANGLE)) {
            val v1 = recognizeTerminal(Symbol.LANGLE)
            val v2 = recognizeEXP()
            val v3 = recognizeTerminal(Symbol.RANGLE)
            return v1 && v2 && v3
        } else if( currentSymbol!!.symbol in setOf( Symbol.COLON)){
            // EPSILON case
            return true
        }
        else{
            return false
        }
    }


    fun recognizeRENDER(): Boolean {
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val v1 = recognizeSTMTS()
            val v2 = recognizeRENDER()
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON,
                Symbol.CIRCLE, Symbol.CIRCLELINE)) {
            val v1 = recognizeSPECS()
            val v2 = recognizeRENDER()
            return v1 && v2
        } else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            // EPSILON case
            return true
        }
        else{
            return false
        }
    }


    fun recognizeRENDERCONT(): Boolean {
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val v1 = recognizeSTMTS()
            val v2 = recognizeRENDERCONT()
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON,
                Symbol.CIRCLE, Symbol.CIRCLELINE)) {
            val v1 = recognizeSPECS()
            val v2 = recognizeRENDERCONT()
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA,
                Symbol.PATH, Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)) {
            val v1 = recognizeINFRASTRUCTURE()
            val v2 = recognizeRENDERCONT()
            return v1 && v2
        } else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            // EPSILON case
            return true
        }
        else{
            return false
        }
    }


    fun recognizeEFFECT(): Boolean {
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val v1 = recognizeSTMTS()
            val v2 = recognizeEFFECT()
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf(Symbol.SET_LOCATION, Symbol.TRANSLATE, Symbol.ROTATE,
                Symbol.SET_MARKER)) {
            val v1 = recognizeCOMMANDS()
            val v2 = recognizeEFFECT()
            return v1 && v2
        } else if(currentSymbol!!.symbol in setOf(Symbol.RCURLY)){
            // EPSILON case
            return true
        }
        else{
            return false
        }
    }


    fun recognizeCOMMANDS(): Boolean {
        if (currentSymbol!!.symbol in setOf(Symbol.SET_LOCATION)) {
            val v1 = recognizeTerminal(Symbol.SET_LOCATION)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeEXP()
            val v4 = recognizeTerminal(Symbol.RPAREN)
            val v5 = recognizeTerminal(Symbol.SEMICOL)
            return v1 && v2 && v3 && v4 && v5
        } else if (currentSymbol!!.symbol in setOf(Symbol.TRANSLATE)) {
            val v1 = recognizeTerminal(Symbol.TRANSLATE)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeEXP()
            val v4 = recognizeTerminal(Symbol.COMMA)
            val v5 = recognizeEXP()
            val v6 = recognizeTerminal(Symbol.RPAREN)
            val v7 = recognizeTerminal(Symbol.SEMICOL)
            return v1 && v2 && v3 && v4 && v5 && v6 && v7
        } else if (currentSymbol!!.symbol in setOf(Symbol.ROTATE)) {
            val v1 = recognizeTerminal(Symbol.ROTATE)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeEXP()
            val v4 = recognizeTerminal(Symbol.RPAREN)
            val v5 = recognizeTerminal(Symbol.SEMICOL)
            return v1 && v2 && v3 && v4 && v5
        } else if (currentSymbol!!.symbol in setOf(Symbol.SET_MARKER)) {
            val v1 = recognizeTerminal(Symbol.SET_MARKER)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeEXP()
            val v4 = recognizeTerminal(Symbol.RPAREN)
            val v5 = recognizeTerminal(Symbol.SEMICOL)
            return v1 && v2 && v3 && v4 && v5
        } else {
            return false
        }
    }


    fun recognizeSPECS(): Boolean {
        if (currentSymbol!!.symbol in setOf(Symbol.BOX)) {
            val v1 = recognizeTerminal(Symbol.BOX)
            val v2 = recognizeTAG()
            val v3 = recognizeTerminal(Symbol.COLON)
            val v4 = recognizeTAG()
            val v5 = recognizeTerminal(Symbol.LPAREN)
            val v6 = recognizeEXP()
            val v7 = recognizeTerminal(Symbol.COMMA)
            val v8 = recognizeEXP()
            val v9 = recognizeTerminal(Symbol.RPAREN)
            val v10 = recognizeTerminal(Symbol.LCURLY)
            val v11 = recognizeEFFECT()
            val v12 = recognizeTerminal(Symbol.RCURLY)
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12
        } else if (currentSymbol!!.symbol in setOf(Symbol.LINE)) {
            val v1 = recognizeTerminal(Symbol.LINE)
            val v2 = recognizeTAG()
            val v3 = recognizeTerminal(Symbol.COLON)
            val v4 = recognizeTAG()
            val v5 = recognizeTerminal(Symbol.LPAREN)
            val v6 = recognizeEXP()
            val v7 = recognizeTerminal(Symbol.COMMA)
            val v8 = recognizeEXP()
            val v9 = recognizeTerminal(Symbol.COMMA)
            val v10 = recognizeEXP()
            val v11 = recognizeTerminal(Symbol.COMMA)
            val v12 = recognizeEXP()
            val v13 = recognizeTerminal(Symbol.RPAREN)
            val v14 = recognizeTerminal(Symbol.LCURLY)
            val v15 = recognizeEFFECT()
            val v16 = recognizeTerminal(Symbol.RCURLY)
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12 && v13 && v14 && v15 && v16
        } else if (currentSymbol!!.symbol in setOf( Symbol.POLYGON)) {
            val v1 = recognizeTerminal(Symbol.POLYGON)
            val v2 = recognizeTAG()
            val v3 = recognizeTerminal(Symbol.COLON)
            val v4 = recognizeTAG()
            val v5 = recognizeTerminal(Symbol.LPAREN)
            val v6 = recognizePOLYARGS()
            val v7 = recognizeTerminal(Symbol.RPAREN)
            val v8 = recognizeTerminal(Symbol.LCURLY)
            val v9 = recognizeEFFECT()
            val v10 = recognizeTerminal(Symbol.RCURLY)
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10
        } else if (currentSymbol!!.symbol in setOf( Symbol.CIRCLE)) {
            val v1 = recognizeTerminal(Symbol.CIRCLE)
            val v2 = recognizeTAG()
            val v3 = recognizeTerminal(Symbol.COLON)
            val v4 = recognizeTAG()
            val v5 = recognizeTerminal(Symbol.LPAREN)
            val v6 = recognizeEXP()
            val v7 = recognizeTerminal(Symbol.COMMA)
            val v8 = recognizeEXP()
            val v9 = recognizeTerminal(Symbol.RPAREN)
            val v10 = recognizeTerminal(Symbol.LCURLY)
            val v11 = recognizeEFFECT()
            val v12 = recognizeTerminal(Symbol.RCURLY)
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12
        } else if (currentSymbol!!.symbol in setOf( Symbol.CIRCLELINE)) {
            val v1 = recognizeTerminal(Symbol.CIRCLELINE)
            val v2 = recognizeTAG()
            val v3 = recognizeTerminal(Symbol.COLON)
            val v4 = recognizeTAG()
            val v5 = recognizeTerminal(Symbol.LPAREN)
            val v6 = recognizeEXP()
            val v7 = recognizeTerminal(Symbol.COMMA)
            val v8 = recognizeEXP()
            val v9 = recognizeTerminal(Symbol.COMMA)
            val v10 = recognizeEXP()
            val v11 = recognizeTerminal(Symbol.RPAREN)
            val v12 = recognizeTerminal(Symbol.LCURLY)
            val v13 = recognizeEFFECT()
            val v14 = recognizeTerminal(Symbol.RCURLY)
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12 && v13 && v14
        } else {
            return false
        }
    }


    fun recognizePOLYARGS(): Boolean {
        val v1 = recognizeEXP()
        val v2 = recognizeTerminal(Symbol.COMMA)
        val v3 = recognizeEXP()
        val v4 = recognizeTerminal(Symbol.COMMA)
        val v5 = recognizeEXP()
        val v6 = recognizePOLYARGS2()
        return v1 && v2 && v3 && v4 && v5 && v6
    }


    fun recognizePOLYARGS2(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.COMMA)) {
            val v1 = recognizeTerminal(Symbol.COMMA)
            val v2 = recognizeEXP()
            val v3 = recognizePOLYARGS2()
            return v1 && v2 && v3
        } else if(currentSymbol!!.symbol in setOf( Symbol.RPAREN)){
            return true // EPSILON case
        }
        else{
            return false
        }
    }


    fun recognizeSTMTS(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.VAR)) {
            val v1 = recognizeTerminal(Symbol.VAR)
            val v2 = recognizeTerminal(Symbol.VARIABLE)
            val v3 = recognizeTerminal(Symbol.EQUALS)
            val v4 = recognizeDATA()
            val v5 = recognizeTerminal(Symbol.SEMICOL)
            return v1 && v2 && v3 && v4 && v5
        } else if (currentSymbol!!.symbol in setOf( Symbol.VARIABLE)) {
            val v1 = recognizeTerminal(Symbol.VARIABLE)
            val v2 = recognizeASSIGNS()
            val v3 = recognizeTerminal(Symbol.SEMICOL)
            return v1 && v2 && v3
        } else if (currentSymbol!!.symbol in setOf( Symbol.FOR)) {
            val v1 = recognizeTerminal(Symbol.FOR)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeTerminal(Symbol.VAR)
            val v4 = recognizeTerminal(Symbol.VARIABLE)
            val v5 = recognizeTerminal(Symbol.EQUALS)
            val v6 = recognizeEXP()
            val v7 = recognizeTerminal(Symbol.SEMICOL)
            val v8 = recognizeEXP()
            val v9 = recognizeTerminal(Symbol.RPAREN)
            val v10 = recognizeTerminal(Symbol.LCURLY)
            val v11 = recognizeCOMPONENTS()
            val v12 = recognizeTerminal(Symbol.RCURLY)
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12
        } else if (currentSymbol!!.symbol in setOf( Symbol.PRINT)) {
            val v1 = recognizeTerminal(Symbol.PRINT)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeEXP()
            val v4 = recognizeTerminal(Symbol.RPAREN)
            val v5 = recognizeTerminal(Symbol.SEMICOL)
            return v1 && v2 && v3 && v4 && v5
        } else if (currentSymbol!!.symbol in setOf( Symbol.CALL)) {
            val v1 = recognizeTerminal(Symbol.CALL)
            val v2 = recognizeTerminal(Symbol.VARIABLE)
            val v3 = recognizeTerminal(Symbol.LPAREN)
            val v4 = recognizeARGUMENTS()
            val v5 = recognizeTerminal(Symbol.RPAREN)
            val v6 = recognizeTerminal(Symbol.SEMICOL)
            return v1 && v2 && v3 && v4 && v5 && v6
        } else if (currentSymbol!!.symbol in setOf( Symbol.DISPLAY_MARKERS)) {
            val v1 = recognizeTerminal(Symbol.DISPLAY_MARKERS)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeEXP()
            val v4 = recognizeTerminal(Symbol.COMMA)
            val v5 = recognizeEXP()
            val v6 = recognizeTerminal(Symbol.COMMA)
            val v7 = recognizeCONSTRUCTNAMES()
            val v8 = recognizeTerminal(Symbol.RPAREN)
            val v9 = recognizeTerminal(Symbol.SEMICOL)
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9
        } else {
            return false
        }
    }


    fun recognizeASSIGNS(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.EQUALS)) {
            val v1 = recognizeTerminal(Symbol.LSQURE)
            val v2 = recognizeEXP()
            val v3 = recognizeTerminal(Symbol.RSQURE)
            val v4 = recognizeTerminal(Symbol.EQUALS)
            val v5 = recognizeEXP()
            return v1 && v2 && v3 && v4 && v5
        } else if (currentSymbol!!.symbol in setOf( Symbol.LSQURE)) {
            val v1 = recognizeTerminal(Symbol.EQUALS)
            val v2 = recognizeDATA()
            return v1 && v2
        } else {
            return false
        }
    }


    fun recognizeCONSTRUCTNAMES(): Boolean {
        if (currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA,
                Symbol.PATH, Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)) {
            return recognizeINFNAMES()
        } else if (currentSymbol!!.symbol in setOf(Symbol.BUILDING_COMPLEX, Symbol.PARK)) {
            return recognizeCONTNAMES()
        } else {
            return false
        }
    }

    fun recognizeEXP(): Boolean {
        return recognizeADDITIVE()
    }


    fun recognizeADDITIVE(): Boolean {
        val v1 = recognizeMULTIPLICATIVE()
        val v2 = recognizeADDITIVE2()
        return v1 && v2
    }


    fun recognizeADDITIVE2(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.PLUS)) {
            val v1 = recognizeTerminal(Symbol.PLUS)
            val v2 = recognizeMULTIPLICATIVE()
            val v3 = recognizeADDITIVE2()
            return v1 && v2 && v3
        } else if (currentSymbol!!.symbol in setOf( Symbol.MINUS)) {
            val v1 = recognizeTerminal(Symbol.MINUS)
            val v2 = recognizeMULTIPLICATIVE()
            val v3 = recognizeADDITIVE2()
            return v1 && v2 && v3
        } else if(currentSymbol!!.symbol in setOf(Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE)){
            return true // EPSILON case
        }
        else{
            return false
        }
    }


    fun recognizeMULTIPLICATIVE(): Boolean {
        val v1 = recognizeEXPONENTIAL()
        val v2 = recognizeMULTIPLICATIVE2()
        return v1 && v2
    }


    fun recognizeMULTIPLICATIVE2(): Boolean {
        if (currentSymbol!!.symbol in setOf(Symbol.TIMES)) {
            val v1 = recognizeTerminal(Symbol.TIMES)
            val v2 = recognizeEXPONENTIAL()
            val v3 = recognizeMULTIPLICATIVE2()
            return v1 && v2 && v3
        } else if (currentSymbol!!.symbol in setOf( Symbol.DIVIDE)) {
            val v1 = recognizeTerminal(Symbol.DIVIDE)
            val v2 = recognizeEXPONENTIAL()
            val v3 = recognizeMULTIPLICATIVE2()
            return v1 && v2 && v3
        } else if (currentSymbol!!.symbol in setOf( Symbol.INTEGER_DIVIDE)) {
            val v1 = recognizeTerminal(Symbol.INTEGER_DIVIDE)
            val v2 = recognizeEXPONENTIAL()
            val v3 = recognizeMULTIPLICATIVE2()
            return v1 && v2 && v3
        } else if( currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE)) {
            return true // EPSILON case
        }
        else{
            return false
        }
    }


    fun recognizeEXPONENTIAL(): Boolean {
        val v1 = recognizeUNARY()
        val v2 = recognizeEXPONENTIAL2()
        return v1 && v2
    }


    fun recognizeEXPONENTIAL2(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.POW)) {
            val v1 = recognizeTerminal(Symbol.POW)
            val v2 = recognizeUNARY()
            val v3 = recognizeEXPONENTIAL2()
            return v1 && v2 && v3
        } else if(currentSymbol!!.symbol in setOf(Symbol.TIMES, Symbol.DIVIDE, Symbol.INTEGER_DIVIDE, Symbol.PLUS, Symbol.MINUS,
                Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE)){
            return true // EPSILON case
        }
        else{
            return false
        }
    }


    fun recognizeUNARY(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.PLUS)) {
            val v1 = recognizeTerminal(Symbol.PLUS)
            val v2 = recognizePRIMARY()
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf( Symbol.MINUS)) {
            val v1 = recognizeTerminal(Symbol.MINUS)
            val v2 = recognizePRIMARY()
            return v1 && v2
        } else if(currentSymbol!!.symbol in setOf( Symbol.REAL, Symbol.VARIABLE, Symbol.LPAREN, Symbol.STRING)){
            val v1 = recognizePRIMARY()
            return v1
        }
        else{
            return false
        }
    }


    fun recognizePRIMARY(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.REAL)) {
            var v1 = recognizeTerminal(Symbol.REAL)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.VARIABLE)) {
            var v1 = recognizeTerminal(Symbol.VARIABLE)
            val v2 = recognizePRIMARY1()
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf( Symbol.LPAREN)) {
            val v1 = recognizeEXP()
            val v2 = recognizePRIMARY2()
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf( Symbol.STRING)) {
            val v1 = recognizeTerminal(Symbol.STRING)
            return v1
        }
        else{
            return false
        }
    }


    fun recognizePRIMARY1(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.LSQURE)) {
            val v1 = recognizeTerminal(Symbol.LSQURE)
            val v2 = recognizeEXP()
            val v3 = recognizeTerminal(Symbol.RSQURE)
            return v1 && v2 && v3
        } else if(currentSymbol!!.symbol in setOf(Symbol.POW, Symbol.TIMES, Symbol.DIVIDE, Symbol.INTEGER_DIVIDE, Symbol.PLUS, Symbol.MINUS, Symbol.COMMA,
                Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE)){
            return true // EPSILON case
        }
        else{
            return false
        }
    }

    fun recognizePRIMARY2(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.RPAREN)) {
            val v1 = recognizeTerminal(Symbol.RPAREN)
            return v1
        }
        else if(currentSymbol!!.symbol in setOf( Symbol.COMMA)){
            val v1 = recognizeTerminal(Symbol.COMMA)
            val v2 = recognizeEXP()
            val v3 = recognizeTerminal(Symbol.RPAREN)
            return v1 && v2 && v3
        }
        else{
            return false
        }
    }


    fun recognizeDATA(): Boolean {
        if (currentSymbol!!.symbol in setOf(Symbol.LSQURE)) {
            var v1 = recognizeLIST()
            return v1
        } else if(currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS,
                Symbol.REAL, Symbol.VARIABLE, Symbol.LPAREN, Symbol.STRING)){
            var v1 = recognizeEXP()
            return v1
        }
        else{
            return false
        }
    }


    fun recognizeLIST(): Boolean {
        val v1 = recognizeTerminal(Symbol.LSQURE)
        val v2 = recognizeLISTITEM()
        val v3 = recognizeTerminal(Symbol.RSQURE)
        return v1 && v2 && v3
    }


    fun recognizeLISTITEM(): Boolean {
        if (currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.REAL, Symbol.VARIABLE,
                Symbol.LPAREN, Symbol.STRING)) {
            val v1 = recognizeEXP()
            val v2 = recognizeLISTITEM2()
            return v1 && v2
        } else if(currentSymbol!!.symbol in setOf(Symbol.RSQURE)){
            return true
        }
        else{
            return false
        }
    }


    fun recognizeLISTITEM2(): Boolean {
        if (currentSymbol!!.symbol in setOf( Symbol.COMMA)) {
            val v1 = recognizeTerminal(Symbol.COMMA)
            val v2 = recognizeEXP()
            val v3 = recognizeLISTITEM2()
            return v1 && v2 && v3
        } else if( currentSymbol!!.symbol in setOf(Symbol.RSQURE)){
            return true
        }
        else{
            return false
        }
    }


}