package LexAnalysis

object ForForeachFFFAutomaton: DFA {
    const val ERROR_STATE = 0
    const val EOF = -1
    const val NEWLINE = '\n'.code

    override val states = (1 .. 170).toSet()
    override val alphabet = 0 .. 255
    override val startState = 1
    override val finalStates = setOf(2,3, 9, 18, 19, 20, 21, 22, 23, 27, 28, 36,
        40, 43, 47, 51, 58, 66, 74, 76, 77, 78, 89, 90, 99, 105, 111, 113, 117, 123, 127, 130, 131,
        134, 137, 141, 155, 156, 157, 158, 159, 160, 161, 162, 163, 166, 167, 169, 170)

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
        val alpha = ('A'..'Z') + ('a'..'z')
        val alphaNum = alpha + ('0'..'9')
        val alphaNumExtra = alphaNum + listOf('+', '-', '*', '/', '.', '-', '_', ' ', '!','?')

        val canBeVariable = listOf(
            Pair(1,'S'), Pair(4,'C'), Pair(5,'H'), Pair(6,'E'), Pair(7,'M'), // schema(SCHEMA)
            Pair(1,'p'), Pair(10,'r'), Pair(11,'o'), Pair(12,'c'), Pair(13,'e'), Pair(14,'d'), Pair(15,'u'), Pair(16,'r'), // PROCEDURE(procedure)
            Pair(1,'C'), Pair(24,'i'), Pair(25,'t'), Pair(26,'y'), // CITY(City)
            Pair(1,'B'), Pair(29,'u'), Pair(30,'i'), Pair(31,'l'), Pair(32,'d'), Pair(33,'i'), Pair(34,'n'), // BUILDING(Building)
            Pair(1,'R'), Pair(37,'o'), Pair(38,'a'), // ROAD(Road)
            Pair(37,'a'), Pair(41,'i'), // RAIL(Rail)
            Pair(1,'A'), Pair(44,'q'), Pair(45,'u'), // AQUA(Aqua)
            Pair(1,'P'), Pair(48,'a'), Pair(49,'t'), // PATH(Path)
            Pair(4,'h'), Pair(52,'o'), Pair(53,'p'), Pair(54,'-'), Pair(55,'T'), Pair(56,'u'), // SHOP-TUS(Shop-Tus)
            Pair(55,'M'), Pair(59,'e'), Pair(60,'r'), Pair(61,'c'), Pair(62,'a'), Pair(63,'t'), Pair(64,'o'), // SHOP-MERCATOR(Shop-Mercator)
            Pair(36,'-'), Pair(67,'C'), Pair(68,'o'), Pair(69,'m'), Pair(70,'p'), Pair(71,'l'), Pair(72,'e'), // BUILDING-COMPLEX(Building-Complex)
            Pair(49,'r'), // PARK(Park)
            Pair(1,'s'), Pair(79,'e'), Pair(80,'t'), Pair(81,'L'), Pair(82,'o'), Pair(83,'c'), Pair(84,'a'), Pair(85,'t'), Pair(86,'i'), Pair(87,'o'), // SET_LOCATION(setLocation)
            Pair(1,'t'), Pair(91,'r'), Pair(92,'a'), Pair(93,'n'), Pair(94,'s'), Pair(95,'l'), Pair(96,'a'), // TRANSLATE(translate)
            Pair(1,'r'), Pair(100,'o'), Pair(101,'t'), Pair(102,'a'), // ROTATE(rotate)
            Pair(81,'M'), Pair(106,'a'), Pair(107,'r'), Pair(108,'k'), Pair(109,'e'), // SET_MARKER(setMarker)
            Pair(29,'o'), // BOX(Box)
            Pair(1,'L'), Pair(114,'i'), Pair(115,'n'), // LINE(Line)
            Pair(48,'o'), Pair(118,'l'), Pair(119,'y'), Pair(120,'g'), Pair(121,'o'), // POLYGON(Polygon)
            Pair(25,'r'), Pair(124,'c'), Pair(125,'l'), // CIRCLE(Circle)
            Pair(1,'v'), Pair(128,'a'), // VAR(var)
            Pair(1,'f'), Pair(132,'o'), // FOR(for)
            Pair(11,'i'), Pair(135,'n'), // PRINT(print)
            Pair(1,'c'), Pair(138,'a'), Pair(139,'l'), // CALL(call)
            Pair(1,'d'), Pair(142,'i'), Pair(143,'s'), Pair(144,'p'), Pair(145,'l'), Pair(146,'a'), Pair(147,'y'), Pair(148,'M'), Pair(149,'a'), Pair(150,'r'), Pair(151,'k'), Pair(152,'e'), Pair(153,'r') // DISPLAY_MARKERS(displayMarkers)
        )

