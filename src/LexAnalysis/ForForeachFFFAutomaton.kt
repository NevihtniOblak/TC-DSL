package LexAnalysis

object ForForeachFFFAutomaton: DFA {
    const val ERROR_STATE = 0
    const val EOF = -1
    const val NEWLINE = '\n'.code

    override val states = (1 .. 51).toSet()
    override val alphabet = 0 .. 255
    override val startState = 1
    override val finalStates = setOf(2,3, 9, 18, 19, 20, 21, 22, 23, 27, 28, 36, 40, 43, 47, 51)

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







    }
}