package AST

interface Program {
    fun eval(): String
}

interface Predef {
    fun eval(): String
}

interface Arguments {}

interface City {
    fun eval(): String
}

interface Components {
    fun eval(): String
}

interface Infnames {
    fun eval(): String
}

interface Contnames {
    fun eval(): String
}

interface Ref {
    fun eval(): String
}

interface Tag {
    fun eval(): String
}

interface Render {
    fun eval(): String
}

interface Rendercont {
    fun eval(): String
}

interface Effect {
    fun eval(): Unit
}

interface Commands {
    fun eval(): Unit
}

interface Specs {
    fun eval(): String
}

interface Polyargs {}

interface Stmts {
    fun eval(): Unit
}

interface Constructnames {
    fun eval(): String
}


interface Exp {

    fun eval(environment: MutableMap<String, Value>): Value
}

interface Data {
    fun eval(environment: MutableMap<String, Value>): Value
}

interface Listitems {
    fun eval(environment: MutableMap<String, Value>): MutableList<String>
}

class Start(val predef: Predef, val city: City) : Program {}

// Predef
class SeqPredef(val predef: Predef, val predef1: Predef) : Predef {}
class Procedure(val arguments: Arguments, val components: Components) : Predef {}
class Schema(val infrastructure: Infrastructure) : Predef {}
class EndPredef : Predef {}

// Arguments

class SeqArguments(val arguments: Arguments, val arguments1: Arguments) : Arguments {}
class ArgumentsExp(val exp: Exp) : Arguments {}
class EndArguments : Arguments {}

// City
class CityComponents(val components: Components) : City {}

// Components
class SeqComponents(val components: Components, val components1: Components) : Components {}
class Infrastructure(val infnames: Infnames, val ref: Ref, val tag: Tag, val render: Render, val effect: Effect) : Components {}
class Containers(val contnames: Contnames, val ref: Ref, val tag: Tag, val rendercont: Rendercont, val effect: Effect) : Components {}
class Statements(val stmts: Stmts) : Components {}
class Specifications(val specs: Specs) : Components {}
class EndComponents : Components {}

// Infnames
class Building() : Infnames {}
class Road() : Infnames {}
class Rail() : Infnames {}
class Aqua() : Infnames {}
class Path() : Infnames {}
class ShopTus() : Infnames {}
class ShopMercator() : Infnames {}

// Contnames
class BuildingComplex() : Contnames {}
class Park() : Contnames {}

// Ref
class Reffrence(val exp: Exp) : Ref {}
class EndRef : Ref {}

// Tag
class TagExp(val exp: Exp) : Tag {}
class EndTag : Tag {}

// Render
class SeqRender(val render: Render, val render1: Render) : Render {}
class RenderStmts(val stmts: Stmts) : Render {}
class RenderSpecs(val specs: Specs) : Render {}
class EndRender : Render {}

// Rendercont

class SeqRendercont(val rendercont: Rendercont, val rendercont1: Rendercont) : Rendercont {}
class RenderContStmts(val stmts: Stmts) : Rendercont {}
class RenderContSpecs(val specs: Specs) : Rendercont {}
class RenderContInfra(val infrastructure: Infrastructure) : Rendercont {}
class EndRendercont : Rendercont {}

// Effect
class SeqEffect(val effect: Effect, val effect1: Effect) : Effect {}
class EffectSmts(val stmts: Stmts) : Effect {}
class EffectCommands(val commands: Commands) : Effect {}
class EndEffect : Effect {}

// Commands
class SetLocation(val exp: Exp) : Commands {}
class Translate(val exp1: Exp, val exp2: Exp) : Commands {}
class Rotate(val exp: Exp) : Commands {}
class SetMarker(val exp: Exp) : Commands {}

