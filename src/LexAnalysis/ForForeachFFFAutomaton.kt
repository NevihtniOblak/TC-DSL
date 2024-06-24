package LexAnalysis

object ForForeachFFFAutomaton: DFA {
    const val ERROR_STATE = 0
    const val EOF = -1
    const val NEWLINE = '\n'.code

    override val states = (1 .. 186).toSet()
    override val alphabet = 0 .. 255
    override val startState = 1

    private val alpha = ('A'..'Z') + ('a'..'z')
    private val alphaNum = alpha + ('0'..'9')
    private val alphaNumExtra = alphaNum + listOf('+', '-', '*', '/', '.', '-', '_', ' ', '!','?')

    //DELO Z STANJII

    val normalStates = setOf(2, 3, 9, 18, 19, 20, 21, 22, 23, 27, 28, 36,
        40, 43, 47, 51, 58, 66, 74, 76, 77, 78, 89, 90, 99, 105, 111, 113, 117, 123, 127, 130, 131,
        134, 137, 141, 155, 156, 157, 158, 159, 160, 161, 162, 163, 166, 167, 169, 170, 174, 177, 181, 183, 185, 186)

    //vmesna stanja ki lahko z nekim branjem grejo v variable (prvi znak je stanje in drugi znak je alphanum znak, ki ga ne smemo prebrati da gre v variable stanje)
    private val canGoToVariable = listOf(
        Pair(1,'S'), Pair(4,'C'), Pair(5,'H'), Pair(6,'E'), Pair(7,'M'), Pair(8,'A'), // schema(SCHEMA)
        Pair(1,'p'), Pair(10,'r'), Pair(11,'o'), Pair(12,'c'), Pair(13,'e'), Pair(14,'d'), Pair(15,'u'), Pair(16,'r'), Pair(17,'e'), // PROCEDURE(procedure)
        Pair(1,'C'), Pair(24,'i'), Pair(25,'t'), Pair(26,'y'), // CITY(City)
        Pair(1,'B'), Pair(29,'u'), Pair(30,'i'), Pair(31,'l'), Pair(32,'d'), Pair(33,'i'), Pair(34,'n'), Pair(35,'g'), // BUILDING(Building)
        Pair(1,'R'), Pair(37,'o'), Pair(38,'a'), Pair(39,'d'),// ROAD(Road)
        Pair(37,'a'), Pair(41,'i'),Pair(42,'l'), // RAIL(Rail)
        Pair(1,'A'), Pair(44,'q'), Pair(45,'u'), Pair(46,'a'), // AQUA(Aqua)
        Pair(1,'P'), Pair(48,'a'), Pair(49,'t'), Pair(50,'h'), // PATH(Path)
        Pair(4,'h'), Pair(52,'o'), Pair(53,'p'), Pair(54,'-'), Pair(55,'T'), Pair(56,'u'), Pair(57,'s'), // SHOP-TUS(Shop-Tus)
        Pair(55,'M'), Pair(59,'e'), Pair(60,'r'), Pair(61,'c'), Pair(62,'a'), Pair(63,'t'), Pair(64,'o'), Pair(65, 'r'), // SHOP-MERCATOR(Shop-Mercator)
        Pair(36,'-'), Pair(67,'C'), Pair(68,'o'), Pair(69,'m'), Pair(70,'p'), Pair(71,'l'), Pair(72,'e'),Pair(73,'x'), // BUILDING-COMPLEX(Building-Complex)
        Pair(49,'r'), Pair(50,'k'), // PARK(Park)
        Pair(1,'s'), Pair(79,'e'), Pair(80,'t'), Pair(81,'L'), Pair(82,'o'), Pair(83,'c'), Pair(84,'a'), Pair(85,'t'), Pair(86,'i'), Pair(87,'o'), Pair(88,'n'), // SET_LOCATION(setLocation)
        Pair(1,'t'), Pair(91,'r'), Pair(92,'a'), Pair(93,'n'), Pair(94,'s'), Pair(95,'l'), Pair(96,'a'), Pair(97,'t'), Pair(98, 'e'),// TRANSLATE(translate)
        Pair(1,'r'), Pair(100,'o'), Pair(101,'t'), Pair(102,'a'), Pair(103,'t') , Pair(104,'e'), // ROTATE(rotate)
        Pair(81,'M'), Pair(106,'a'), Pair(107,'r'), Pair(108,'k'), Pair(109,'e'), Pair(110,'r'), // SET_MARKER(setMarker)
        Pair(29,'o'), Pair(112,'x'),// BOX(Box)
        Pair(1,'L'), Pair(114,'i'), Pair(115,'n'), Pair(116,'e'),// LINE(Line)
        Pair(48,'o'), Pair(118,'l'), Pair(119,'y'), Pair(120,'g'), Pair(121,'o'),  Pair(122,'n'), // POLYGON(Polygon)
        Pair(25,'r'), Pair(124,'c'), Pair(125,'l'), Pair(126,'e'),// CIRCLE(Circle)
        Pair(127,'L'), Pair(171,'i'), Pair(172,'n'), Pair(173,'e'), // CIRCLELINE(CircleLine)
        Pair(1,'v'), Pair(128,'a'), Pair(129,'r'),// VAR(var)
        Pair(1,'f'), Pair(132,'o'), Pair(133,'r'),// FOR(for)
        Pair(11,'i'), Pair(135,'n'), Pair(136,'t'),// PRINT(print)
        Pair(1,'c'), Pair(138,'a'), Pair(139,'l'), Pair(140,'l'), // CALL(call)
        Pair(1,'d'), Pair(142,'i'), Pair(143,'s'), Pair(144,'p'), Pair(145,'l'), Pair(146,'a'), Pair(147,'y'), Pair(148,'M'), Pair(149,'a'), Pair(150,'r'), Pair(151,'k'), Pair(152,'e'), Pair(153,'r'), Pair(154,'s'), // DISPLAY_MARKERS(displayMarkers)
        Pair(92, 'u'), Pair(176, 'e'), // true
        Pair(132, 'a'), Pair(178, 'l'), Pair(179, 's'), Pair(180, 'e') // false

    )

