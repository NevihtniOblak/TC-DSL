package AST

import LexAnalysis.Lexer
import LexAnalysis.Symbol
import LexAnalysis.Token


class Parser(private val lexer: Lexer) {
    private var currentSymbol: Token? = null

    private fun panic(): Nothing =
        currentSymbol?.let { throw ParseException(it.symbol, it.lexeme, it.startRow, it.startColumn) } ?: error("cannot happen")


    private fun parseTerminal(symbol: Symbol): String =
        if (currentSymbol?.symbol == symbol) {
            println("Recognized: $currentSymbol")
            val lexme = currentSymbol!!.lexeme
            currentSymbol = lexer.getToken()
            lexme
        } else {
            panic()
        }



    //PARSING FUNKCIJE


    //INH-PROGRAM
    fun recognizePROGRAM(): Program {
        /*
        println("Recognizing PROGRAM")
        var v1 = recognizePREDEF()
        var v2 = recognizeCITY()

        println("PROGRAM RETURN: "+(v1 && v2))
        return v1 && v2

         */
    }


    //INH-PREDEF
    fun recognizePREDEF(): Predef {
        println("Recognizing PREDEF")
        if(currentSymbol!!.symbol in setOf(Symbol.PROCEDURE)){
            var v1 = recognizePROCEDURE()
            var v2 = recognizePREDEF()

            println("PREDEF RETURN: "+(v1 && v2))
            return v1 && v2
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.SCHEMA)){
            var v1 = recognizeSCHEMAS()
            var v2 = recognizePREDEF()

            println("PREDEF RETURN: "+(v1 && v2))
            return v1 && v2
        }
        else if (currentSymbol!!.symbol in setOf(Symbol.CITY)){
            //epsilon
            println("PREDEF RETURN: "+true)
            return true
        }
        else{

            println("PREDEF RETURN: "+false)
            return false
        }
    }

    //INH-PREDEF
    fun recognizeSCHEMAS(): Predef {
        println("Recognizing SCHEMAS")
        var v1 = recognizeTerminal(Symbol.SCHEMA)

        //var v2 = recognizeSCHEMAS2()
        var v2 = recognizeINFRASTRUCTURE()

        println("SCHEMAS RETURN: "+(v1 && v2))
        return v1 && v2
    }


    //INH-PREDEF
    fun recognizePROCEDURE(): Predef {
        println( "Recognizing PROCEDURE")
        var v1 = recognizeTerminal(Symbol.PROCEDURE)
        var v2 = recognizeTerminal(Symbol.VARIABLE)
        var v3 = recognizeTerminal(Symbol.LPAREN)
        var v4 = recognizeARGUMENTS()
        var v5 = recognizeTerminal(Symbol.RPAREN)
        var v6 = recognizeTerminal(Symbol.LCURLY)
        var v7 = recognizeCOMPONENTS()
        var v8 = recognizeTerminal(Symbol.RCURLY)

        println("PROCEDURE RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8))
        return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8
    }

    //INH-ARGUMENTS
    fun recognizeARGUMENTS(): Arguments {
        println("Recognizing ARGUMENTS")

        if(currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.REAL, Symbol.VARIABLE, Symbol.LPAREN, Symbol.STRING)){
            var v1 = recognizeEXP()
            var v2 = recognizeARGUMENTS2()

            println("ARGUMENTS RETURN: "+(v1 && v2))
            return v1 && v2
        }
        else if (currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            //epsilon
            println("ARGUMENTS RETURN: "+true)
            return true
        }
        else{
            println("ARGUMENTS RETURN: "+false)
            return false
        }
    }

    //INH-ARGUMENTS
    fun recognizeARGUMENTS2(): Arguments {
        println("Recognizing ARGUMENTS2")

        if(currentSymbol!!.symbol in setOf(Symbol.COMMA)){
            var v1 = recognizeTerminal(Symbol.COMMA)
            //var v2 = recognizeTerminal(Symbol.VARIABLE)
            var v2 = recognizeEXP()
            var v3 = recognizeARGUMENTS2()

            println("ARGUMENTS2 RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            //epsilon

            println("ARGUMENTS2 RETURN: "+true)
            return true
        }
        else{
            println( "ARGUMENTS2 RETURN: "+false)
            return false
        }
    }


    //INH-CITY
    fun recognizeCITY(): City {
        println("Recognizing CITY")
        var v1 = recognizeTerminal(Symbol.CITY)
        var v2 = recognizeTerminal(Symbol.COLON)
        var v3 = recognizeTerminal(Symbol.STRING)
        var v4 = recognizeTerminal(Symbol.LPAREN)
        var v5 = recognizeCOMPONENTS()
        var v6 = recognizeTerminal(Symbol.RPAREN)


        println("CITY RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6))
        return v1 && v2 && v3 && v4 && v5 && v6
    }

    //INH-COMPONENTS
    fun recognizeCOMPONENTS(): Components {
        println("Recognizing COMPONENTS")

        if(currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA, Symbol.PATH,
                Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)){
            var v1 = recognizeINFRASTRUCTURE()
            var v2 = recognizeCOMPONENTS()

            println("COMPONENTS RETURN: "+(v1 && v2))
            return v1 && v2
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.BUILDING_COMPLEX, Symbol.PARK)){
            var v1 = recognizeCONTAINERS()
            var v2 = recognizeCOMPONENTS()

            println("COMPONENTS RETURN: "+(v1 && v2))
            return v1 && v2
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)){
            var v1 = recognizeSTMTS()
            var v2 = recognizeCOMPONENTS()

            println("COMPONENTS RETURN: "+(v1 && v2))
            return v1 && v2
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON, Symbol.CIRCLE, Symbol.CIRCLELINE)){
            var v1 = recognizeSPECS()
            var v2 = recognizeCOMPONENTS()

            println("COMPONENTS RETURN: "+(v1 && v2))
            return v1 && v2
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.RCURLY, Symbol.RPAREN)){
            //epsilon
            println("COMPONENTS RETURN: "+true)
            return true
        }
        else{
            println("COMPONENTS RETURN: "+false)
            return false
        }
    }

    //INH-COMPONENTS
    fun recognizeINFRASTRUCTURE(): Components {
        println("Recognizing INFRASTRUCTURE")
        var v1 = recognizeINFNAMES()
        var v2 = recognizeREF()
        var v3 = recognizeTerminal(Symbol.COLON)
        var v4 = recognizeTAG()
        var v5 = recognizeTerminal(Symbol.LPAREN)
        var v6 = recognizeRENDER()
        var v7 = recognizeTerminal(Symbol.RPAREN)
        var v8 = recognizeTerminal(Symbol.LCURLY)
        var v9 = recognizeEFFECT()
        var v10 = recognizeTerminal(Symbol.RCURLY)

        println("INFRASTRUCTURE RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10))
        return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10
    }

    //INH-INFNAMES
    fun recognizeINFNAMES(): Infnames {
        println("Recognizing INFNAMES")
        if (currentSymbol!!.symbol in setOf( Symbol.BUILDING)) {
            var v1 = recognizeTerminal(Symbol.BUILDING)
            println("INFNAMES RETURN: "+v1)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.ROAD)) {
            var v1 = recognizeTerminal(Symbol.ROAD)
            println("INFNAMES RETURN: "+v1)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.RAIL)) {
            var v1 = recognizeTerminal(Symbol.RAIL)
            println("INFNAMES RETURN: "+v1)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.AQUA)) {
            var v1 = recognizeTerminal(Symbol.AQUA)
            println("INFNAMES RETURN: "+v1)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.PATH)) {
            var v1 = recognizeTerminal(Symbol.PATH)
            println("INFNAMES RETURN: "+v1)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.SHOP_TUS)) {
            var v1 = recognizeTerminal(Symbol.SHOP_TUS)
            println("INFNAMES RETURN: "+v1)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.SHOP_MERCATOR)) {
            var v1 = recognizeTerminal(Symbol.SHOP_MERCATOR)
            println("INFNAMES RETURN: "+v1)
            return v1
        } else {
            println("INFNAMES RETURN: "+false)
            return false
        }
    }


    //INH COMPONENTS
    fun recognizeCONTAINERS(): Components {
        println("Recognizing CONTAINERS")
        var v1 = recognizeCONTNAMES()
        var v2 = recognizeREF()
        var v3 = recognizeTerminal(Symbol.COLON)
        var v4 = recognizeTAG()
        var v5 = recognizeTerminal(Symbol.LPAREN)
        var v6 = recognizeRENDERCONT()
        var v7 = recognizeTerminal(Symbol.RPAREN)
        var v8 = recognizeTerminal(Symbol.LCURLY)
        var v9 = recognizeEFFECT()
        var v10 = recognizeTerminal(Symbol.RCURLY)

        println("CONTAINERS RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10))
        return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10

    }


    //INH-CONTNAMES
    fun recognizeCONTNAMES(): Contnames {
        println("Recognizing CONTNAMES")
        if (currentSymbol!!.symbol in setOf( Symbol.BUILDING_COMPLEX)) {
            val v1 = recognizeTerminal(Symbol.BUILDING_COMPLEX)
            println("CONTNAMES RETURN: "+v1)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.PARK)) {
            val v1 = recognizeTerminal(Symbol.PARK)
            println("CONTNAMES RETURN: "+v1)
            return v1
        }
        else{
            println("CONTNAMES RETURN: "+false)
            return false
        }
    }


    //INH-TAG
    fun recognizeTAG(): Tag {
        println("Recognizing TAG")
        if (currentSymbol!!.symbol in setOf( Symbol.LANGLE)) {
            val v1 = recognizeTerminal(Symbol.LANGLE)
            val v2 = recognizeEXP()
            val v3 = recognizeTerminal(Symbol.RANGLE)
            return v1 && v2 && v3
        } else if( currentSymbol!!.symbol in setOf( Symbol.LPAREN)){
            // EPSILON case

            println("TAG RETURN: "+true)
            return true
        }
        else{
            println("TAG RETURN: "+false)
            return false
        }
    }

    //INH-REF
    fun recognizeREF(): Ref {
        println("Recognizing REF")
        if (currentSymbol!!.symbol in setOf( Symbol.LANGLE)) {
            val v1 = recognizeTerminal(Symbol.LANGLE)
            val v2 = recognizeEXP()
            val v3 = recognizeTerminal(Symbol.RANGLE)
            println("REF RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        } else if( currentSymbol!!.symbol in setOf( Symbol.COLON)){
            // EPSILON case
            println("REF RETURN: "+true)
            return true
        }
        else{
            println("REF RETURN: "+false)
            return false
        }
    }


    //INH-RENDER
    fun recognizeRENDER(): Render {
        println("Recognizing RENDER")
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val v1 = recognizeSTMTS()
            val v2 = recognizeRENDER()

            println("RENDER RETURN: "+(v1 && v2))
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON,
                Symbol.CIRCLE, Symbol.CIRCLELINE)) {
            val v1 = recognizeSPECS()
            val v2 = recognizeRENDER()
            println("RENDER RETURN: "+(v1 && v2))
            return v1 && v2
        } else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            // EPSILON case
            println("RENDER RETURN: "+true)
            return true
        }
        else{
            println("RENDER RETURN: "+false)
            return false
        }
    }


    //INH-RENDERCONT
    fun recognizeRENDERCONT(): Rendercont {
        println("Recognizing RENDERCONT")
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val v1 = recognizeSTMTS()
            val v2 = recognizeRENDERCONT()

            println("RENDERCONT RETURN: "+(v1 && v2))
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON,
                Symbol.CIRCLE, Symbol.CIRCLELINE)) {
            val v1 = recognizeSPECS()
            val v2 = recognizeRENDERCONT()

            println("RENDERCONT RETURN: "+(v1 && v2))
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA,
                Symbol.PATH, Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)) {
            val v1 = recognizeINFRASTRUCTURE()
            val v2 = recognizeRENDERCONT()

            println("RENDERCONT RETURN: "+(v1 && v2))
            return v1 && v2
        } else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            // EPSILON case
            println("RENDERCONT RETURN: "+true)
            return true
        }
        else{
            println("RENDERCONT RETURN: "+false)
            return false
        }
    }


    //INH-EFFECT
    fun recognizeEFFECT(): Effect {
        println("Recognizing EFFECT")
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val v1 = recognizeSTMTS()
            val v2 = recognizeEFFECT()

            println("EFFECT RETURN: "+(v1 && v2))
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf(Symbol.SET_LOCATION, Symbol.TRANSLATE, Symbol.ROTATE,
                Symbol.SET_MARKER)) {
            val v1 = recognizeCOMMANDS()
            val v2 = recognizeEFFECT()

            println("EFFECT RETURN: "+(v1 && v2))
            return v1 && v2
        } else if(currentSymbol!!.symbol in setOf(Symbol.RCURLY)){
            // EPSILON case
            println("EFFECT RETURN: "+true)
            return true
        }
        else{
            println("EFFECT RETURN: "+false)
            return false
        }
    }

    //ING-COMMANDS
    fun recognizeCOMMANDS(): Commands {
        println("Recognizing COMMANDS")
        if (currentSymbol!!.symbol in setOf(Symbol.SET_LOCATION)) {
            val v1 = recognizeTerminal(Symbol.SET_LOCATION)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeEXP()
            val v4 = recognizeTerminal(Symbol.RPAREN)
            val v5 = recognizeTerminal(Symbol.SEMICOL)

            println("COMMANDS RETURN: "+(v1 && v2 && v3 && v4 && v5))
            return v1 && v2 && v3 && v4 && v5
        } else if (currentSymbol!!.symbol in setOf(Symbol.TRANSLATE)) {
            val v1 = recognizeTerminal(Symbol.TRANSLATE)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeEXP()
            val v4 = recognizeTerminal(Symbol.COMMA)
            val v5 = recognizeEXP()
            val v6 = recognizeTerminal(Symbol.RPAREN)
            val v7 = recognizeTerminal(Symbol.SEMICOL)

            println("COMMANDS RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6 && v7))
            return v1 && v2 && v3 && v4 && v5 && v6 && v7
        } else if (currentSymbol!!.symbol in setOf(Symbol.ROTATE)) {
            val v1 = recognizeTerminal(Symbol.ROTATE)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeEXP()
            val v4 = recognizeTerminal(Symbol.RPAREN)
            val v5 = recognizeTerminal(Symbol.SEMICOL)

            println("COMMANDS RETURN: "+(v1 && v2 && v3 && v4 && v5))
            return v1 && v2 && v3 && v4 && v5
        } else if (currentSymbol!!.symbol in setOf(Symbol.SET_MARKER)) {
            val v1 = recognizeTerminal(Symbol.SET_MARKER)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeEXP()
            val v4 = recognizeTerminal(Symbol.RPAREN)
            val v5 = recognizeTerminal(Symbol.SEMICOL)

            println("COMMANDS RETURN: "+(v1 && v2 && v3 && v4 && v5))
            return v1 && v2 && v3 && v4 && v5
        } else {

            println("COMMANDS RETURN: "+false)
            return false
        }
    }

    //INH-SPECS
    fun recognizeSPECS(): Specs {
        println("Recognizing SPECS")
        if (currentSymbol!!.symbol in setOf(Symbol.BOX)) {
            val v1 = recognizeTerminal(Symbol.BOX)
            val v2 = recognizeREF()
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

            println("SPECS RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12))
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12
        } else if (currentSymbol!!.symbol in setOf(Symbol.LINE)) {
            val v1 = recognizeTerminal(Symbol.LINE)
            val v2 = recognizeREF()
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

            println("SPECS RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12 && v13 && v14 && v15 && v16))
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12 && v13 && v14 && v15 && v16
        } else if (currentSymbol!!.symbol in setOf( Symbol.POLYGON)) {
            val v1 = recognizeTerminal(Symbol.POLYGON)
            val v2 = recognizeREF()
            val v3 = recognizeTerminal(Symbol.COLON)
            val v4 = recognizeTAG()
            val v5 = recognizeTerminal(Symbol.LPAREN)
            val v6 = recognizePOLYARGS()
            val v7 = recognizeTerminal(Symbol.RPAREN)
            val v8 = recognizeTerminal(Symbol.LCURLY)
            val v9 = recognizeEFFECT()
            val v10 = recognizeTerminal(Symbol.RCURLY)

            println("SPECS RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10))
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10
        } else if (currentSymbol!!.symbol in setOf( Symbol.CIRCLE)) {
            val v1 = recognizeTerminal(Symbol.CIRCLE)
            val v2 = recognizeREF()
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

            println("SPECS RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12))
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12
        } else if (currentSymbol!!.symbol in setOf( Symbol.CIRCLELINE)) {
            val v1 = recognizeTerminal(Symbol.CIRCLELINE)
            val v2 = recognizeREF()
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

            println("SPECS RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12 && v13 && v14))
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12 && v13 && v14
        } else {

            println("SPECS RETURN: "+false)
            return false
        }
    }


    //INH-POLYARGS
    fun recognizePOLYARGS(): Polyargs {
        println("Recognizing POLYARGS")
        val v1 = recognizeEXP()
        val v2 = recognizeTerminal(Symbol.COMMA)
        val v3 = recognizeEXP()
        val v4 = recognizeTerminal(Symbol.COMMA)
        val v5 = recognizeEXP()
        val v6 = recognizePOLYARGS2()

        println("POLYARGS RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6))
        return v1 && v2 && v3 && v4 && v5 && v6
    }


    //INH-POLYARGS
    fun recognizePOLYARGS2(): Polyargs {
        println("Recognizing POLYARGS2")
        if (currentSymbol!!.symbol in setOf( Symbol.COMMA)) {
            val v1 = recognizeTerminal(Symbol.COMMA)
            val v2 = recognizeEXP()
            val v3 = recognizePOLYARGS2()

            println("POLYARGS2 RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        } else if(currentSymbol!!.symbol in setOf( Symbol.RPAREN)){

            println("POLYARGS2 RETURN: "+true)
            return true // EPSILON case
        }
        else{

            println("POLYARGS2 RETURN: "+false)
            return false
        }
    }


    //INH-STMTS
    fun recognizeSTMTS(): Stmts {
        println("Recognizing STMTS")
        if (currentSymbol!!.symbol in setOf( Symbol.VAR)) {
            val v1 = recognizeTerminal(Symbol.VAR)
            val v2 = recognizeTerminal(Symbol.VARIABLE)
            val v3 = recognizeTerminal(Symbol.EQUALS)
            val v4 = recognizeDATA()
            val v5 = recognizeTerminal(Symbol.SEMICOL)

            println("STMTS RETURN: "+(v1 && v2 && v3 && v4 && v5))
            return v1 && v2 && v3 && v4 && v5
        } else if (currentSymbol!!.symbol in setOf( Symbol.VARIABLE)) {
            val v1 = recognizeTerminal(Symbol.VARIABLE)
            val v2 = recognizeASSIGNS()
            val v3 = recognizeTerminal(Symbol.SEMICOL)

            println("STMTS RETURN: "+(v1 && v2 && v3))
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

            println("STMTS RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12))
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9 && v10 && v11 && v12
        } else if (currentSymbol!!.symbol in setOf( Symbol.PRINT)) {
            val v1 = recognizeTerminal(Symbol.PRINT)
            val v2 = recognizeTerminal(Symbol.LPAREN)
            val v3 = recognizeEXP()
            val v4 = recognizeTerminal(Symbol.RPAREN)
            val v5 = recognizeTerminal(Symbol.SEMICOL)

            println("STMTS RETURN: "+(v1 && v2 && v3 && v4 && v5))
            return v1 && v2 && v3 && v4 && v5
        } else if (currentSymbol!!.symbol in setOf( Symbol.CALL)) {
            val v1 = recognizeTerminal(Symbol.CALL)
            val v2 = recognizeTerminal(Symbol.VARIABLE)
            val v3 = recognizeTerminal(Symbol.LPAREN)
            val v4 = recognizeARGUMENTS()
            val v5 = recognizeTerminal(Symbol.RPAREN)
            val v6 = recognizeTerminal(Symbol.SEMICOL)

            println("STMTS RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6))
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

            println("STMTS RETURN: "+(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9))
            return v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8 && v9
        } else {

            println("STMTS RETURN: "+false)
            return false
        }
    }


    //INH-STMNTS
    fun recognizeASSIGNS(): Stmts {
        println("Recognizing ASSIGNS")
        if (currentSymbol!!.symbol in setOf( Symbol.LSQURE)) {
            val v1 = recognizeTerminal(Symbol.LSQURE)
            val v2 = recognizeEXP()
            val v3 = recognizeTerminal(Symbol.RSQURE)
            val v4 = recognizeTerminal(Symbol.EQUALS)
            val v5 = recognizeEXP()

            println("ASSIGNS RETURN: "+(v1 && v2 && v3 && v4 && v5))
            return v1 && v2 && v3 && v4 && v5
        } else if (currentSymbol!!.symbol in setOf( Symbol.EQUALS)) {
            val v1 = recognizeTerminal(Symbol.EQUALS)
            val v2 = recognizeDATA()

            println("ASSIGNS RETURN: "+(v1 && v2))
            return v1 && v2
        } else {

            println("ASSIGNS RETURN: "+false)
            return false
        }
    }


    //INH-CONSTRUCTNAMES
    fun recognizeCONSTRUCTNAMES(): Constructnames {
        println("Recognizing CONSTRUCTNAMES")
        if (currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA,
                Symbol.PATH, Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)) {

            var v1 = recognizeINFNAMES()
            println("CONSTRUCTNAMES RETURN: "+v1)
            return v1
        } else if (currentSymbol!!.symbol in setOf(Symbol.BUILDING_COMPLEX, Symbol.PARK)) {
            var v1 = recognizeCONTNAMES()
            println("CONSTRUCTNAMES RETURN: "+v1)
            return v1

        } else {
            println("CONSTRUCTNAMES RETURN: "+false)
            return false
        }
    }

    //INH-EXP
    fun recognizeEXP(): Exp {
        println("Recognizing EXP")
        val v1 = recognizeADDITIVE()

        println("EXP RETURN: "+v1)
        return v1
    }


    //INH-EXP
    fun recognizeADDITIVE(): Exp {
        println("Recognizing ADDITIVE")
        val v1 = recognizeMULTIPLICATIVE()
        val v2 = recognizeADDITIVE2()

        println("ADDITIVE RETURN: "+(v1 && v2))
        return v1 && v2
    }


    //INH EXP
    fun recognizeADDITIVE2(): Exp {
        println("Recognizing ADDITIVE2")
        if (currentSymbol!!.symbol in setOf( Symbol.PLUS)) {
            val v1 = recognizeTerminal(Symbol.PLUS)
            val v2 = recognizeMULTIPLICATIVE()
            val v3 = recognizeADDITIVE2()

            println("ADDITIVE2 RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        } else if (currentSymbol!!.symbol in setOf( Symbol.MINUS)) {
            val v1 = recognizeTerminal(Symbol.MINUS)
            val v2 = recognizeMULTIPLICATIVE()
            val v3 = recognizeADDITIVE2()

            println("ADDITIVE2 RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        } else if(currentSymbol!!.symbol in setOf(Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE)){

            println("ADDITIVE2 RETURN: "+true)
            return true // EPSILON case
        }
        else{

            println("ADDITIVE2 RETURN: "+false)
            return false
        }
    }


    //INH-EXP
    fun recognizeMULTIPLICATIVE(): Exp {
        println("Recognizing MULTIPLICATIVE")
        val v1 = recognizeEXPONENTIAL()
        val v2 = recognizeMULTIPLICATIVE2()

        println("MULTIPLICATIVE RETURN: "+(v1 && v2))
        return v1 && v2
    }


    //INH-EXP
    fun recognizeMULTIPLICATIVE2(): Exp {
        println("Recognizing MULTIPLICATIVE2")
        if (currentSymbol!!.symbol in setOf(Symbol.TIMES)) {
            val v1 = recognizeTerminal(Symbol.TIMES)
            val v2 = recognizeEXPONENTIAL()
            val v3 = recognizeMULTIPLICATIVE2()

            println("MULTIPLICATIVE2 RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        } else if (currentSymbol!!.symbol in setOf( Symbol.DIVIDE)) {
            val v1 = recognizeTerminal(Symbol.DIVIDE)
            val v2 = recognizeEXPONENTIAL()
            val v3 = recognizeMULTIPLICATIVE2()

            println("MULTIPLICATIVE2 RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        } else if (currentSymbol!!.symbol in setOf( Symbol.INTEGER_DIVIDE)) {
            val v1 = recognizeTerminal(Symbol.INTEGER_DIVIDE)
            val v2 = recognizeEXPONENTIAL()
            val v3 = recognizeMULTIPLICATIVE2()

            println("MULTIPLICATIVE2 RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        } else if( currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE)) {

            println("MULTIPLICATIVE2 RETURN: "+true)
            return true // EPSILON case
        }
        else{

            println("MULTIPLICATIVE2 RETURN: "+false)
            return false
        }
    }


    //INH-EXP
    fun recognizeEXPONENTIAL(): Exp {
        println("Recognizing EXPONENTIAL")
        val v1 = recognizeUNARY()
        val v2 = recognizeEXPONENTIAL2()

        println("EXPONENTIAL RETURN: "+(v1 && v2))
        return v1 && v2
    }


    //INH-EXP
    fun recognizeEXPONENTIAL2(): Exp {
        println("Recognizing EXPONENTIAL2")
        if (currentSymbol!!.symbol in setOf( Symbol.POW)) {
            val v1 = recognizeTerminal(Symbol.POW)
            val v2 = recognizeUNARY()
            val v3 = recognizeEXPONENTIAL2()

            println("EXPONENTIAL2 RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        } else if(currentSymbol!!.symbol in setOf(Symbol.TIMES, Symbol.DIVIDE, Symbol.INTEGER_DIVIDE, Symbol.PLUS, Symbol.MINUS,
                Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE)){

            println("EXPONENTIAL2 RETURN: "+true)
            return true // EPSILON case
        }
        else{

            println("EXPONENTIAL2 RETURN: "+false)
            return false
        }
    }


    //INH-EXP
    fun recognizeUNARY(): Exp {
        println("Unary: "+currentSymbol!!.symbol + " "+currentSymbol!!.lexeme)
        if (currentSymbol!!.symbol in setOf( Symbol.PLUS)) {
            val v1 = recognizeTerminal(Symbol.PLUS)
            val v2 = recognizePRIMARY()

            println("UNARY RETURN: "+(v1 && v2))
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf( Symbol.MINUS)) {
            val v1 = recognizeTerminal(Symbol.MINUS)
            val v2 = recognizePRIMARY()

            println("UNARY RETURN: "+(v1 && v2))
            return v1 && v2
        } else if(currentSymbol!!.symbol in setOf( Symbol.REAL, Symbol.VARIABLE, Symbol.LPAREN, Symbol.STRING)){
            val v1 = recognizePRIMARY()

            println("UNARY RETURN: "+v1)
            return v1
        }
        else{

            println("UNARY RETURN: "+false)
            return false
        }
    }


    //INH-EXP
    fun recognizePRIMARY(): Exp {
        println("Recognizing PRIMARY")
        if (currentSymbol!!.symbol in setOf( Symbol.REAL)) {
            var v1 = recognizeTerminal(Symbol.REAL)

            println("PRIMARY RETURN: "+v1)
            return v1
        } else if (currentSymbol!!.symbol in setOf( Symbol.VARIABLE)) {
            var v1 = recognizeTerminal(Symbol.VARIABLE)
            val v2 = recognizePRIMARY1()

            println("PRIMARY RETURN: "+(v1 && v2))
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf( Symbol.LPAREN)) {
            val v1 = recognizeTerminal(Symbol.LPAREN)
            val v2 = recognizeEXP()
            val v3 = recognizePRIMARY2()

            println("PRIMARY RETURN: "+(v1 && v2 && v3))
            return v1 && v2
        } else if (currentSymbol!!.symbol in setOf( Symbol.STRING)) {
            val v1 = recognizeTerminal(Symbol.STRING)

            println("PRIMARY RETURN: "+v1)
            return v1
        }
        else{

            println("PRIMARY RETURN: "+false)
            return false
        }
    }


    //INH-EXP
    fun recognizePRIMARY1(): Exp {
        println("Recognizing PRIMARY1")
        if (currentSymbol!!.symbol in setOf( Symbol.LSQURE)) {
            val v1 = recognizeTerminal(Symbol.LSQURE)
            val v2 = recognizeEXP()
            val v3 = recognizeTerminal(Symbol.RSQURE)

            println("PRIMARY1 RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        } else if(currentSymbol!!.symbol in setOf(Symbol.POW, Symbol.TIMES, Symbol.DIVIDE, Symbol.INTEGER_DIVIDE, Symbol.PLUS, Symbol.MINUS, Symbol.COMMA,
                Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE)){

            println("PRIMARY1 RETURN: "+true)
            return true // EPSILON case
        }
        else{

            println("PRIMARY1 RETURN: "+false)
            return false
        }
    }

    //INH-EXP
    fun recognizePRIMARY2(): Exp {
        println("Recognizing PRIMARY2")
        if (currentSymbol!!.symbol in setOf( Symbol.RPAREN)) {
            val v1 = recognizeTerminal(Symbol.RPAREN)

            println("PRIMARY2 RETURN: "+v1)
            return v1
        }
        else if(currentSymbol!!.symbol in setOf( Symbol.COMMA)){
            val v1 = recognizeTerminal(Symbol.COMMA)
            val v2 = recognizeEXP()
            val v3 = recognizeTerminal(Symbol.RPAREN)

            println("PRIMARY2 RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        }
        else{

            println("PRIMARY2 RETURN: "+false)
            return false
        }
    }

    //INH-DATA
    fun recognizeDATA(): Data {
        println("Recognizing DATA")
        if (currentSymbol!!.symbol in setOf(Symbol.LSQURE)) {
            var v1 = recognizeLIST()

            println("DATA RETURN: "+v1)
            return v1
        } else if(currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS,
                Symbol.REAL, Symbol.VARIABLE, Symbol.LPAREN, Symbol.STRING)){
            var v1 = recognizeEXP()

            println("DATA RETURN: "+v1)
            return v1
        }
        else{

            println("DATA RETURN: "+false)
            return false
        }
    }

    //INH-DATA
    fun recognizeLIST(): Data {
        println("Recognizing LIST")
        val v1 = recognizeTerminal(Symbol.LSQURE)
        val v2 = recognizeLISTITEM()
        val v3 = recognizeTerminal(Symbol.RSQURE)

        println("LIST RETURN: "+(v1 && v2 && v3))
        return v1 && v2 && v3
    }


    //INH-LISTITEMS
    fun recognizeLISTITEM(): Listitems {
        println("Recognizing LISTITEM")
        if (currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.REAL, Symbol.VARIABLE,
                Symbol.LPAREN, Symbol.STRING)) {
            val v1 = recognizeEXP()
            val v2 = recognizeLISTITEM2()

            println("LISTITEM RETURN: "+(v1 && v2))
            return v1 && v2
        } else if(currentSymbol!!.symbol in setOf(Symbol.RSQURE)){

            println("LISTITEM RETURN: "+true)
            return true
        }
        else{

            println("LISTITEM RETURN: "+false)
            return false
        }
    }


    //INH-LISTITEMS
    fun recognizeLISTITEM2(): Listitems {
        println("Recognizing LISTITEM2")
        if (currentSymbol!!.symbol in setOf( Symbol.COMMA)) {
            val v1 = recognizeTerminal(Symbol.COMMA)
            val v2 = recognizeEXP()
            val v3 = recognizeLISTITEM2()

            println("LISTITEM2 RETURN: "+(v1 && v2 && v3))
            return v1 && v2 && v3
        } else if( currentSymbol!!.symbol in setOf(Symbol.RSQURE)){

            println("LISTITEM2 RETURN: "+true)
            return true
        }
        else{

            println("LISTITEM2 RETURN: "+false)
            return false
        }
    }


}