// Specs
class Box(val ref: Ref, val tag: Tag, val exp1: Exp, val exp2: Exp, val effect: Effect) : Specs {}
class Line(val ref: Ref, val tag: Tag, val exp1: Exp, val exp2: Exp, val exp3: Exp, val exp4: Exp, val effect: Effect) : Specs {}
class Polygon(val ref: Ref, val tag: Tag, val polyargs: Polyargs, val effect: Effect) : Specs {}
class Circle(val ref: Ref, val tag: Tag, val exp1: Exp, val exp2: Exp, val effect: Effect) : Specs {}
class CircleLine(val ref: Ref, val tag: Tag, val exp1: Exp, val exp2: Exp, val exp3: Exp, val effect: Effect) : Specs {}

// Polyargs
class SeqPolyargs(val polyargs: Polyargs, val polyargs1: Polyargs) : Polyargs {}
class PolyargsExp(val exp: Exp) : Polyargs {}
class EndPolyargs : Polyargs {}

// Stmts
class Define(val variable: String, val data: Data) : Stmts {}
class Assign(val variable: String, val exp: Data) : Stmts {}
class Forloop(val variable: String, val exp1: Exp, val exp2: Exp, val components: Components) : Stmts {}
class Print(val exp: Exp) : Stmts {}
class Call(val variable: String, val arguments: Arguments) : Stmts {}
class DisplayMarkers(val exp1: Exp, val exp2: Exp, val constructnames: Constructnames) : Stmts {}
class ListItemAssign(val variable: String, val exp1: Exp, val exp2: Exp) : Stmts {}

// Constructnames
class InfName(val infnames: Infnames) : Constructnames {}
class ContName(val contnames: Contnames) : Constructnames {}

// Exp
class Plus(val exp1: Exp, val exp2: Exp) : Exp {

    override fun eval(environment: MutableMap<String, Value>): Value {

        val value1 = exp1.eval(environment)
        val value2 = exp2.eval(environment)
        if(!(value1.type == Type.REAL && value2.type == Type.REAL)){
            throw Exception("Type mismatch in Plus operation")
        }
        var res = value1.value[0].toDouble() + value2.value[0].toDouble()

        return Value(Type.REAL, mutableListOf(res.toString()))
    }
}
class Minus(val exp1: Exp, val exp2: Exp) : Exp {

    override fun eval(environment: MutableMap<String, Value>): Value {

        val value1 = exp1.eval(environment)
        val value2 = exp2.eval(environment)
        if(!(value1.type == Type.REAL && value2.type == Type.REAL)){
            throw Exception("Type mismatch in Minus operation")
        }
        var res = value1.value[0].toDouble() - value2.value[0].toDouble()

        return Value(Type.REAL, mutableListOf(res.toString()))
    }
}
class Times(val exp1: Exp, val exp2: Exp) : Exp {
    override fun eval(environment: MutableMap<String, Value>): Value {

        val value1 = exp1.eval(environment)
        val value2 = exp2.eval(environment)
        if(!(value1.type == Type.REAL && value2.type == Type.REAL)){
            throw Exception("Type mismatch in Times operation")
        }
        var res = value1.value[0].toDouble() * value2.value[0].toDouble()

        return Value(Type.REAL, mutableListOf(res.toString()))
    }
}
class Divides(val exp1: Exp, val exp2: Exp) : Exp {
    override fun eval(environment: MutableMap<String, Value>): Value {

        val value1 = exp1.eval(environment)
        val value2 = exp2.eval(environment)
        if(!(value1.type == Type.REAL && value2.type == Type.REAL)){
            throw Exception("Type mismatch in Divides operation")
        }
        var res = value1.value[0].toDouble() / value2.value[0].toDouble()

        return Value(Type.REAL, mutableListOf(res.toString()))
    }
}
class IntegerDivides(val exp1: Exp, val exp2: Exp) : Exp {