    //funkcija da lahko gorjni zapis pretvorimo v obliko mape kjer je kljuc stanje in vrednost seznam znakov ki jih lahko preberemo da gre v variable
    private fun groupPairs(pairs: List<Pair<Int, Char>>): List<Pair<Int, List<Char>>> {
        return pairs.groupBy({ it.first }, { it.second })
            .map { Pair(it.key, it.value) }
    }


    //vsa stanja iz katerih lahko preidemo na variable z branjem ustreznega znaka -> set vseh stanj od canGoToVariable
    val canGoToVatriableStates = canGoToVariable.map { it.first }.toSet()


    //vsa stanja v katerih ce koncamo dobimo variable -> to so stanja ki lahko grejo v varibale in hkrati niso koncna stanja nekih drugih konstruktov ali zacetno stanje 1
    val extraVariableStates = canGoToVatriableStates - normalStates - setOf(1)


    //KONCNA STANJA
    override val finalStates = normalStates + extraVariableStates

    private val numberOfStates = states.max() + 1 // plus the ERROR_STATE
    private val numberOfCodes = alphabet.max() + 1 // plus the EOF
    private val transitions = Array(numberOfStates) {IntArray(numberOfCodes)}
    private val values = Array(numberOfStates) { Symbol.SKIP }

    private fun setTransition(from: Int, chr: Char, to: Int) {
        transitions[from][chr.code + 1] = to // + 1 because EOF is -1 and the array starts at 0
    }

    private fun setTransition(from: Int, code: Int, to: Int) {
        transitions[from][code + 1] = to
    }

    private fun setSymbol(state: Int, symbol: Symbol) {
        values[state] = symbol
    }

    override fun next(state: Int, code: Int): Int {
        assert(states.contains(state))
        assert(alphabet.contains(code))
        return transitions[state][code + 1]
    }

    override fun symbol(state: Int): Symbol {
        assert(states.contains(state))
        return values[state]
    }

