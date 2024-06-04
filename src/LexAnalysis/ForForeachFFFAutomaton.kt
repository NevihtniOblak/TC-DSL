package LexAnalysis

object ForForeachFFFAutomaton: DFA {
    override val states = (1 .. 11).toSet()
    override val alphabet = 0 .. 255
    override val startState = 1
    override val finalStates = setOf(2, 3, 5, 9, 10, 11)

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
        /*
        setTransition(1, 'f', 2)
        setTransition(2, 'f', 3)
        setTransition(3, 'f', 3)
        setTransition(2, 'o', 4)
        setTransition(4, 'r', 5)
        setTransition(5, 'e', 6)
        setTransition(6, 'a', 7)
        setTransition(7, 'c', 8)
        setTransition(8, 'h', 9)
        setTransition(1, ' ', 10)
        setTransition(1, EOF, 11)

        setSymbol(2, LexAnalysis.Symbol.FFF)
        setSymbol(3, LexAnalysis.Symbol.FFF)
        setSymbol(5, LexAnalysis.Symbol.FOR)
        setSymbol(9, LexAnalysis.Symbol.FOREACH)
        setSymbol(11, LexAnalysis.Symbol.EOF)

         */
    }
}