    override fun eval(environment: MutableMap<String, Value>): Value {

        val value1 = exp1.eval(environment)
        val value2 = exp2.eval(environment)
        if(!(value1.type == Type.REAL && value2.type == Type.REAL)){
            throw Exception("Type mismatch in IntegerDivides operation")
        }
        var res = value1.value[0].toInt() / value2.value[0].toInt()

        return Value(Type.REAL, mutableListOf(res.toString()))
    }
}
class Pow(val exp1: Exp, val exp2: Exp) : Exp {

    override fun eval(environment: MutableMap<String, Value>): Value {

        val value1 = exp1.eval(environment)
        val value2 = exp2.eval(environment)
        if(!(value1.type == Type.REAL && value2.type == Type.REAL)){
            throw Exception("Type mismatch in Pow operation")
        }
        var res = Math.pow(value1.value[0].toDouble(), value2.value[0].toDouble())
        return Value(Type.REAL, mutableListOf(res.toString()))
    }

}
class UnaryPlus(val exp: Exp) : Exp {

    override fun eval(environment: MutableMap<String, Value>): Value {

        val value = exp.eval(environment)
        if(!(value.type == Type.REAL)){
            throw Exception("Type mismatch in UnaryPlus operation")
        }
        var res = value.value[0].toDouble()

        return Value(Type.REAL, mutableListOf(res.toString()))
    }

}
class UnaryMinus(val exp: Exp) : Exp {

    override fun eval(environment: MutableMap<String, Value>): Value {

        val value = exp.eval(environment)
        if(!(value.type == Type.REAL)){
            throw Exception("Type mismatch in UnaryMinus operation")
        }
        var res = value.value[0].toDouble()*-1

        return Value(Type.REAL, mutableListOf(res.toString()))
    }

}
class Real(val double: Double) : Exp {

    override fun eval(environment: MutableMap<String, Value>): Value {
        return Value(Type.REAL, mutableListOf(double.toString()))
    }
}
class Variable(val string: String) : Exp {

    override fun eval(environment: MutableMap<String, Value>): Value {
        return environment[string] ?: throw Exception("Variable $string not found")
    }
}
class Point(val exp1: Exp, val exp2: Exp) : Exp {

    override fun eval(environment: MutableMap<String, Value>): Value {
        val value1 = exp1.eval(environment)
        val value2 = exp2.eval(environment)

        if(!(value1.type == Type.REAL && value2.type == Type.REAL)){
            throw Exception("Type mismatch in Point operation")
        }
        var res = "("+value1.value[0]+","+value2.value[0]+")"
        return Value(Type.POINT, mutableListOf(res))
    }
}
class StringExp(val string: String) : Exp {

    override fun eval(environment: MutableMap<String, Value>): Value {
        return Value(Type.STRING, mutableListOf(string))
    }
}
class ListIndex(val variable: String, val exp: Exp): Exp {

    override fun eval(environment: MutableMap<String, Value>): Value {
        var index = exp.eval(environment)
        var listItem = environment[variable] ?: throw Exception("Listitem at $variable not found")

        if(!(index.type == Type.REAL)){
            throw Exception("Type mismatch in ListIndex operation")
        }
        var resValue = listItem.value[index.value[0].toInt()]
        var resType = listItem.type

        return Value(resType, mutableListOf(resValue))
    }
}

// Data
class ListData(val listitems: Listitems) : Data {
    override fun eval(environment: MutableMap<String, Value>): Value {

        var list = listitems.eval(environment)

        return Value(Type.LIST, list)

    }
}
class ExpData(val exp: Exp) : Data {

    override fun eval(environment: MutableMap<String, Value>): Value {
        return exp.eval(environment)
    }

}

// Listitems
class SeqListItems(val exp: Exp, val listitems: Listitems) : Listitems {
    override fun eval(environment: MutableMap<String, Value>): MutableList<String> {
        var firstItem = exp.eval(environment).value

         firstItem += listitems.eval(environment)

        return firstItem
    }
}
class EndListitems : Listitems {
    override fun eval(environment: MutableMap<String, Value>): MutableList<String> {
        return mutableListOf()
    }
}