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
    fun parsePROGRAM(): Program {
        println("Parsing PROGRAM")

        var predef = parsePREDEF()
        var city = parseCITY()

        var v1 = Start(predef, city)
        println("PROGRAM RETURN: "+(v1))
        return v1

    }


    //INH-PREDEF
    fun parsePREDEF(): Predef {
        println("Parsing PREDEF")
        if(currentSymbol!!.symbol in setOf(Symbol.PROCEDURE)){

            var procedure = parsePROCEDURE()
            var components = parsePREDEF()

            var v1 = SeqPredef(procedure, components)

            println("PREDEF RETURN: "+ (v1))
            return v1
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.SCHEMA)){
            var schemas = parseSCHEMAS()
            var predef = parsePREDEF()

            var v1 = SeqPredef(schemas, predef)

            println("PREDEF RETURN: "+(v1))
            return v1
        }
        else if (currentSymbol!!.symbol in setOf(Symbol.CITY)){
            //epsilon
            var v1 = EndPredef()
            println("PREDEF RETURN: "+v1)
            return v1
        }
        else{

            println("PREDEF RETURN: panic")
            return panic()
        }
    }

    //INH-PREDEF
    fun parseSCHEMAS(): Predef {
        println("Parsing SCHEMAS")
        var t1 = parseTerminal(Symbol.SCHEMA)
        var infra = parseINFRASTRUCTURE()

        if (infra !is Infrastructure) {
            throw IllegalArgumentException("Expected Infrastructure object, but got ${infra::class.simpleName}")
        }

        var v1 = Schema(infra)

        println("SCHEMAS RETURN: "+(v1))
        return v1
    }


    //INH-PREDEF
    fun parsePROCEDURE(): Predef {
        println( "Recognizing PROCEDURE")
        var v1 = parseTerminal(Symbol.PROCEDURE)
        var v2 = parseTerminal(Symbol.VARIABLE)
        var v3 = parseTerminal(Symbol.LPAREN)
        var arguments = parseARGUMENTS()
        var v5 = parseTerminal(Symbol.RPAREN)
        var v6 = parseTerminal(Symbol.LCURLY)
        var components = parseCOMPONENTS()
        var v8 = parseTerminal(Symbol.RCURLY)

        var res = Procedure(arguments, components)

        println("POCEDURE return:" + res)

        return res

    }

    //INH-ARGUMENTS
    fun parseARGUMENTS(): Arguments {
        println("Recognizing ARGUMENTS")

        if(currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.REAL, Symbol.VARIABLE, Symbol.LPAREN, Symbol.STRING)){
            var exp = parseEXP()
            var arguments = parseARGUMENTS2()

            var res = SeqArguments(ArgumentsExp(exp), arguments)

            println("ARGUMENTS RETURN: "+res)
            return res
        }
        else if (currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            //epsilon
            var res = EndArguments()
            println("ARGUMENTS RETURN: "+ res)
            return res
        }
        else{
            println("ARGUMENTS RETURN: " + "panic")
            return panic()
        }
    }

    //INH-ARGUMENTS
    fun parseARGUMENTS2(): Arguments {
        println("Recognizing ARGUMENTS2")

        if(currentSymbol!!.symbol in setOf(Symbol.COMMA)){
            var t1 = parseTerminal(Symbol.COMMA)
            var exp = parseEXP()
            var arguments = parseARGUMENTS2()

            var res = SeqArguments(ArgumentsExp(exp), arguments)

            println("ARGUMENTS2 RETURN: "+res)
            return res

        }
        else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            //epsilon
            var res = EndArguments()

            println("ARGUMENTS2 RETURN: "+ res)
            return res
        }
        else{
            println( "ARGUMENTS2 RETURN: " + "panic")
            return panic()
        }
    }


    //INH-CITY
    fun parseCITY(): City {
        println("Recognizing CITY")
        var t1 = parseTerminal(Symbol.CITY)
        var t2 = parseTerminal(Symbol.COLON)
        var t3 = parseTerminal(Symbol.STRING)
        var t4 = parseTerminal(Symbol.LPAREN)
        var components = parseCOMPONENTS()
        var t5 = parseTerminal(Symbol.RPAREN)

        var res = CityComponents(components)

        println("CITY RETURN: "+res)
        return res
    }

    //INH-COMPONENTS
    fun parseCOMPONENTS(): Components {
        println("Recognizing COMPONENTS")

        if(currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA, Symbol.PATH,
                Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)){
            var infra = parseINFRASTRUCTURE()
            var components = parseCOMPONENTS()

            var res = SeqComponents(infra, components)

            println("COMPONENTS RETURN: "+res)
            return res
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.BUILDING_COMPLEX, Symbol.PARK)){
            var containers = parseCONTAINERS()
            var components = parseCOMPONENTS()

            var res = SeqComponents(containers, components)

            println("COMPONENTS RETURN: "+res)
            return res

        }
        else if(currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)){
            var stmts = parseSTMTS()
            var components = parseCOMPONENTS()

            var res = SeqComponents(Statements(stmts), components)

            println("COMPONENTS RETURN: "+res)
            return res
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON, Symbol.CIRCLE, Symbol.CIRCLELINE)){
            var specs = parseSPECS()
            var components = parseCOMPONENTS()

            var res = SeqComponents(Specifications(specs), components)

            println("COMPONENTS RETURN: "+res)
            return res
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.RCURLY, Symbol.RPAREN)){
            //epsilon
            var res = EndComponents()

            println("COMPONENTS RETURN: "+res)
            return res
        }
        else{
            println("COMPONENTS RETURN: "+ "panic")
            return panic()
        }
    }

    //INH-COMPONENTS
    fun parseINFRASTRUCTURE(): Components {
        println("Recognizing INFRASTRUCTURE")
        var infnames = parseINFNAMES()
        var ref = parseREF()
        var t1 = parseTerminal(Symbol.COLON)
        var tag = parseTAG()
        var t2 = parseTerminal(Symbol.LPAREN)
        var render = parseRENDER()
        var t3 = parseTerminal(Symbol.RPAREN)
        var t4 = parseTerminal(Symbol.LCURLY)
        var effect = parseEFFECT()
        var t5 = parseTerminal(Symbol.RCURLY)

        var res = Infrastructure(infnames, ref, tag, render, effect)

        println("INFRASTRUCTURE RETURN: "+res)

        return res

    }

    //INH-INFNAMES
    fun parseINFNAMES(): Infnames {
        println("Recognizing INFNAMES")
        if (currentSymbol!!.symbol in setOf( Symbol.BUILDING)) {
            var t = parseTerminal(Symbol.BUILDING)

            var res = Building()
            println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.ROAD)) {
            var t = parseTerminal(Symbol.ROAD)

            var res = Road()
            println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.RAIL)) {
            var t = parseTerminal(Symbol.RAIL)

            var res = Rail()
            println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.AQUA)) {
            var t = parseTerminal(Symbol.AQUA)

            var res = Aqua()
            println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.PATH)) {
            var t = parseTerminal(Symbol.PATH)

            var res = Path()
            println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.SHOP_TUS)) {
            var t = parseTerminal(Symbol.SHOP_TUS)

            var res = ShopTus()
            println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.SHOP_MERCATOR)) {
            var t = parseTerminal(Symbol.SHOP_MERCATOR)

            var res = ShopMercator()
            println("INFNAMES RETURN: "+res)
            return res

        } else {
            println("INFNAMES RETURN: "+ "panic")
            return panic()
        }
    }


    //INH COMPONENTS
    fun parseCONTAINERS(): Components {
        println("Recognizing CONTAINERS")
        var contname = parseCONTNAMES()
        var ref = parseREF()
        var t1 = parseTerminal(Symbol.COLON)
        var tag = parseTAG()
        var t2 = parseTerminal(Symbol.LPAREN)
        var rendercont = parseRENDERCONT()
        var t3 = parseTerminal(Symbol.RPAREN)
        var t4 = parseTerminal(Symbol.LCURLY)
        var effect = parseEFFECT()
        var t5 = parseTerminal(Symbol.RCURLY)

        var res = Containers(contname, ref, tag, rendercont, effect)

        println("CONTAINERS RETURN: "+res)
        return res
    }


    //INH-CONTNAMES
    fun parseCONTNAMES(): Contnames {
        println("Parse CONTNAMES")
        if (currentSymbol!!.symbol in setOf( Symbol.BUILDING_COMPLEX)) {
            val t1 = parseTerminal(Symbol.BUILDING_COMPLEX)

            var res = BuildingComplex()
            println("CONTNAMES RETURN: "+res)
            return res
        } else if (currentSymbol!!.symbol in setOf( Symbol.PARK)) {
            val t1 = parseTerminal(Symbol.PARK)

            var res = Park()
            println("CONTNAMES RETURN: "+res)
            return res
        }
        else{
            println("CONTNAMES RETURN: "+ "panic")

            return panic()
        }
    }


    //INH-TAG
    fun parseTAG(): Tag {
        println("Recognizing TAG")
        if (currentSymbol!!.symbol in setOf( Symbol.LANGLE)) {
            val t1 = parseTerminal(Symbol.LANGLE)
            val exp = parseEXP()
            val t2 = parseTerminal(Symbol.RANGLE)

            var res = TagExp(exp)
            println("TAG RETURN: "+res)
            return res
        } else if( currentSymbol!!.symbol in setOf( Symbol.LPAREN)){
            // EPSILON case
            var res = EndTag()

            println("TAG RETURN: "+ res)
            return res
        }
        else{
            println("TAG RETURN: "+ "panic")
            return panic()
        }
    }

    //INH-REF
    fun parseREF(): Ref {
        println("Recognizing REF")
        if (currentSymbol!!.symbol in setOf( Symbol.LANGLE)) {
            val t1 = parseTerminal(Symbol.LANGLE)
            val exp = parseEXP()
            val t2 = parseTerminal(Symbol.RANGLE)

            var res = Reffrence(exp)
            println("REF RETURN: "+res)
            return res

        } else if( currentSymbol!!.symbol in setOf( Symbol.COLON)){
            // EPSILON case
            var res = EndRef()

            println("REF RETURN: "+ res)
            return res
        }
        else{
            println("REF RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-RENDER
    fun parseRENDER(): Render {
        println("Recognizing RENDER")
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val stmts = parseSTMTS()
            val render = parseRENDER()

            var res = SeqRender(RenderStmts(stmts), render)
            println("RENDER RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON,
                Symbol.CIRCLE, Symbol.CIRCLELINE)) {
            val specs = parseSPECS()
            val render = parseRENDER()

            var res = SeqRender(RenderSpecs(specs), render)
            println("RENDER RETURN: "+res)
            return res

        } else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            // EPSILON case

            var res = EndRender()
            println("RENDER RETURN: "+ res)
            return res
        }
        else{
            println("RENDER RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-RENDERCONT
    fun parseRENDERCONT(): Rendercont {
        println("Recognizing RENDERCONT")
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val stmts = parseSTMTS()
            val rendercont = parseRENDERCONT()

            var res = SeqRendercont(RenderContStmts(stmts), rendercont)
            println("RENDERCONT RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON,
                Symbol.CIRCLE, Symbol.CIRCLELINE)) {

            val specs = parseSPECS()
            val rendercont = parseRENDERCONT()

            var res = SeqRendercont(RenderContSpecs(specs), rendercont)
            println("RENDERCONT RETURN: "+res)

            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA,
                Symbol.PATH, Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)) {

            val infra = parseINFRASTRUCTURE()
            val rendercont = parseRENDERCONT()

            if(infra !is Infrastructure){
                throw IllegalArgumentException("Expected Infrastructure object, but got ${infra::class.simpleName}")
            }
            var res = SeqRendercont(RenderContInfra(infra), rendercont)
            println("RENDERCONT RETURN: "+res)

            return res

        } else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            // EPSILON case
            var res = EndRendercont()
            println("RENDERCONT RETURN: "+res)
            return res
        }
        else{
            println("RENDERCONT RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-EFFECT
    fun parseEFFECT(): Effect {
        println("Recognizing EFFECT")
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val stmts = parseSTMTS()
            val effect = parseEFFECT()

            var res = SeqEffect(EffectSmts(stmts), effect)
            println("EFFECT RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.SET_LOCATION, Symbol.TRANSLATE, Symbol.ROTATE,
                Symbol.SET_MARKER)) {
            val commands = parseCOMMANDS()
            val effect = parseEFFECT()

            var res = SeqEffect(EffectCommands(commands), effect)
            println("EFFECT RETURN: "+res)
            return res

        } else if(currentSymbol!!.symbol in setOf(Symbol.RCURLY)){
            // EPSILON case
            var res = EndEffect()
            println("EFFECT RETURN: "+res)
            return res
        }
        else{
            println("EFFECT RETURN: "+ "panic")
            return panic()
        }
    }

    //ING-COMMANDS
    fun parseCOMMANDS(): Commands {
        println("Recognizing COMMANDS")
        if (currentSymbol!!.symbol in setOf(Symbol.SET_LOCATION)) {
            val t1 = parseTerminal(Symbol.SET_LOCATION)
            val t2 = parseTerminal(Symbol.LPAREN)
            val exp = parseEXP()
            val t3 = parseTerminal(Symbol.RPAREN)
            val t4 = parseTerminal(Symbol.SEMICOL)

            var res = SetLocation(exp)
            println("COMMANDS RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.TRANSLATE)) {
            val t1 = parseTerminal(Symbol.TRANSLATE)
            val t2 = parseTerminal(Symbol.LPAREN)
            val exp1 = parseEXP()
            val t3 = parseTerminal(Symbol.COMMA)
            val exp2 = parseEXP()
            val t4 = parseTerminal(Symbol.RPAREN)
            val t5 = parseTerminal(Symbol.SEMICOL)

            var res = Translate(exp1, exp2)
            println("COMMANDS RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.ROTATE)) {
            val t1 = parseTerminal(Symbol.ROTATE)
            val t2 = parseTerminal(Symbol.LPAREN)
            val exp = parseEXP()
            val t3 = parseTerminal(Symbol.RPAREN)
            val t4 = parseTerminal(Symbol.SEMICOL)

            var res = Rotate(exp)
            println("COMMANDS RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.SET_MARKER)) {
            val t1 = parseTerminal(Symbol.SET_MARKER)
            val t2 = parseTerminal(Symbol.LPAREN)
            val exp = parseEXP()
            val t3 = parseTerminal(Symbol.RPAREN)
            val t4 = parseTerminal(Symbol.SEMICOL)

            var res = SetMarker(exp)
            println("COMMANDS RETURN: "+res)
            return res

        } else {

            println("COMMANDS RETURN: "+ "panic")
            return panic()
        }
    }

    //INH-SPECS
    fun parseSPECS(): Specs {
        println("Recognizing SPECS")
        if (currentSymbol!!.symbol in setOf(Symbol.BOX)) {
            val t1 = parseTerminal(Symbol.BOX)
            val ref = parseREF()
            val t2 = parseTerminal(Symbol.COLON)
            val tag = parseTAG()
            val t3 = parseTerminal(Symbol.LPAREN)
            val exp1 = parseEXP()
            val t4 = parseTerminal(Symbol.COMMA)
            val exp2 = parseEXP()
            val t5 = parseTerminal(Symbol.RPAREN)
            val t6 = parseTerminal(Symbol.LCURLY)
            val effect = parseEFFECT()
            val t7 = parseTerminal(Symbol.RCURLY)

            var res = Box(ref, tag, exp1, exp2, effect)
            println("SPECS RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.LINE)) {
            val t1 = parseTerminal(Symbol.LINE)
            val ref = parseREF()
            val t2 = parseTerminal(Symbol.COLON)
            val tag = parseTAG()
            val t3 = parseTerminal(Symbol.LPAREN)
            val exp1 = parseEXP()
            val t4 = parseTerminal(Symbol.COMMA)
            val exp2 = parseEXP()
            val t5 = parseTerminal(Symbol.COMMA)
            val exp3 = parseEXP()
            val t6 = parseTerminal(Symbol.COMMA)
            val exp4 = parseEXP()
            val t7 = parseTerminal(Symbol.RPAREN)
            val t8 = parseTerminal(Symbol.LCURLY)
            val effect = parseEFFECT()
            val t9 = parseTerminal(Symbol.RCURLY)

            var res = Line(ref, tag, exp1, exp2, exp3, exp4, effect)
            println("SPECS RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.POLYGON)) {
            val t1 = parseTerminal(Symbol.POLYGON)
            val ref = parseREF()
            val t2 = parseTerminal(Symbol.COLON)
            val tag = parseTAG()
            val t3 = parseTerminal(Symbol.LPAREN)
            val polyargs = parsePOLYARGS()
            val t4 = parseTerminal(Symbol.RPAREN)
            val t5 = parseTerminal(Symbol.LCURLY)
            val effect = parseEFFECT()
            val t6 = parseTerminal(Symbol.RCURLY)

            var res = Polygon(ref, tag, polyargs, effect)
            println("SPECS RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.CIRCLE)) {
            val t1 = parseTerminal(Symbol.CIRCLE)
            val ref = parseREF()
            val t2 = parseTerminal(Symbol.COLON)
            val tag = parseTAG()
            val t3 = parseTerminal(Symbol.LPAREN)
            val exp1 = parseEXP()
            val t4 = parseTerminal(Symbol.COMMA)
            val exp2 = parseEXP()
            val t5 = parseTerminal(Symbol.RPAREN)
            val t6 = parseTerminal(Symbol.LCURLY)
            val effect = parseEFFECT()
            val t7 = parseTerminal(Symbol.RCURLY)

            var res = Circle(ref, tag, exp1, exp2, effect)
            println("SPECS RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.CIRCLELINE)) {
            val t1 = parseTerminal(Symbol.CIRCLELINE)
            val ref = parseREF()
            val t2 = parseTerminal(Symbol.COLON)
            val tag = parseTAG()
            val t3 = parseTerminal(Symbol.LPAREN)
            val exp1 = parseEXP()
            val t4 = parseTerminal(Symbol.COMMA)
            val exp2 = parseEXP()
            val t5 = parseTerminal(Symbol.COMMA)
            val exp3 = parseEXP()
            val t6 = parseTerminal(Symbol.RPAREN)
            val t7 = parseTerminal(Symbol.LCURLY)
            val effect = parseEFFECT()
            val t8 = parseTerminal(Symbol.RCURLY)

            var res = CircleLine(ref, tag, exp1, exp2, exp3, effect)
            println("SPECS RETURN: "+res)
            return res

        } else {

            println("SPECS RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-POLYARGS
    fun parsePOLYARGS(): Polyargs {
        println("Recognizing POLYARGS")
        val exp1 = parseEXP()
        val t1 = parseTerminal(Symbol.COMMA)
        val exp2 = parseEXP()
        val t2 = parseTerminal(Symbol.COMMA)
        val exp3 = parseEXP()
        val polyargsExtra = parsePOLYARGS2()

        var res = SeqPolyargs(PolyargsExp(exp1), SeqPolyargs(PolyargsExp(exp2), SeqPolyargs(PolyargsExp(exp3), polyargsExtra)))
        println("POLYARGS RETURN: "+res)
        return res
    }


    //INH-POLYARGS
    fun parsePOLYARGS2(): Polyargs {
        println("Recognizing POLYARGS2")
        if (currentSymbol!!.symbol in setOf( Symbol.COMMA)) {
            val t1 = parseTerminal(Symbol.COMMA)
            val exp = parseEXP()
            val polyargsExtra = parsePOLYARGS2()

            var res = SeqPolyargs(PolyargsExp(exp), polyargsExtra)
            println("POLYARGS2 RETURN: "+res)
            return res

        } else if(currentSymbol!!.symbol in setOf( Symbol.RPAREN)){

            var res = EndPolyargs()
            println("POLYARGS2 RETURN: "+ res)
            return res // EPSILON case
        }
        else{
            println("POLYARGS2 RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-STMTS
    fun parseSTMTS(): Stmts {
        println("Recognizing STMTS")
        if (currentSymbol!!.symbol in setOf( Symbol.VAR)) {
            val t1 = parseTerminal(Symbol.VAR)
            val variable = parseTerminal(Symbol.VARIABLE)
            val t2 = parseTerminal(Symbol.EQUALS)
            val data = parseDATA()
            val t3 = parseTerminal(Symbol.SEMICOL)

            var res = Define(variable, data)
            println("STMTS RETURN: "+res)
            return res


        } else if (currentSymbol!!.symbol in setOf( Symbol.VARIABLE)) {
            val variable = parseTerminal(Symbol.VARIABLE)
            val assigns = parseASSIGNS(variable)
            val t1 = parseTerminal(Symbol.SEMICOL)

            var res = assigns
            println("STMTS RETURN: "+res)
            return res


        } else if (currentSymbol!!.symbol in setOf( Symbol.FOR)) {
            val t1 = parseTerminal(Symbol.FOR)
            val t2 = parseTerminal(Symbol.LPAREN)
            val t3 = parseTerminal(Symbol.VAR)
            val variable = parseTerminal(Symbol.VARIABLE)
            val t4 = parseTerminal(Symbol.EQUALS)
            val exp1 = parseEXP()
            val t5 = parseTerminal(Symbol.SEMICOL)
            val exp2 = parseEXP()
            val t6 = parseTerminal(Symbol.RPAREN)
            val t7 = parseTerminal(Symbol.LCURLY)
            val components = parseCOMPONENTS()
            val t8 = parseTerminal(Symbol.RCURLY)

            var res = Forloop(variable, exp1, exp2, components)

            println("STMTS RETURN: "+res)
            return res


        } else if (currentSymbol!!.symbol in setOf( Symbol.PRINT)) {
            val t1 = parseTerminal(Symbol.PRINT)
            val t2 = parseTerminal(Symbol.LPAREN)
            val exp = parseEXP()
            val t3 = parseTerminal(Symbol.RPAREN)
            val t4 = parseTerminal(Symbol.SEMICOL)

            var res = Print(exp)
            println("STMTS RETURN: "+res)

            return res


        } else if (currentSymbol!!.symbol in setOf( Symbol.CALL)) {
            val t1 = parseTerminal(Symbol.CALL)
            val variable = parseTerminal(Symbol.VARIABLE)
            val t2 = parseTerminal(Symbol.LPAREN)
            val arguments = parseARGUMENTS()
            val t3 = parseTerminal(Symbol.RPAREN)
            val t4 = parseTerminal(Symbol.SEMICOL)

            var res = Call(variable, arguments)
            println("STMTS RETURN: "+res)
            return res


        } else if (currentSymbol!!.symbol in setOf( Symbol.DISPLAY_MARKERS)) {
            val t1 = parseTerminal(Symbol.DISPLAY_MARKERS)
            val t2 = parseTerminal(Symbol.LPAREN)
            val exp1 = parseEXP()
            val t3 = parseTerminal(Symbol.COMMA)
            val exp2 = parseEXP()
            val t4 = parseTerminal(Symbol.COMMA)
            val constructnames = parseCONSTRUCTNAMES()
            val t5 = parseTerminal(Symbol.RPAREN)
            val t6 = parseTerminal(Symbol.SEMICOL)

            var res = DisplayMarkers(exp1, exp2, constructnames)
            println("STMTS RETURN: "+res)
            return res


        } else {

            println("STMTS RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-STMNTS
    fun parseASSIGNS(variable: String): Stmts {
        println("Recognizing ASSIGNS")
        if (currentSymbol!!.symbol in setOf( Symbol.LSQURE)) {
            val t1 = parseTerminal(Symbol.LSQURE)
            val exp1 = parseEXP()
            val t2 = parseTerminal(Symbol.RSQURE)
            val t3 = parseTerminal(Symbol.EQUALS)
            val exp2 = parseEXP()

            var res = ListItemAssign(variable, exp1, exp2)

            println("ASSIGNS RETURN: "+res)
            return res


        } else if (currentSymbol!!.symbol in setOf( Symbol.EQUALS)) {
            val t1 = parseTerminal(Symbol.EQUALS)
            val data = parseDATA()

            var res = Assign(variable, data)

            println("ASSIGNS RETURN: "+res)
            return res
        } else {

            println("ASSIGNS RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-CONSTRUCTNAMES
    fun parseCONSTRUCTNAMES(): Constructnames {
        println("Recognizing CONSTRUCTNAMES")
        if (currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA,
                Symbol.PATH, Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)) {

            var infnames = parseINFNAMES()

            var res = InfName(infnames)
            println("CONSTRUCTNAMES RETURN: "+res)
            return res


        } else if (currentSymbol!!.symbol in setOf(Symbol.BUILDING_COMPLEX, Symbol.PARK)) {
            var contNames = parseCONTNAMES()

            var res = ContName(contNames)
            println("CONSTRUCTNAMES RETURN: "+res)
            return res

        } else {
            println("CONSTRUCTNAMES RETURN: "+ "panic")
            return panic()
        }
    }

    //INH-EXP
    fun parseEXP(): Exp {
        println("Recognizing EXP")
        val v1 = recognizeADDITIVE()

        println("EXP RETURN: "+v1)
        return v1
    }


    //INH-EXP
    fun parseADDITIVE(): Exp {
        println("Recognizing ADDITIVE")
        val v1 = recognizeMULTIPLICATIVE()
        val v2 = recognizeADDITIVE2()

        println("ADDITIVE RETURN: "+(v1 && v2))
        return v1 && v2
    }


    //INH EXP
    fun parseADDITIVE2(): Exp {
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
    fun parseMULTIPLICATIVE(): Exp {
        println("Recognizing MULTIPLICATIVE")
        val v1 = recognizeEXPONENTIAL()
        val v2 = recognizeMULTIPLICATIVE2()

        println("MULTIPLICATIVE RETURN: "+(v1 && v2))
        return v1 && v2
    }


    //INH-EXP
    fun parseMULTIPLICATIVE2(): Exp {
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
    fun parseEXPONENTIAL(): Exp {
        println("Recognizing EXPONENTIAL")
        val v1 = recognizeUNARY()
        val v2 = recognizeEXPONENTIAL2()

        println("EXPONENTIAL RETURN: "+(v1 && v2))
        return v1 && v2
    }


    //INH-EXP
    fun parseEXPONENTIAL2(): Exp {
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
    fun parseUNARY(): Exp {
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
    fun parsePRIMARY(): Exp {
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
    fun parsePRIMARY1(): Exp {
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
    fun parsePRIMARY2(): Exp {
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
    fun parseDATA(): Data {
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
    fun parseLIST(): Data {
        println("Recognizing LIST")
        val v1 = recognizeTerminal(Symbol.LSQURE)
        val v2 = recognizeLISTITEM()
        val v3 = recognizeTerminal(Symbol.RSQURE)

        println("LIST RETURN: "+(v1 && v2 && v3))
        return v1 && v2 && v3
    }


    //INH-LISTITEMS
    fun parseLISTITEM(): Listitems {
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
    fun parseLISTITEM2(): Listitems {
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