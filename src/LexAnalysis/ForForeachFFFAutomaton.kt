package LexAnalysis

object ForForeachFFFAutomaton: DFA {
    const val ERROR_STATE = 0
    const val EOF = -1
    const val NEWLINE = '\n'.code

    override val states = (1 .. 141).toSet()
    override val alphabet = 0 .. 255
    override val startState = 1
    override val finalStates = setOf(2,3, 9, 18, 19, 20, 21, 22, 23, 27, 28, 36,
        40, 43, 47, 51, 58, 66, 74, 76, 77, 78, 89, 90, 99, 105, 111, 113, 117, 123, 127, 130, 131,
        134, 137, 141)

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

        //LPAREN(()
        setTransition(1,'(', 19)

        //RPAREN ())
        setTransition(1,')', 20)

        //LCURLY({)
        setTransition(1,'{', 21)

        //RCURLY(})
        setTransition(1,'}', 22)

        //COMMA(,)
        setTransition(1,',', 23)

        //CITY(City)
        setTransition(1,'C', 24)
        setTransition(24,'i', 25)
        setTransition(25,'t', 26)
        setTransition(26,'y', 27)

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

        //LANGLE(<)
        setTransition(1,'<', 77)

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







    }
}