    init {


        //TRANZICIJE

        //eof
        setTransition(1, EOF, 2)

        //skip
        setTransition(1, ' ', 3)
        setTransition(1, '\t', 3)
        setTransition(1, '\r', 3)
        setTransition(1, '\n', 3)

        //schema(SCHEMA)
        setTransition(1,'S', 4)
        setTransition(4,'C', 5)
        setTransition(5,'H', 6)
        setTransition(6,'E', 7)
        setTransition(7,'M', 8)
        setTransition(8,'A', 9)

        for(char in alphaNum){
            setTransition(9, char, 170)
        }

        //PROCEDURE(procedure)
        setTransition(1,'p', 10)
        setTransition(10,'r', 11)
        setTransition(11,'o', 12)
        setTransition(12,'c', 13)
        setTransition(13,'e', 14)
        setTransition(14,'d', 15)
        setTransition(15,'u', 16)
        setTransition(16,'r', 17)
        setTransition(17,'e', 18)

        for(char in alphaNum){
            setTransition(18, char, 170)
        }

        //ignore
        //LPAREN(()
        setTransition(1,'(', 19)

        //ignore
        //RPAREN ())
        setTransition(1,')', 20)

        //ignore
        //LCURLY({)
        setTransition(1,'{', 21)

        //ignore
        //RCURLY(})
        setTransition(1,'}', 22)

        //ignore
        //COMMA(,)
        setTransition(1,',', 23)

        //CITY(City)
        setTransition(1,'C', 24)
        setTransition(24,'i', 25)
        setTransition(25,'t', 26)
        setTransition(26,'y', 27)

        for(char in alphaNum){
            setTransition(27, char, 170)
        }

        //ignore
        //COLON(:)
        setTransition(1,':', 28)

        //BUILDING(Building)
        setTransition(1,'B', 29)
        setTransition(29,'u', 30)
        setTransition(30,'i', 31)
        setTransition(31,'l', 32)
        setTransition(32,'d', 33)
        setTransition(33,'i', 34)
        setTransition(34,'n', 35)
        setTransition(35,'g', 36)

        for(char in alphaNum){
            setTransition(36, char, 170)
        }

        //ROAD(Road)
        setTransition(1,'R', 37)
        setTransition(37,'o', 38)
        setTransition(38,'a', 39)
        setTransition(39,'d', 40)

        for(char in alphaNum){
            setTransition(40, char, 170)
        }

        //RAIL(Rail)
        //R dobi od Road
        setTransition(37,'a', 41)
        setTransition(41,'i', 42)
        setTransition(42,'l', 43)

        for(char in alphaNum){
            setTransition(43, char, 170)
        }

        //AQUA(Aqua)
        setTransition(1,'A', 44)
        setTransition(44,'q', 45)
        setTransition(45,'u', 46)
        setTransition(46,'a', 47)

        for(char in alphaNum){
            setTransition(47, char, 170)
        }

        //PATH(Path)
        setTransition(1,'P', 48)
        setTransition(48,'a', 49)
        setTransition(49,'t', 50)
        setTransition(50,'h', 51)

        for(char in alphaNum){
            setTransition(51, char, 170)
        }

        //SHOP-TUS(Shop-Tus)
        //S dobi od SCHEMA
        setTransition(4,'h', 52)
        setTransition(52,'o', 53)
        setTransition(53,'p', 54)
        setTransition(54,'-', 55)
        setTransition(55,'T', 56)
        setTransition(56,'u', 57)
        setTransition(57,'s', 58)

        for(char in alphaNum){
            setTransition(58, char, 170)
        }

        //SHOP-MERCATOR(Shop-Mercator)
        //Shop- dobi od SHOP-TUS
        setTransition(55,'M', 59)
        setTransition(59,'e', 60)
        setTransition(60,'r', 61)
        setTransition(61,'c', 62)
        setTransition(62,'a', 63)
        setTransition(63,'t', 64)
        setTransition(64,'o', 65)
        setTransition(65,'r', 66)

        for(char in alphaNum){
            setTransition(66, char, 170)
        }

        //BUILDING-COMPLEX(Building-Complex)
        //Building dobi od BUILDING
        setTransition(36,'-', 67)
        setTransition(67,'C', 68)
        setTransition(68,'o', 69)
        setTransition(69,'m', 70)
        setTransition(70,'p', 71)
        setTransition(71,'l', 72)
        setTransition(72,'e', 73)
        setTransition(73,'x', 74)

        for(char in alphaNum){
            setTransition(74, char, 170)
        }

        //PARK(Park)
        //Pa dobi od PATH
        setTransition(49,'r', 75)
        setTransition(75,'k', 76)

        for(char in alphaNum){
            setTransition(76, char, 170)
        }

        //ignore
        //LANGLE(<)
        setTransition(1,'<', 77)

        //ignore
        //RANGLE(>)
        setTransition(1,'>', 78)

        //SET_LOCATION(setLocation)
        setTransition(1,'s', 79)
        setTransition(79,'e', 80)
        setTransition(80,'t', 81)
        setTransition(81,'L', 82)
        setTransition(82,'o', 83)
        setTransition(83,'c', 84)
        setTransition(84,'a', 85)
        setTransition(85,'t', 86)
        setTransition(86,'i', 87)
        setTransition(87,'o', 88)
        setTransition(88,'n', 89)

        for(char in alphaNum){
            setTransition(89, char, 170)
        }

        //ignore
        //SEMICOL(;)
        setTransition(1,';', 90)

        //TRANSLATE(translate)
        setTransition(1,'t', 91)
        setTransition(91,'r', 92)
        setTransition(92,'a', 93)
        setTransition(93,'n', 94)
        setTransition(94,'s', 95)
        setTransition(95,'l', 96)
        setTransition(96,'a', 97)
        setTransition(97,'t', 98)
        setTransition(98,'e', 99)

        for(char in alphaNum){
            setTransition(99, char, 170)
        }

        //ROTATE(rotate)
        setTransition(1,'r', 100)
        setTransition(100,'o', 101)
        setTransition(101,'t', 102)
        setTransition(102,'a', 103)
        setTransition(103,'t', 104)
        setTransition(104,'e', 105)

        for(char in alphaNum){
            setTransition(105, char, 170)
        }

        //SET_MARKER(setMarker)
        //set dobi od SET_LOCATION
        setTransition(81,'M', 106)
        setTransition(106,'a', 107)
        setTransition(107,'r', 108)
        setTransition(108,'k', 109)
        setTransition(109,'e', 110)
        setTransition(110,'r', 111)

        for(char in alphaNum){
            setTransition(112, char, 170)
        }

        //BOX(Box)
        //B dobi od BUILDING
        setTransition(29,'o', 112)
        setTransition(112,'x', 113)

        for(char in alphaNum){
            setTransition(113, char, 170)
        }

        //LINE(Line)
        setTransition(1,'L', 114)
        setTransition(114,'i', 115)
        setTransition(115,'n', 116)
        setTransition(116,'e', 117)

        for(char in alphaNum){
            setTransition(117, char, 170)
        }

        //POLYGON(Polygon)
        //P dobi od PATH
        setTransition(48,'o', 118)
        setTransition(118,'l', 119)
        setTransition(119,'y', 120)
        setTransition(120,'g', 121)
        setTransition(121,'o', 122)
        setTransition(122,'n', 123)

        for(char in alphaNum){
            setTransition(123, char, 170)
        }

        //CIRCLE(Circle)
        //Ci dobi od CITY
        setTransition(25,'r', 124)
        setTransition(124,'c', 125)
        setTransition(125,'l', 126)
        setTransition(126,'e', 127)

        for(char in alphaNum){
            setTransition(127, char, 170)
        }

        //CIRCLELINE(CircleLine)
        //CIRCLE dobi od CIRCLE
        setTransition(127,'L', 171)
        setTransition(171,'i', 172)
        setTransition(172,'n', 173)
        setTransition(173,'e', 174)

        for(char in alphaNum){
            setTransition(174, char, 170)
        }

        //VAR(var)
        setTransition(1,'v', 128)
        setTransition(128,'a', 129)
        setTransition(129,'r', 130)

        for(char in alphaNum){
            setTransition(130, char, 170)
        }

        //ignore
        //EQUALS(=)
        setTransition(1,'=', 131)

        //FOR(for)
        setTransition(1,'f', 132)
        setTransition(132,'o', 133)
        setTransition(133,'r', 134)

        for(char in alphaNum){
            setTransition(134, char, 170)
        }

        //PRINT(print)
        //Pr dobi od PROCEDURE

        setTransition(11,'i', 135)
        setTransition(135,'n', 136)
        setTransition(136,'t', 137)

        for(char in alphaNum){
            setTransition(137, char, 170)
        }

        //CALL(call)
        setTransition(1,'c', 138)
        setTransition(138,'a', 139)
        setTransition(139,'l', 140)
        setTransition(140,'l', 141)

        for(char in alphaNum){
            setTransition(141, char, 170)
        }

        //DISPLAY_MARKERS(displayMarkers)
        setTransition(1,'d', 142)
        setTransition(142,'i', 143)
        setTransition(143,'s', 144)
        setTransition(144,'p', 145)
        setTransition(145,'l', 146)
        setTransition(146,'a', 147)
        setTransition(147,'y', 148)
        setTransition(148,'M', 149)
        setTransition(149,'a', 150)
        setTransition(150,'r', 151)
        setTransition(151,'k', 152)
        setTransition(152,'e', 153)
        setTransition(153,'r', 154)
        setTransition(154,'s', 155)

        for(char in alphaNum){
            setTransition(155, char, 170)
        }

        //ignore
        //LSQURE([)
        setTransition(1,'[', 156)

        //ignore
        //RSQURE(])
        setTransition(1,']', 157)

        //ignore
        //PLUS(+)
        setTransition(1,'+', 158)

        //ignore
        //MINUS(-)
        setTransition(1,'-', 159)

        //ignore
        //MULTIPLY(*)
        setTransition(1,'*', 160)

        //ignore
        //DIVIDE(/)
        setTransition(1,'/', 161)

        //ignore
        //INTEGER_DIVIDE(//)
        setTransition(161,'/', 162)

        //ignore
        //POW(^)
        setTransition(1,'^', 163)

        //ignore
        //STRING
        setTransition(1,'"', 164)
        for (char in alphaNumExtra) {
            setTransition(164, char, 165)
            setTransition(165, char, 165)
        }
        setTransition(165, '"', 166)

        //ignore
        //REAL
        for(i in '0'..'9'){
            setTransition(1, i, 167)
            setTransition(167, i, 167)
        }
        setTransition(167, '.', 168)
        for(i in '0'..'9'){
            setTransition(168, i, 169)
            setTransition(169, i, 169)
        }

        //VARIABLE
        val canGoToVariableMaps = groupPairs(canGoToVariable)

        /*
        for ( element in canGoToVariableMaps){
            println(element)
        }

         */

        for(map in canGoToVariableMaps){
            //pazimo na 1. stanje -> variable rabi vsaj neki alpha znak
            if(map.first == 1){
                for (char in alpha - map.second) {
                    setTransition(map.first, char, 170)
                }
            }
            else {
                for (char in alphaNum - map.second) {
                    setTransition(map.first, char, 170)
                }
            }
        }

        for(char in alphaNum){
            setTransition(170, char, 170)
        }

        //true
        //tr dobi od translate
        setTransition(92, 'u', 175)
        setTransition(175, 'e', 177)

        for(char in alphaNum){
            setTransition(177, char, 170)
        }

        //false
        //f dobi od for
        setTransition(132, 'a', 178)
        setTransition(178, 'l', 179)
        setTransition(179, 's', 180)
        setTransition(180, 'e', 181)

        for(char in alphaNum){
            setTransition(181, char, 170)
        }

        //AND(&&)
        setTransition(1, '&', 182)
        setTransition(182, '&', 183)

        //OR(||)
        setTransition(1, '|', 184)
        setTransition(184, '|', 185)

        //NEGATE(!)
        setTransition(1, '!', 186)


        //KONCNA STANJA
        //eof
        setSymbol(2, Symbol.EOF)
        //schema(SCHEMA)
        setSymbol(9, Symbol.SCHEMA)
        //PROCEDURE(procedure)
        setSymbol(18, Symbol.PROCEDURE)
        //LPAREN(()
        setSymbol(19, Symbol.LPAREN)
        //RPAREN ())
        setSymbol(20, Symbol.RPAREN)
        //LCURLY({)
        setSymbol(21, Symbol.LCURLY)
        //RCURLY(})
        setSymbol(22, Symbol.RCURLY)
        //COMMA(:)
        setSymbol(23, Symbol.COMMA)
        //CITY(City)
        setSymbol(27, Symbol.CITY)
        //COLON(:)
        setSymbol(28, Symbol.COLON)
        //BUILDING(Building)
        setSymbol(36, Symbol.BUILDING)
        //ROAD(Road)
        setSymbol(40, Symbol.ROAD)
        //RAIL(Rail)
        setSymbol(43, Symbol.RAIL)
        //AQUA(Aqua)
        setSymbol(47, Symbol.AQUA)
        //PATH(Path)
        setSymbol(51, Symbol.PATH)
        //SHOP-TUS(Shop-Tus)
        setSymbol(58, Symbol.SHOP_TUS)
        //SHOP-MERCATOR(Shop-Mercator)
        setSymbol(66, Symbol.SHOP_MERCATOR)
        //BUILDING-COMPLEX(Building-Complex)
        setSymbol(74, Symbol.BUILDING_COMPLEX)
        //PARK(Park)
        setSymbol(76, Symbol.PARK)
        //LANGLE(<)
        setSymbol(77, Symbol.LANGLE)
        //RANGLE(>)
        setSymbol(78, Symbol.RANGLE)
        //SET_LOCATION(setLocation)
        setSymbol(89, Symbol.SET_LOCATION)
        //SEMICOL(;)
        setSymbol(90, Symbol.SEMICOL)
        //TRANSLATE(translate)
        setSymbol(99, Symbol.TRANSLATE)
        //ROTATE(rotate)
        setSymbol(105, Symbol.ROTATE)
        //SET_MARKER(setMarker)
        setSymbol(111, Symbol.SET_MARKER)
        //BOX(Box)
        setSymbol(113, Symbol.BOX)
        //LINE(Line)
        setSymbol(117, Symbol.LINE)
        //POLYGON(Polygon)
        setSymbol(123, Symbol.POLYGON)
        //CIRCLE(Circle)
        setSymbol(127, Symbol.CIRCLE)
        //VAR(var)
        setSymbol(130, Symbol.VAR)
        //EQUALS(=)
        setSymbol(131, Symbol.EQUALS)
        //FOR(for)
        setSymbol(134, Symbol.FOR)
        //PRINT(print)
        setSymbol(137, Symbol.PRINT)
        //CALL(call)
        setSymbol(141, Symbol.CALL)
        //DISPLAY_MARKERS(displayMarkers)
        setSymbol(155, Symbol.DISPLAY_MARKERS)
        //LSQURE([)
        setSymbol(156, Symbol.LSQURE)
        //RSQURE(])
        setSymbol(157, Symbol.RSQURE)
        //PLUS(+)
        setSymbol(158, Symbol.PLUS)
        //MINUS(-)
        setSymbol(159, Symbol.MINUS)
        //MULTIPLY(*)
        setSymbol(160, Symbol.TIMES)
        //DIVIDE(/)
        setSymbol(161, Symbol.DIVIDE)
        //INTEGER_DIVIDE(//)
        setSymbol(162, Symbol.INTEGER_DIVIDE)
        //POW(^)
        setSymbol(163, Symbol.POW)
        //STRING("alphanumericExtra")
        setSymbol(166, Symbol.STRING)

        //REAL(num.num)
        setSymbol(167, Symbol.REAL)
        setSymbol(169, Symbol.REAL)

        //VARIABLE(alpha+alphanum*)
        setSymbol(170, Symbol.VARIABLE)

        //zagotovimo da so ustrezna vmesna stanja tudi lahko spremenljivke
        for (state in extraVariableStates){
            setSymbol(state, Symbol.VARIABLE)
        }

        //CIRCLELINE(CircleLine)
        setSymbol(174, Symbol.CIRCLELINE)

        //TRUE(true)
        setSymbol(177, Symbol.TRUE)

        //FALSE(false)
        setSymbol(181, Symbol.FALSE)

        //AND(&&)
        setSymbol(183, Symbol.AND)

        //OR(||)
        setSymbol(185, Symbol.OR)

        //NEGATE(!)
        setSymbol(186, Symbol.NEGATE)




    }
}