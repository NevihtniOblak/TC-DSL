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
            //println("Recognized: $currentSymbol")
            val lexme = currentSymbol!!.lexeme
            currentSymbol = lexer.getToken()
            lexme
        } else {
            panic()
        }

    fun parseStart(): Program {
        currentSymbol = lexer.getToken()
        val result = parsePROGRAM();

        return when (currentSymbol?.symbol) {
            Symbol.EOF -> result
            else -> panic()
        }
    }



    //PARSING FUNKCIJE


    //INH-PROGRAM
    fun parsePROGRAM(): Program {
        //println("Parsing PROGRAM")

        var predef = parsePREDEF()
        var city = parseCITY()

        var v1 = Start(predef, city)
        //println("PROGRAM RETURN: "+(v1))
        return v1

    }


    //INH-PREDEF
    fun parsePREDEF(): Predef {
        //println("Parsing PREDEF")
        if(currentSymbol!!.symbol in setOf(Symbol.PROCEDURE)){

            var procedure = parsePROCEDURE()
            var components = parsePREDEF()

            var v1 = SeqPredef(procedure, components)

            //println("PREDEF RETURN: "+ (v1))
            return v1
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.SCHEMA)){
            var schemas = parseSCHEMAS()
            var predef = parsePREDEF()

            var v1 = SeqPredef(schemas, predef)

            //println("PREDEF RETURN: "+(v1))
            return v1
        }
        else if (currentSymbol!!.symbol in setOf(Symbol.CITY)){
            //epsilon
            var v1 = EndPredef()
            //println("PREDEF RETURN: "+v1)
            return v1
        }
        else{

            //println("PREDEF RETURN: panic")
            return panic()
        }
    }

    //INH-PREDEF
    fun parseSCHEMAS(): Predef {
        //println("Parsing SCHEMAS")
        var t1 = parseTerminal(Symbol.SCHEMA)
        var infra = parseINFRASTRUCTURE()

        if (infra !is Infrastructure) {
            throw IllegalArgumentException("Expected Infrastructure object, but got ${infra::class.simpleName}")
        }

        var v1 = Schema(infra)

        //println("SCHEMAS RETURN: "+(v1))
        return v1
    }


    //INH-PREDEF
    fun parsePROCEDURE(): Predef {
        //println( "Recognizing PROCEDURE")
        var v1 = parseTerminal(Symbol.PROCEDURE)
        var name = parseTerminal(Symbol.VARIABLE)
        var v3 = parseTerminal(Symbol.LPAREN)
        //var arguments = parseARGUMENTS()
        var parameters = parsePARAMETERS()
        //
        var v5 = parseTerminal(Symbol.RPAREN)
        var v6 = parseTerminal(Symbol.LCURLY)
        var components = parseCOMPONENTS()
        var v8 = parseTerminal(Symbol.RCURLY)

        var res = Procedure(name,parameters, components)

        //println("POCEDURE return:" + res)

        return res

    }

    //INH-PARAMETERS
    fun parsePARAMETERS(): Parameters {
        //println("Recognizing PARAMETERS")

        if(currentSymbol!!.symbol in setOf(Symbol.VARIABLE)){
            //var exp = parseEXP()
            var parameter = parseTerminal(Symbol.VARIABLE)
            //
            var nextParameter = parsePARAMETERS2()

            //var res = SeqArguments(ArgumentsExp(exp), arguments)
            var res = SeqParameters(Parameter(parameter), nextParameter)

            //println("PARAMETERS RETURN: "+res)
            return res
        }
        else if (currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            //epsilon
            var res = EndParameter()
            //println("PARAMETERS RETURN: "+ res)
            return res
        }
        else{
            //println("PARAMETERS RETURN: " + "panic")
            return panic()
        }
    }

    //INH-ARGUMENTS
    fun parsePARAMETERS2(): Parameters {
        //println("Recognizing PARAMETERS2")

        if(currentSymbol!!.symbol in setOf(Symbol.COMMA)){
            var t1 = parseTerminal(Symbol.COMMA)
            //var exp = parseEXP()
            var parameter = parseTerminal(Symbol.VARIABLE)
            //
            var nextParameter = parsePARAMETERS2()

            //var res = SeqArguments(ArgumentsExp(exp), arguments)
            var res = SeqParameters(Parameter(parameter), nextParameter)

            //println("ARGUMENTS2 RETURN: "+res)
            return res

        }
        else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            //epsilon
            var res = EndParameter()

            //println("PARAMETERS2 RETURN: "+ res)
            return res
        }
        else{
            //println( "PARAMETERS2 RETURN: " + "panic")
            return panic()
        }
    }


    //INH-ARGUMENTS
    fun parseARGUMENTS(): Arguments {
        //println("Recognizing ARGUMENTS")

        if(currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.NEGATE, Symbol.REAL, Symbol.VARIABLE, Symbol.LPAREN, Symbol.STRING, Symbol.TRUE, Symbol.FALSE
            )){
            var exp = parseEXP()
            var arguments = parseARGUMENTS2()

            //var res = SeqArguments(ArgumentsExp(exp), arguments)
            var res = SeqArguments(Argument(exp), arguments)

            //println("ARGUMENTS RETURN: "+res)
            return res
        }
        else if (currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            //epsilon
            var res = EndArgument()
            //println("ARGUMENTS RETURN: "+ res)
            return res
        }
        else{
            //println("ARGUMENTS RETURN: " + "panic")
            return panic()
        }
    }

    //INH-ARGUMENTS
    fun parseARGUMENTS2(): Arguments {
        //println("Recognizing ARGUMENTS2")

        if(currentSymbol!!.symbol in setOf(Symbol.COMMA)){
            var t1 = parseTerminal(Symbol.COMMA)
            var exp = parseEXP()

            var arguments = parseARGUMENTS2()

            var res = SeqArguments(Argument(exp), arguments)

            //println("ARGUMENTS2 RETURN: "+res)
            return res

        }
        else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            //epsilon
            var res = EndArgument()

            //println("ARGUMENTS2 RETURN: "+ res)
            return res
        }
        else{
            //println( "ARGUMENTS2 RETURN: " + "panic")
            return panic()
        }
    }


    //INH-CITY
    fun parseCITY(): City {
        //println("Recognizing CITY")
        var t1 = parseTerminal(Symbol.CITY)
        var t2 = parseTerminal(Symbol.COLON)
        var t3 = parseTerminal(Symbol.STRING)
        var t4 = parseTerminal(Symbol.LPAREN)
        var components = parseCOMPONENTS()
        var t5 = parseTerminal(Symbol.RPAREN)

        var res = CityComponents(components)

        //println("CITY RETURN: "+res)
        return res
    }

    //INH-COMPONENTS
    fun parseCOMPONENTS(): Components {
        //println("Recognizing COMPONENTS")

        if(currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA, Symbol.PATH,
                Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)){
            var infra = parseINFRASTRUCTURE()
            var components = parseCOMPONENTS()

            var res = SeqComponents(infra, components)

            //println("COMPONENTS RETURN: "+res)
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

            //println("COMPONENTS RETURN: "+res)
            return res
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON, Symbol.CIRCLE, Symbol.CIRCLELINE)){
            var specs = parseSPECS()
            var components = parseCOMPONENTS()

            var res = SeqComponents(Specifications(specs), components)

            //println("COMPONENTS RETURN: "+res)
            return res
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.RCURLY, Symbol.RPAREN)){
            //epsilon
            var res = EndComponents()

            //println("COMPONENTS RETURN: "+res)
            return res
        }
        else{
            //println("COMPONENTS RETURN: "+ "panic")
            return panic()
        }
    }

    //INH-COMPONENTS
    fun parseINFRASTRUCTURE(): Components {
        //println("Recognizing INFRASTRUCTURE")
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

        //println("INFRASTRUCTURE RETURN: "+res)

        return res

    }

    //INH-INFNAMES
    fun parseINFNAMES(): Infnames {
        //println("Recognizing INFNAMES")
        if (currentSymbol!!.symbol in setOf( Symbol.BUILDING)) {
            var t = parseTerminal(Symbol.BUILDING)

            var res = Building()
            //println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.ROAD)) {
            var t = parseTerminal(Symbol.ROAD)

            var res = Road()
            //println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.RAIL)) {
            var t = parseTerminal(Symbol.RAIL)

            var res = Rail()
            //println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.AQUA)) {
            var t = parseTerminal(Symbol.AQUA)

            var res = Aqua()
            //println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.PATH)) {
            var t = parseTerminal(Symbol.PATH)

            var res = Path()
            //println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.SHOP_TUS)) {
            var t = parseTerminal(Symbol.SHOP_TUS)

            var res = ShopTus()
            //println("INFNAMES RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.SHOP_MERCATOR)) {
            var t = parseTerminal(Symbol.SHOP_MERCATOR)

            var res = ShopMercator()
            //println("INFNAMES RETURN: "+res)
            return res

        } else {
            //println("INFNAMES RETURN: "+ "panic")
            return panic()
        }
    }


    //INH COMPONENTS
    fun parseCONTAINERS(): Components {
        //println("Recognizing CONTAINERS")
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

        //println("CONTAINERS RETURN: "+res)
        return res
    }


    //INH-CONTNAMES
    fun parseCONTNAMES(): Contnames {
        //println("Parse CONTNAMES")
        if (currentSymbol!!.symbol in setOf( Symbol.BUILDING_COMPLEX)) {
            val t1 = parseTerminal(Symbol.BUILDING_COMPLEX)

            var res = BuildingComplex()
            //println("CONTNAMES RETURN: "+res)
            return res
        } else if (currentSymbol!!.symbol in setOf( Symbol.PARK)) {
            val t1 = parseTerminal(Symbol.PARK)

            var res = Park()
            //println("CONTNAMES RETURN: "+res)
            return res
        }
        else{
            //println("CONTNAMES RETURN: "+ "panic")

            return panic()
        }
    }


    //INH-TAG
    fun parseTAG(): Tag {
        //println("Recognizing TAG")
        if (currentSymbol!!.symbol in setOf( Symbol.LANGLE)) {
            val t1 = parseTerminal(Symbol.LANGLE)
            val exp = parseEXP()
            val t2 = parseTerminal(Symbol.RANGLE)

            var res = TagExp(exp)
            //println("TAG RETURN: "+res)
            return res
        } else if( currentSymbol!!.symbol in setOf( Symbol.LPAREN)){
            // EPSILON case
            var res = EndTag()

            //println("TAG RETURN: "+ res)
            return res
        }
        else{
            //println("TAG RETURN: "+ "panic")
            return panic()
        }
    }

    //INH-REF
    fun parseREF(): Ref {
        //println("Recognizing REF")
        if (currentSymbol!!.symbol in setOf( Symbol.LANGLE)) {
            val t1 = parseTerminal(Symbol.LANGLE)
            val exp = parseEXP()
            val t2 = parseTerminal(Symbol.RANGLE)

            var res = Reffrence(exp)
            //println("REF RETURN: "+res)
            return res

        } else if( currentSymbol!!.symbol in setOf( Symbol.COLON)){
            // EPSILON case
            var res = EndRef()

            //println("REF RETURN: "+ res)
            return res
        }
        else{
            //println("REF RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-RENDER
    fun parseRENDER(): Render {
        //println("Recognizing RENDER")
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val stmts = parseSTMTS()
            val render = parseRENDER()

            var res = SeqRender(RenderStmts(stmts), render)
            //println("RENDER RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON,
                Symbol.CIRCLE, Symbol.CIRCLELINE)) {
            val specs = parseSPECS()
            val render = parseRENDER()

            var res = SeqRender(RenderSpecs(specs), render)
            //println("RENDER RETURN: "+res)
            return res

        } else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            // EPSILON case

            var res = EndRender()
            //println("RENDER RETURN: "+ res)
            return res
        }
        else{
            //println("RENDER RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-RENDERCONT
    fun parseRENDERCONT(): Rendercont {
        //println("Recognizing RENDERCONT")
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val stmts = parseSTMTS()
            val rendercont = parseRENDERCONT()

            var res = SeqRendercont(RenderContStmts(stmts), rendercont)
            //println("RENDERCONT RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.BOX, Symbol.LINE, Symbol.POLYGON,
                Symbol.CIRCLE, Symbol.CIRCLELINE)) {

            val specs = parseSPECS()
            val rendercont = parseRENDERCONT()

            var res = SeqRendercont(RenderContSpecs(specs), rendercont)
            //println("RENDERCONT RETURN: "+res)

            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA,
                Symbol.PATH, Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)) {

            val infra = parseINFRASTRUCTURE()
            val rendercont = parseRENDERCONT()

            if(infra !is Infrastructure){
                throw IllegalArgumentException("Expected Infrastructure object, but got ${infra::class.simpleName}")
            }
            var res = SeqRendercont(RenderContInfra(infra), rendercont)
            //println("RENDERCONT RETURN: "+res)

            return res

        } else if(currentSymbol!!.symbol in setOf(Symbol.RPAREN)){
            // EPSILON case
            var res = EndRendercont()
            //println("RENDERCONT RETURN: "+res)
            return res
        }
        else{
            //println("RENDERCONT RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-EFFECT
    fun parseEFFECT(): Effect {
        //println("Recognizing EFFECT")
        if (currentSymbol!!.symbol in setOf(Symbol.VAR, Symbol.VARIABLE, Symbol.FOR, Symbol.PRINT,
                Symbol.CALL, Symbol.DISPLAY_MARKERS)) {
            val stmts = parseSTMTS()
            val effect = parseEFFECT()

            var res = SeqEffect(EffectSmts(stmts), effect)
            //println("EFFECT RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.SET_LOCATION, Symbol.TRANSLATE, Symbol.ROTATE,
                Symbol.SET_MARKER)) {
            val commands = parseCOMMANDS()
            val effect = parseEFFECT()

            var res = SeqEffect(EffectCommands(commands), effect)
            //println("EFFECT RETURN: "+res)
            return res

        } else if(currentSymbol!!.symbol in setOf(Symbol.RCURLY)){
            // EPSILON case
            var res = EndEffect()
            //println("EFFECT RETURN: "+res)
            return res
        }
        else{
            //println("EFFECT RETURN: "+ "panic")
            return panic()
        }
    }

    //ING-COMMANDS
    fun parseCOMMANDS(): Commands {
        //println("Recognizing COMMANDS")
        if (currentSymbol!!.symbol in setOf(Symbol.SET_LOCATION)) {
            val t1 = parseTerminal(Symbol.SET_LOCATION)
            val t2 = parseTerminal(Symbol.LPAREN)
            val exp = parseEXP()
            val t3 = parseTerminal(Symbol.RPAREN)
            val t4 = parseTerminal(Symbol.SEMICOL)

            var res = SetLocation(exp)
            //println("COMMANDS RETURN: "+res)
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
            //println("COMMANDS RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.ROTATE)) {
            val t1 = parseTerminal(Symbol.ROTATE)
            val t2 = parseTerminal(Symbol.LPAREN)
            val exp = parseEXP()
            val t3 = parseTerminal(Symbol.RPAREN)
            val t4 = parseTerminal(Symbol.SEMICOL)

            var res = Rotate(exp)
            //println("COMMANDS RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf(Symbol.SET_MARKER)) {
            val t1 = parseTerminal(Symbol.SET_MARKER)
            val t2 = parseTerminal(Symbol.LPAREN)
            val exp = parseEXP()
            val t3 = parseTerminal(Symbol.RPAREN)
            val t4 = parseTerminal(Symbol.SEMICOL)

            var res = SetMarker(exp)
            //println("COMMANDS RETURN: "+res)
            return res

        } else {

            //println("COMMANDS RETURN: "+ "panic")
            return panic()
        }
    }

    //INH-SPECS
    fun parseSPECS(): Specs {
        //println("Recognizing SPECS")
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
            //println("SPECS RETURN: "+res)
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
            //println("SPECS RETURN: "+res)
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
            //println("SPECS RETURN: "+res)
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
            //println("SPECS RETURN: "+res)
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
            //println("SPECS RETURN: "+res)
            return res

        } else {

            //println("SPECS RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-POLYARGS
    fun parsePOLYARGS(): Polyargs {
        //println("Recognizing POLYARGS")
        val exp1 = parseEXP()
        val t1 = parseTerminal(Symbol.COMMA)
        val exp2 = parseEXP()
        val t2 = parseTerminal(Symbol.COMMA)
        val exp3 = parseEXP()
        val polyargsExtra = parsePOLYARGS2()

        var res = SeqPolyargs(PolyargsExp(exp1), SeqPolyargs(PolyargsExp(exp2), SeqPolyargs(PolyargsExp(exp3), polyargsExtra)))
        //println("POLYARGS RETURN: "+res)
        return res
    }


    //INH-POLYARGS
    fun parsePOLYARGS2(): Polyargs {
        //println("Recognizing POLYARGS2")
        if (currentSymbol!!.symbol in setOf( Symbol.COMMA)) {
            val t1 = parseTerminal(Symbol.COMMA)
            val exp = parseEXP()
            val polyargsExtra = parsePOLYARGS2()

            var res = SeqPolyargs(PolyargsExp(exp), polyargsExtra)
           // println("POLYARGS2 RETURN: "+res)
            return res

        } else if(currentSymbol!!.symbol in setOf( Symbol.RPAREN)){

            var res = EndPolyargs()
            //println("POLYARGS2 RETURN: "+ res)
            return res // EPSILON case
        }
        else{
            //println("POLYARGS2 RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-STMTS
    fun parseSTMTS(): Stmts {
        //println("Recognizing STMTS")
        if (currentSymbol!!.symbol in setOf( Symbol.VAR)) {
            val t1 = parseTerminal(Symbol.VAR)
            val variable = parseTerminal(Symbol.VARIABLE)
            val t2 = parseTerminal(Symbol.EQUALS)
            val data = parseDATA()
            val t3 = parseTerminal(Symbol.SEMICOL)

            var res = Define(variable, data)
            //println("STMTS RETURN: "+res)
            return res


        } else if (currentSymbol!!.symbol in setOf( Symbol.VARIABLE)) {
            val variable = parseTerminal(Symbol.VARIABLE)
            val assigns = parseASSIGNS(variable)
            val t1 = parseTerminal(Symbol.SEMICOL)

            var res = assigns
            //println("STMTS RETURN: "+res)
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

            //println("STMTS RETURN: "+res)
            return res


        } else if (currentSymbol!!.symbol in setOf( Symbol.PRINT)) {
            val t1 = parseTerminal(Symbol.PRINT)
            val t2 = parseTerminal(Symbol.LPAREN)
            val exp = parseEXP()
            val t3 = parseTerminal(Symbol.RPAREN)
            val t4 = parseTerminal(Symbol.SEMICOL)

            var res = Print(exp)
            //println("STMTS RETURN: "+res)

            return res


        } else if (currentSymbol!!.symbol in setOf( Symbol.CALL)) {
            val t1 = parseTerminal(Symbol.CALL)
            val variable = parseTerminal(Symbol.VARIABLE)
            val t2 = parseTerminal(Symbol.LPAREN)
            val arguments = parseARGUMENTS()
            val t3 = parseTerminal(Symbol.RPAREN)
            val t4 = parseTerminal(Symbol.SEMICOL)

            var res = Call(variable, arguments)
            //println("STMTS RETURN: "+res)
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
            //println("STMTS RETURN: "+res)
            return res


        } else {

            //println("STMTS RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-STMNTS
    fun parseASSIGNS(variable: String): Stmts {
        //println("Recognizing ASSIGNS")
        if (currentSymbol!!.symbol in setOf( Symbol.LSQURE)) {
            val t1 = parseTerminal(Symbol.LSQURE)
            val exp1 = parseEXP()
            val t2 = parseTerminal(Symbol.RSQURE)
            val t3 = parseTerminal(Symbol.EQUALS)
            val exp2 = parseEXP()

            var res = ListItemAssign(variable, exp1, exp2)

            //println("ASSIGNS RETURN: "+res)
            return res


        } else if (currentSymbol!!.symbol in setOf( Symbol.EQUALS)) {
            val t1 = parseTerminal(Symbol.EQUALS)
            val data = parseDATA()

            var res = Assign(variable, data)

            //println("ASSIGNS RETURN: "+res)
            return res
        } else {

            //println("ASSIGNS RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-CONSTRUCTNAMES
    fun parseCONSTRUCTNAMES(): Constructnames {
        //println("Recognizing CONSTRUCTNAMES")
        if (currentSymbol!!.symbol in setOf(Symbol.BUILDING, Symbol.ROAD, Symbol.RAIL, Symbol.AQUA,
                Symbol.PATH, Symbol.SHOP_TUS, Symbol.SHOP_MERCATOR)) {

            var infnames = parseINFNAMES()

            var res = InfName(infnames)
            //println("CONSTRUCTNAMES RETURN: "+res)
            return res


        } else if (currentSymbol!!.symbol in setOf(Symbol.BUILDING_COMPLEX, Symbol.PARK)) {
            var contNames = parseCONTNAMES()

            var res = ContName(contNames)
            //println("CONSTRUCTNAMES RETURN: "+res)
            return res

        } else {
            //println("CONSTRUCTNAMES RETURN: "+ "panic")
            return panic()
        }
    }

    //INH-EXP
    fun parseEXP(): Exp {
        //println("Recognizing EXP")
        val or = parseOR()

        var res = or
        //println("EXP RETURN: "+or)
        return or
    }

    //INH-EXP
    fun parseOR(): Exp{
        var and = parseAND()
        var or2 = parseOR2(and)

        var res = or2
        //println("OR RETURN: "+or2)
        return or2
    }

    fun parseOR2(iexp: Exp): Exp{
        if(currentSymbol!!.symbol in setOf(Symbol.OR)) {
            var t1 = parseTerminal(Symbol.OR)
            var and = parseAND()
            var or2 = parseOR2(and)

            var subres = Or(iexp, or2)
            var res = parseOR2(subres)

            //println("OR2 RETURN: "+res)
            return res

        }
        else if(currentSymbol!!.symbol in setOf(Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE)){
            var res = iexp
            //println("OR2 RETURN: "+res)
            return res
        }
        else{
            return panic()
        }


    }


    //INH-EXP
    fun parseAND(): Exp{

        var equal = parseEQUAL()
        var and2 = parseAND2(equal)

        var res = and2
        //println("AND RETURN: "+and2)
        return and2

    }

    fun parseAND2(iexp: Exp): Exp{

        if(currentSymbol!!.symbol in setOf(Symbol.AND)) {
            var t1 = parseTerminal(Symbol.AND)
            var equal = parseEQUAL()
            var and2 = parseAND2(equal)

            var subres = And(iexp, and2)
            var res = parseAND2(subres)

            //println("AND2 RETURN: "+res)
            return res

        }
        else if(currentSymbol!!.symbol in setOf(Symbol.OR, Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE)){
            var res = iexp
            //println("AND2 RETURN: "+res)
            return res
        }
        else{
            return panic()
        }

    }

    fun parseEQUAL(): Exp{

        var compare = parseCOMPARE()
        var equal2 = parseEQUAL2(compare)

        var res = equal2
        //println("EQUAL RETURN: "+equal2)
        return equal2

    }

    fun parseEQUAL2(iexp: Exp): Exp{

        if(currentSymbol!!.symbol in setOf(Symbol.EQUALS)) {
            var t1 = parseTerminal(Symbol.EQUALS)
            var t2 = parseTerminal(Symbol.EQUALS)
            var compare = parseCOMPARE()
            var equal2 = parseEQUAL2(compare)

            var subres = Equal(iexp, equal2)
            var res = parseEQUAL2(subres)

            //println("EQUAL2 RETURN: "+res)
            return res
        }

        else if(currentSymbol!!.symbol in setOf(Symbol.NEGATE)){
            var t1 = parseTerminal(Symbol.NEGATE)
            var t2 = parseTerminal(Symbol.EQUALS)
            var compare = parseCOMPARE()
            var equal2 = parseEQUAL2(compare)

            var subres = Inequal(iexp, equal2)
            var res = parseEQUAL2(subres)

            //println("EQUAL2 RETURN: "+res)
            return res
        }

        else if(currentSymbol!!.symbol in setOf(Symbol.AND, Symbol.OR, Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE)){
            var res = iexp
            //println("EQUAL2 RETURN: "+res)
            return res
        }
        else{
            return panic()
        }

    }

    //INH-EXP
    fun parseCOMPARE(): Exp{

        var additive = parseADDITIVE()
        var compare2 = parseCOMPARE2(additive)

        var res = compare2
        //println("COMPARE RETURN: "+compare2)
        return compare2
    }

    //INH-EXP
    fun parseCOMPARE2(iexp: Exp): Exp{

        if(currentSymbol!!.symbol in setOf(Symbol.RANGLE)){
            //greater or greaterEqual
            var t1 = parseTerminal(Symbol.RANGLE)

            if(currentSymbol!!.symbol in setOf(Symbol.EQUALS)){
                // greaterEqual
                var t2 = parseTerminal(Symbol.EQUALS)

                var add = parseADDITIVE()
                var subres = GreaterEqual(iexp, add)
                var compare2 = parseCOMPARE2(subres)

                var res = compare2
                //println("COMPARE2 RETURN: "+res)
                return res

            }
            else if(currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.NEGATE, Symbol.REAL, Symbol.VARIABLE, Symbol.LPAREN, Symbol.STRING, Symbol.TRUE, Symbol.FALSE)){
                // greater
                var add = parseADDITIVE()
                var subres = Greater(iexp, add)
                var compare2 = parseCOMPARE2(subres)

                var res = compare2
                //println("COMPARE2 RETURN: "+res)
                return res

            }
            else{
                //println("COMPARE2 RETURN: "+ "panic")
                return panic()

            }

        }
        else if(currentSymbol!!.symbol in setOf(Symbol.LANGLE)){
            //lesser or lesserEqual
            var t1 = parseTerminal(Symbol.LANGLE)

            if(currentSymbol!!.symbol in setOf(Symbol.EQUALS)){
                // lesserEqual
                var t2 = parseTerminal(Symbol.EQUALS)

                var add = parseADDITIVE()
                var subres = LesserEqual(iexp, add)
                var compare2 = parseCOMPARE2(subres)

                var res = compare2
                //println("COMPARE2 RETURN: "+res)
                return res


            }
            else if(currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.NEGATE, Symbol.REAL, Symbol.VARIABLE, Symbol.LPAREN, Symbol.STRING, Symbol.TRUE, Symbol.FALSE
                )){
                // lesser
                var add = parseADDITIVE()
                var subres = Lesser(iexp, add)
                var compare2 = parseCOMPARE2(subres)

                var res = compare2
                //println("COMPARE2 RETURN: "+res)
                return res

            }
            else{
                //println("COMPARE2 RETURN: "+ "panic")
                return panic()

            }

        }
        else if(currentSymbol!!.symbol in setOf(Symbol.EQUALS, Symbol.NEGATE, Symbol.AND, Symbol.OR, Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL, Symbol.RANGLE
            )){
            var res = iexp
            //println("COMPARE2 return"+ res)
            return res
        }
        else{
            //println("COMPARE2 RETURN: "+ "panic")
            return panic()
        }

    }



    //INH-EXP
    fun parseADDITIVE(): Exp {
        //println("Recognizing ADDITIVE")

        val multi = parseMULTIPLICATIVE()
        val add = parseADDITIVE2(multi)

        var res = add
        //println("ADDITIVE RETURN: "+add)
        return add

    }


    //INH EXP
    fun parseADDITIVE2(iexp: Exp): Exp {
        //println("Recognizing ADDITIVE2")
        if (currentSymbol!!.symbol in setOf( Symbol.PLUS)) {
            val t1 = parseTerminal(Symbol.PLUS)
            val multi = parseMULTIPLICATIVE()

            var subres = Plus(iexp, multi)
            val add2 = parseADDITIVE2(subres)

            var res = add2
            //println("ADDITIVE2 RETURN: "+add2)
            return add2

        } else if (currentSymbol!!.symbol in setOf( Symbol.MINUS)) {
            val t1 = parseTerminal(Symbol.MINUS)
            val multi = parseMULTIPLICATIVE()

            var subres = Minus(iexp, multi)
            val add2 = parseADDITIVE2(subres)

            var res = add2
            //println("ADDITIVE2 RETURN: "+add2)
            return add2

        } else if(currentSymbol!!.symbol in setOf(Symbol.RANGLE, Symbol.LANGLE, Symbol.EQUALS, Symbol.NEGATE, Symbol.AND, Symbol.OR, Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL)){

            var res = iexp
            //println("ADDITIVE2 RETURN: "+ res)
            return res // EPSILON case
        }
        else{

            //println("ADDITIVE2 RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-EXP
    fun parseMULTIPLICATIVE(): Exp {
        //println("Recognizing MULTIPLICATIVE")
        var expo = parseEXPONENTIAL()

        val multi2 = parseMULTIPLICATIVE2(expo)

        var res = multi2
        //println("MULTIPLICATIVE RETURN: "+multi2)
        return multi2

    }


    //INH-EXP
    fun parseMULTIPLICATIVE2(iexp : Exp): Exp {
        //println("Recognizing MULTIPLICATIVE2")
        if (currentSymbol!!.symbol in setOf(Symbol.TIMES)) {

            val t1 = parseTerminal(Symbol.TIMES)
            val expon = parseEXPONENTIAL()

            val subres = Times(iexp, expon)

            val multi2 = parseMULTIPLICATIVE2(subres)
            var res = multi2

            //println("MULTIPLICATIVE2 RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.DIVIDE)) {
            val t1 = parseTerminal(Symbol.DIVIDE)
            val expon = parseEXPONENTIAL()

            val subres = Divides(iexp, expon)

            val multi2 = parseMULTIPLICATIVE2(subres)
            var res = multi2

            //println("MULTIPLICATIVE2 RETURN: "+multi2)
            return multi2
        } else if (currentSymbol!!.symbol in setOf( Symbol.INTEGER_DIVIDE)) {
            val t1 = parseTerminal(Symbol.INTEGER_DIVIDE)
            val expon = parseEXPONENTIAL()

            val subres = IntegerDivides(iexp, expon)

            val multi2 = parseMULTIPLICATIVE2(subres)
            var res = multi2

            //println("MULTIPLICATIVE2 RETURN: "+res)
            return res
        } else if( currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.RANGLE, Symbol.LANGLE, Symbol.EQUALS, Symbol.NEGATE, Symbol.AND, Symbol.OR, Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL)) {

            var res = iexp
            //println("MULTIPLICATIVE2 RETURN: "+res)
            return res // EPSILON case
        }
        else{

            //println("MULTIPLICATIVE2 RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-EXP
    fun parseEXPONENTIAL(): Exp {
        //println("Recognizing EXPONENTIAL")
        val unary = parseUNARY()

        val expo2 = parseEXPONENTIAL2(unary)

        var res = expo2
        //println("EXPONENTIAL RETURN: "+expo2)
        return expo2

    }


    //INH-EXP
    fun parseEXPONENTIAL2(iexp: Exp): Exp {
        //println("Recognizing EXPONENTIAL2")
        if (currentSymbol!!.symbol in setOf( Symbol.POW)) {
            val t1 = parseTerminal(Symbol.POW)

            val unary = parseUNARY()
            val rhs = parseEXPONENTIAL2(unary)

            var subres = Pow(iexp, rhs)

            var res = parseEXPONENTIAL2(subres)
            //println("EXPONENTIAL2 RETURN: "+res)
            return res

        } else if(currentSymbol!!.symbol in setOf(Symbol.TIMES, Symbol.DIVIDE, Symbol.INTEGER_DIVIDE, Symbol.PLUS, Symbol.MINUS, Symbol.RANGLE, Symbol.LANGLE, Symbol.EQUALS, Symbol.NEGATE, Symbol.AND, Symbol.OR, Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL)){

            var res = iexp
            //println("EXPONENTIAL2 RETURN: "+res)

            return res // EPSILON case
        }
        else{

            //println("EXPONENTIAL2 RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-EXP
    fun parseUNARY(): Exp {
        //println("Unary: "+currentSymbol!!.symbol + " "+currentSymbol!!.lexeme)
        if (currentSymbol!!.symbol in setOf( Symbol.PLUS)) {
            val t1 = parseTerminal(Symbol.PLUS)

            val primary = parsePRIMARY()

            var res = UnaryPlus(primary)
            //println("UNARY RETURN: "+res)
            return res

        }
        else if (currentSymbol!!.symbol in setOf( Symbol.MINUS)) {
            val t1 = parseTerminal(Symbol.MINUS)

            val primary = parsePRIMARY()

            var res = UnaryMinus(primary)
            //println("UNARY RETURN: "+res)
            return res

        }
        else if(currentSymbol!!.symbol in setOf(Symbol.NEGATE)){
            var t1 = parseTerminal(Symbol.NEGATE)

            var primary = parsePRIMARY()

            var res = Negate(primary)
            //println("UNARY RETURN: "+res)
            return res

        }
        else if(currentSymbol!!.symbol in setOf( Symbol.REAL, Symbol.VARIABLE, Symbol.LPAREN, Symbol.STRING, Symbol.TRUE, Symbol.FALSE)){
            val primary = parsePRIMARY()

            var res = primary
            //println("UNARY RETURN: "+res)
            return res

        }
        else{

            //println("UNARY RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-EXP
    fun parsePRIMARY(): Exp {
        //println("Recognizing PRIMARY")
        if (currentSymbol!!.symbol in setOf( Symbol.REAL)) {
            var real = parseTerminal(Symbol.REAL)

            var res = Real(real.toDouble())
            //println("PRIMARY RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.VARIABLE)) {
            var variable = parseTerminal(Symbol.VARIABLE)
            val primary1 = parsePRIMARY1(variable)

            var res = primary1
            //println("PRIMARY RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.LPAREN)) {
            val t1 = parseTerminal(Symbol.LPAREN)
            val exp = parseEXP()
            val primary2 = parsePRIMARY2(exp)

            var res = primary2
            //println( "PRIMARY RETURN: "+res)
            return res

        } else if (currentSymbol!!.symbol in setOf( Symbol.STRING)) {
            val string = parseTerminal(Symbol.STRING)

            var res = StringExp(string)
            //println("PRIMARY RETURN: "+res)
            return res

        }

        else if(currentSymbol!!.symbol in setOf(Symbol.FALSE, Symbol.TRUE)){
            var bool = parseBOOL()

            var res = bool

            //println("PRIMARY RETURN: "+res)
            return res
        }
        else{

            //println("PRIMARY RETURN: "+ "panic")
            return panic()
        }
    }


    fun parseBOOL(): Exp{

        if(currentSymbol!!.symbol in setOf(Symbol.TRUE)){
            var t1 = parseTerminal(Symbol.TRUE)

            var res = BooleanExp(true)
            //println("BOOL RETURN: "+res)
            return res
        }
        else if(currentSymbol!!.symbol in setOf(Symbol.FALSE)){
            var t1 = parseTerminal(Symbol.FALSE)

            var res = BooleanExp(false)
            //println("BOOL RETURN: "+res)
            return res
        }
        else{
            return panic()
        }

    }


    //INH-EXP
    fun parsePRIMARY1(ivariable: String): Exp {
        //println("Recognizing PRIMARY1")
        if (currentSymbol!!.symbol in setOf( Symbol.LSQURE)) {
            val t1 = parseTerminal(Symbol.LSQURE)
            val exp = parseEXP()
            val t3 = parseTerminal(Symbol.RSQURE)

            var res = ListIndex(ivariable, exp)
            //println("PRIMARY1 RETURN: "+res)
            return res

        } else if(currentSymbol!!.symbol in setOf(Symbol.POW, Symbol.TIMES, Symbol.DIVIDE, Symbol.INTEGER_DIVIDE, Symbol.PLUS, Symbol.MINUS, Symbol.RANGLE, Symbol.LANGLE, Symbol.EQUALS, Symbol.NEGATE, Symbol.AND, Symbol.OR, Symbol.COMMA, Symbol.RPAREN, Symbol.RSQURE, Symbol.SEMICOL)){

            var res = Variable(ivariable)
            //println("PRIMARY1 RETURN: "+ res)
            return res // EPSILON case
        }
        else{

            //println("PRIMARY1 RETURN: "+ "panic")
            return panic()
        }
    }

    //INH-EXP
    fun parsePRIMARY2(iexp: Exp): Exp {
        //println("Recognizing PRIMARY2")
        if (currentSymbol!!.symbol in setOf( Symbol.RPAREN)) {
            val t1 = parseTerminal(Symbol.RPAREN)

            var res = iexp
            //println( "PRIMARY2 RETURN: "+res)
            return res
        }
        else if(currentSymbol!!.symbol in setOf( Symbol.COMMA)){
            val t1 = parseTerminal(Symbol.COMMA)
            val exp = parseEXP()
            val t2 = parseTerminal(Symbol.RPAREN)

            var res = Point(iexp, exp)
            //println("PRIMARY2 RETURN: "+res)
            return res
        }
        else{

            //println("PRIMARY2 RETURN: "+ "panic")
            return panic()
        }
    }

    //INH-DATA
    fun parseDATA(): Data {
        //println("Recognizing DATA")
        if (currentSymbol!!.symbol in setOf(Symbol.LSQURE)) {
            var list = parseLIST()

            var res = list
            //println("DATA RETURN: "+res)
            return res

        } else if(currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.NEGATE, Symbol.REAL, Symbol.VARIABLE, Symbol.LPAREN, Symbol.STRING, Symbol.TRUE, Symbol.FALSE)){
            var exp = parseEXP()

            var res = ExpData(exp)
            //println("DATA RETURN: "+res)
            return res
        }
        else{

            //println("DATA RETURN: "+ "panic")
            return panic()
        }
    }

    //INH-DATA
    fun parseLIST(): Data {
        //println("Recognizing LIST")
        val t1 = parseTerminal(Symbol.LSQURE)
        val listitems = parseLISTITEM()
        val t2 = parseTerminal(Symbol.RSQURE)

        var res = ListData(listitems)
        //println("LIST RETURN: "+res)
        return res
    }


    //INH-LISTITEMS
    fun parseLISTITEM(): Listitems {
        //println("Recognizing LISTITEM")
        if (currentSymbol!!.symbol in setOf(Symbol.PLUS, Symbol.MINUS, Symbol.REAL, Symbol.VARIABLE,
                Symbol.LPAREN, Symbol.STRING, Symbol.TRUE, Symbol.FALSE)) {
            val exp1 = parseEXP()
            val listitem2 = parseLISTITEM2()

            var res = SeqListItems(exp1, listitem2)
            //println("LISTITEM RETURN: "+res)
            return res

        } else if(currentSymbol!!.symbol in setOf(Symbol.RSQURE)){

            var res = EndListitems()
            //println("LISTITEM RETURN: "+res)
            return res
        }
        else{

            //println("LISTITEM RETURN: "+ "panic")
            return panic()
        }
    }


    //INH-LISTITEMS
    fun parseLISTITEM2(): Listitems {
        //println("Recognizing LISTITEM2")
        if (currentSymbol!!.symbol in setOf( Symbol.COMMA)) {
            val t1 = parseTerminal(Symbol.COMMA)
            val exp = parseEXP()
            val listitems2 = parseLISTITEM2()

            var res = SeqListItems(exp, listitems2)
            //println("LISTITEM2 RETURN: "+res)
            return res


        } else if( currentSymbol!!.symbol in setOf(Symbol.RSQURE)){

            var res = EndListitems()
            //println("LISTITEM2 RETURN: "+ res)
            return res
        }
        else{

            //println("LISTITEM2 RETURN: "+ "panic")
            return panic()
        }
    }
}