        //TRANZICIJE

        //eof
        setTransition(1, EOF, 2)
        //skip
        setTransition(1, ' ', 3)


        //schema(SCHEMA)
        setTransition(1,'S', 4)
        setTransition(4,'C', 5)
        setTransition(5,'H', 6)
        setTransition(6,'E', 7)
        setTransition(7,'M', 8)
        setTransition(8,'A', 9)

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

        //ROAD(Road)
        setTransition(1,'R', 37)
        setTransition(37,'o', 38)
        setTransition(38,'a', 39)
        setTransition(39,'d', 40)

        //RAIL(Rail)
        //R dobi od Road
        setTransition(37,'a', 41)
        setTransition(41,'i', 42)
        setTransition(42,'l', 43)

        //AQUA(Aqua)
        setTransition(1,'A', 44)
        setTransition(44,'q', 45)
        setTransition(45,'u', 46)
        setTransition(46,'a', 47)

        //PATH(Path)
        setTransition(1,'P', 48)
        setTransition(48,'a', 49)
        setTransition(49,'t', 50)
        setTransition(50,'h', 51)

        //SHOP-TUS(Shop-Tus)
        //S dobi od SCHEMA
        setTransition(4,'h', 52)
        setTransition(52,'o', 53)
        setTransition(53,'p', 54)
        setTransition(54,'-', 55)
        setTransition(55,'T', 56)
        setTransition(56,'u', 57)
        setTransition(57,'s', 58)

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

        //PARK(Park)
        //Pa dobi od PATH
        setTransition(49,'r', 75)
        setTransition(75,'k', 76)

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

        //ROTATE(rotate)
        setTransition(1,'r', 100)
        setTransition(100,'o', 101)
        setTransition(101,'t', 102)
        setTransition(102,'a', 103)
        setTransition(103,'t', 104)
        setTransition(104,'e', 105)

        //SET_MARKER(setMarker)
        //set dobi od SET_LOCATION
        setTransition(81,'M', 106)
        setTransition(106,'a', 107)
        setTransition(107,'r', 108)
        setTransition(108,'k', 109)
        setTransition(109,'e', 110)
        setTransition(110,'r', 111)

        //BOX(Box)
        //B dobi od BUILDING
        setTransition(29,'o', 112)
        setTransition(112,'x', 113)

        //LINE(Line)
        setTransition(1,'L', 114)
        setTransition(114,'i', 115)
        setTransition(115,'n', 116)
        setTransition(116,'e', 117)

        //POLYGON(Polygon)
        //P dobi od PATH
        setTransition(48,'o', 118)
        setTransition(118,'l', 119)
        setTransition(119,'y', 120)
        setTransition(120,'g', 121)
        setTransition(121,'o', 122)
        setTransition(122,'n', 123)

        //CIRCLE(Circle)
        //Ci dobi od CITY
        setTransition(25,'r', 124)
        setTransition(124,'c', 125)
        setTransition(125,'l', 126)
        setTransition(126,'e', 127)

        //VAR(var)
        setTransition(1,'v', 128)
        setTransition(128,'a', 129)
        setTransition(129,'r', 130)

        //ignore
        //EQUALS(=)
        setTransition(1,'=', 131)

        //FOR(for)
        setTransition(1,'f', 132)
        setTransition(132,'o', 133)
        setTransition(133,'r', 134)

        //PRINT(print)
        //Pr dobi od PROCEDURE

        setTransition(11,'i', 135)
        setTransition(135,'n', 136)
        setTransition(136,'t', 137)

        //CALL(call)
        setTransition(1,'c', 138)
        setTransition(138,'a', 139)
        setTransition(139,'l', 140)
        setTransition(140,'l', 141)

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

        /*
        //ignore
        //VARIABLE
        val excludeCharsVariable = listOf('p', 'S', 'C', 'B', 'R', 'A', 'P', 's', 't', 'r', 'v', 'f', 'c', 'd', 'L')
        for(i in alpha){
            if(i !in excludeCharsVariable){
                setTransition(1, i, 170)
            }
        }
        for(i in alphaNum){
            setTransition(170, i, 170)
        }

         */










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

        //VARIABLE(alpha+alphaNumExtra)
        setSymbol(170, Symbol.VARIABLE)
        //poskrbimo da ce se ustrezne koncajo v vmesnih stanjih so to potem variabli












    }
}