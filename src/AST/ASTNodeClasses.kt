package AST

enum class EnvType {
    VARIABLE, PROCEDURE, SCHEMA
}

fun pointParse(point: Value): Pair<Double, Double> {
    if(point.type != Type.POINT){
        throw Exception("Type mismatch in pointParse function")
    }
    val coords = point.value[0].split(",")
    return Pair(coords[0].toDouble(), coords[1].toDouble())
}

fun tab(level: Int): String {
    return "\t".repeat(level)
}

typealias Environment = MutableMap<EnvType, MutableMap<String, Any>>

interface Program {
    fun eval(environment: Environment): String
}

interface Predef {
    fun eval(environment: Environment): Unit
}


interface Arguments {
    fun eval(env: Environment, index : Int): MutableMap<Int,Value>
}

interface Parameters {
    fun eval(index: Int): MutableMap<Int,String>
}

interface City {
    fun eval(env: Environment, indent: Int): String
}

interface Components {
    fun eval(env: Environment, indent: Int): String
}

interface Infnames {
    fun eval(): String
}

interface Contnames {
    fun eval(): String
}

interface Ref {
    fun eval(env: Environment): String
}

interface Tag {
    fun eval(env: Environment): String
}

interface Render {
    fun eval(env: Environment, indent: Int, parent: MutableList<String>): String
}

interface Rendercont {
    fun eval(env: Environment, indent: Int, parent: MutableList<String>): String
}

interface Effect {
    fun eval(env: Environment): Unit
}

interface Commands {
    fun eval(env: Environment): Unit
}

interface Specs {
    fun eval(env: Environment, indent: Int, parent: MutableList<String>): String
}

interface Polyargs {
    fun eval(env: Environment): MutableList<Pair<Double,Double>>
}

interface Stmts {
    fun eval(env: Environment): Unit
}

interface Constructnames {
    fun eval(): String
}


interface Exp {

    fun eval(env: Environment): Value
}

interface Data {
    fun eval(env: Environment): Value
}

interface Listitems {
    fun eval(env: Environment): MutableList<String>
}

class Start(val predef: Predef, val city: City) : Program {

    override fun eval(environment: Environment): String {
        predef.eval(environment)
        return city.eval(environment, 0)
    }
}

// Predef
class SeqPredef(val predef1: Predef, val predef2: Predef) : Predef {

        override fun eval(environment: Environment) {
            predef1.eval(environment)
            predef2.eval(environment)
        }
}
class Procedure(val name: String, val params: Parameters, val components: Components) : Predef {

    override fun eval(environment: Environment): Unit {
        environment[EnvType.PROCEDURE]?.set(name, this)
    }
}
class Schema(val infrastructure: Infrastructure) : Predef {

    override fun eval(environment: Environment) {
        val tag = infrastructure.tag.eval(environment)
        environment[EnvType.SCHEMA]?.set(tag, this)
    }
}
class EndPredef : Predef {

        override fun eval(environment: Environment) {
        }

}

// Arguments

class SeqParameters(val param1: Parameters, val param2: Parameters) : Parameters {
    override fun eval(index: Int): MutableMap<Int,String>{
        var firstParam = param1.eval(index)
        var restOfParams = param2.eval(index+1)
        firstParam.putAll(restOfParams)
        return firstParam
    }
}
class Parameter(val argName: String) : Parameters {
    override fun eval(index: Int): MutableMap<Int,String> {
        return mutableMapOf(index to argName)
    }
}
class EndParameter : Parameters {
    override fun eval(index: Int): MutableMap<Int,String> {
        return mutableMapOf()
    }
}

// Arguments
class SeqArguments(val arguments1: Arguments, val arguments2: Arguments) : Arguments {

    override fun eval(env: Environment, index: Int): MutableMap<Int,Value>{
        var firstArg = arguments1.eval(env, index)
        var restOfArgs = arguments2.eval(env,index+1)
        firstArg.putAll(restOfArgs)
        return firstArg
    }
}
class Argument(val exp: Exp) : Arguments {
    override fun eval(env: Environment, index: Int): MutableMap<Int,Value> {
        return mutableMapOf(index to exp.eval(env))
    }
}
class EndArgument : Arguments {
    override fun eval(env: Environment, index: Int): MutableMap<Int,Value> {
        return mutableMapOf()
    }
}


// City
class CityComponents(val components: Components) : City {
    override fun eval(env: Environment, indent: Int): String {
        var components = components.eval(env, 0)
        var geoJson = ""
        geoJson += tab(indent) + "{\n"
        geoJson += tab(indent+1) + "\"type\": \"FeatureCollection\",\n"
        geoJson += tab(indent+1) + "\"features\": [\n"
        geoJson += components
        geoJson += tab(indent+1) + "]\n"
        geoJson += tab(indent) + "}\n"

        return geoJson


    }
}

// Components
class SeqComponents(val components1: Components, val components2: Components) : Components {
    override fun eval(env: Environment, indent: Int): String {
        //return components1.eval(env, indent) +",\n"+ components2.eval(env, indent)
        var first = components1.eval(env, indent)
        var following = components2.eval(env, indent)

        if(first != "" && following != ""){
            return first + tab(indent+1) +",\n" + following
        }
        else if(first != ""){
            return first
        }
        else if(following != ""){
            return following
        }
        else{
            return ""
        }
    }
}
class Infrastructure(val infnames: Infnames, val ref: Ref, val tag: Tag, val render: Render, val effect: Effect) : Components {
    override fun eval(env: Environment, indent: Int): String {
        var infname = infnames.eval()
        var ref = ref.eval(env)
        var tag = tag.eval(env)
        var effect = effect.eval(env)
        //return render.eval(env, indent+1)
        var geoJson = ""

        return render.eval(env, indent, mutableListOf(infname, tag))
    }
}
class Containers(val contnames: Contnames, val ref: Ref, val tag: Tag, val rendercont: Rendercont, val effect: Effect) : Components {
    override fun eval(env: Environment, indent: Int): String {
        var contname = contnames.eval()
        var ref = ref.eval(env)
        var tag = tag.eval(env)
        var effect = effect.eval(env)

        return rendercont.eval(env, indent, mutableListOf(contname, tag))
    }
}
class Statements(val stmts: Stmts) : Components {
    override fun eval(env: Environment, indent: Int): String {
        stmts.eval(env)
        return ""
    }
}
class Specifications(val specs: Specs) : Components {
    override fun eval(env: Environment, indent: Int): String {
        return specs.eval(env, indent+1, mutableListOf("",""))
    }
}
class EndComponents : Components {
    override fun eval(env: Environment, indent: Int): String {
        return ""
    }
}

// Infnames
class Building() : Infnames {
    override fun eval(): String {
        return "Building"
    }
}
class Road() : Infnames {
    override fun eval(): String {
        return "Road"
    }
}
class Rail() : Infnames {

    override fun eval(): String {
        return "Rail"
    }
}
class Aqua() : Infnames {
    override fun eval(): String {
        return "Aqua"
    }
}
class Path() : Infnames {

    override fun eval(): String {
        return "Path"
    }
}
class ShopTus() : Infnames {

    override fun eval(): String {
        return "Shop-Tus"
    }
}
class ShopMercator() : Infnames {

    override fun eval(): String {
        return "Shop-Mercator"
    }
}

// Contnames
class BuildingComplex() : Contnames {

    override fun eval(): String {
        return "BuildingComplex"
    }
}
class Park() : Contnames {

    override fun eval(): String {
        return "Park"
    }
}

// Ref
class Reffrence(val exp: Exp) : Ref {

    override fun eval(environment: Environment): String {
        var value = exp.eval(environment)
        if(value.type != Type.STRING){
            throw Exception("Type mismatch in Ref operation")
        }
        return value.value[0]
    }
}
class EndRef : Ref {
    override fun eval(environment: Environment): String {
        return ""
    }
}

// Tag
class TagExp(val exp: Exp) : Tag {
    override fun eval(environment: Environment): String {
        var value = exp.eval(environment)
        if (value.type != Type.STRING) {
            throw Exception("Type mismatch in Ref operation")
        }
        return value.value[0]
    }
}
class EndTag : Tag {
    override fun eval(environment: Environment): String {
        return ""
    }
}

// Render
class SeqRender(val render: Render, val render1: Render) : Render {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        var first = render.eval(env, indent+1, parent)
        var following = render1.eval(env, indent, parent)
        if(first != "" && following != ""){
            return first + tab(indent+1) +",\n" + following
        }
        else if(first != ""){
            return first
        }
        else if(following != ""){
            return following
        }
        else{
            return ""
        }
    }
}
class RenderStmts(val stmts: Stmts) : Render {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        stmts.eval(env)
        return ""
    }
}
class RenderSpecs(val specs: Specs) : Render {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        return specs.eval(env, indent+1, parent)
    }
}
class EndRender : Render {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        return ""
    }
}

// Rendercont

class SeqRendercont(val rendercont: Rendercont, val rendercont1: Rendercont) : Rendercont {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        var first = rendercont.eval(env, indent, parent)
        var following = rendercont1.eval(env, indent, parent)
        if(first != "" && following != ""){
            return first + tab(indent+1) +",\n" + following
        }
        else if(first != ""){
            return first
        }
        else if(following != ""){
            return following
        }
        else{
            return ""
        }
    }
}
class RenderContStmts(val stmts: Stmts) : Rendercont {
    override fun eval(env: Environment, indent: Int,  parent: MutableList<String>): String {
        stmts.eval(env)
        return ""
    }
}
class RenderContSpecs(val specs: Specs) : Rendercont {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        return specs.eval(env, indent+1, parent)

    }
}
class RenderContInfra(val infrastructure: Infrastructure) : Rendercont {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        return infrastructure.eval(env, indent+1)
    }
}
class EndRendercont : Rendercont {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        return ""
    }

}

// Effect
class SeqEffect(val effect: Effect, val effect1: Effect) : Effect {
    override fun eval(env: Environment) {
    }
}
class EffectSmts(val stmts: Stmts) : Effect {
    override fun eval(env: Environment) {
    }
}
class EffectCommands(val commands: Commands) : Effect {
    override fun eval(env: Environment) {
    }
}
class EndEffect : Effect {
    override fun eval(env: Environment) {
    }
}

// Commands
class SetLocation(val exp: Exp) : Commands {
    override fun eval(env: Environment) {
    }
}
class Translate(val exp1: Exp, val exp2: Exp) : Commands {
    override fun eval(env: Environment) {
    }
}
class Rotate(val exp: Exp) : Commands {
    override fun eval(env: Environment) {
    }
}
class SetMarker(val exp: Exp) : Commands {
    override fun eval(env: Environment) {
    }
}

// Specs
class Box(val ref: Ref, val tag: Tag, val exp1: Exp, val exp2: Exp, val effect: Effect) : Specs {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        var tag = tag.eval(env)

        var p1 = exp1.eval(env)
        var p2 = exp2.eval(env)
        if(p1.type != Type.POINT || p2.type != Type.POINT){
            throw Exception("Type mismatch in Box operation")
        }
        var coord1 = pointParse(p1)
        var coord2 = pointParse(p2)

        val (x1, y1) = coord1
        val (x2, y2) = coord2

        val rectangleCoords = listOf(
            listOf(y1, x1),
            listOf(y2, x1),
            listOf(y2, x2),
            listOf(y1, x2),
            listOf(y1, x1) // Repeat the first point to close the polygon
        )

        var geoJson = ""
        geoJson += tab(indent) + "{\n"
        geoJson += tab(indent+1) + "\"type\": \"Feature\",\n"
        geoJson += tab(indent+1) + "\"geometry\":{\n"
        geoJson += tab(indent+2) + "\"type\": \"Polygon\",\n"
        geoJson += tab(indent+2) + "\"coordinates\": [[\n"
        geoJson += tab(indent+3)
        geoJson += rectangleCoords.joinToString(",\n${tab(indent+3)}") { "[${it[0]}, ${it[1]}]" }
        geoJson += "\n"
        geoJson += tab(indent+2) + "]]\n"
        geoJson += tab(indent+1) + "},\n"
        geoJson += tab(indent+1) + "\"properties\": {\n"
        if(tag != ""){
            geoJson += tab(indent+2) + "\"tag\": $tag,\n"
        }
        else{
            geoJson += tab(indent+2) + "\"tag\": \"\",\n"
        }
        if(parent[0]!=""){
            geoJson += tab(indent+2) + "\"Parent type\": \"${parent[0]}\",\n"
        }
        else{
            geoJson += tab(indent+2) + "\"Parent type\": \"\",\n"
        }
        if(parent[1]!=""){
            geoJson += tab(indent+2) + "\"Parent tag\": ${parent[1]}\n"
        }
        else{
            geoJson += tab(indent+2) + "\"Parent tag\": \"\"\n"
        }
        geoJson += tab(indent+1) + "}\n"
        geoJson += tab(indent) + "}\n"


        return geoJson
    }
}
class Line(val ref: Ref, val tag: Tag, val exp1: Exp, val exp2: Exp, val exp3: Exp, val exp4: Exp, val effect: Effect) : Specs {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        var tag = tag.eval(env)
        var p1 = exp1.eval(env)
        var p2 = exp2.eval(env)
        if(p1.type != Type.POINT || p2.type != Type.POINT){
            throw Exception("Type mismatch in Box operation")
        }
        var coord1 = pointParse(p1)
        var coord2 = pointParse(p2)

        val (x1, y1) = coord1
        val (x2, y2) = coord2

        val lineCoords = listOf(
            listOf(y1, x1),
            listOf(y2, x2)
        )

        var geoJson = ""
        geoJson += tab(indent) + "{\n"
        geoJson += tab(indent+1) + "\"type\": \"Feature\",\n"
        geoJson += tab(indent+1) + "\"geometry\":{\n"
        geoJson += tab(indent+2) + "\"type\": \"LineString\",\n"
        geoJson += tab(indent+2) + "\"coordinates\": [\n"
        geoJson += tab(indent+3)
        geoJson += lineCoords.joinToString(",\n${tab(indent+3)}") { "[${it[0]}, ${it[1]}]" }
        geoJson += "\n"
        geoJson += tab(indent+2) + "]\n"
        geoJson += tab(indent+1) + "},\n"
        geoJson += tab(indent+1) + "\"properties\": {\n"

        if(tag != ""){
            geoJson += tab(indent+2) + "\"tag\": $tag,\n"
        }
        else{
            geoJson += tab(indent+2) + "\"tag\": \"\",\n"
        }
        if(parent[0]!=""){
            geoJson += tab(indent+2) + "\"Parent type\": \"${parent[0]}\",\n"
        }
        else{
            geoJson += tab(indent+2) + "\"Parent type\": \"\",\n"
        }
        if(parent[1]!=""){
            geoJson += tab(indent+2) + "\"Parent tag\": ${parent[1]}\n"
        }
        else{
            geoJson += tab(indent+2) + "\"Parent tag\": \"\"\n"
        }


        geoJson += tab(indent+1) + "}\n"
        geoJson += tab(indent) + "}\n"

        return geoJson
    }
}
class Polygon(val ref: Ref, val tag: Tag, val polyargs: Polyargs, val effect: Effect) : Specs {

    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        var tag = tag.eval(env)
        var coordinates = polyargs.eval(env)
        coordinates.add(coordinates[0])

        var geoJson = ""
        geoJson += tab(indent) + "{\n"
        geoJson += tab(indent+1) + "\"type\": \"Feature\",\n"
        geoJson += tab(indent+1) + "\"geometry\":{\n"
        geoJson += tab(indent+2) + "\"type\": \"Polygon\",\n"
        geoJson += tab(indent+2) + "\"coordinates\": [[\n"
        geoJson += tab(indent+3)
        geoJson += coordinates.joinToString(",\n${tab(indent+3)}") { "[${it.second}, ${it.first}]" }
        geoJson += "\n"
        geoJson += tab(indent+2) + "]]\n"
        geoJson += tab(indent+1) + "},\n"
        geoJson += tab(indent+1) + "\"properties\": {\n"
        if(tag != ""){
            geoJson += tab(indent+2) + "\"tag\": $tag,\n"
        }
        else{
            geoJson += tab(indent+2) + "\"tag\": \"\",\n"
        }
        if(parent[0]!=""){
            geoJson += tab(indent+2) + "\"Parent type\": \"${parent[0]}\",\n"
        }
        else{
            geoJson += tab(indent+2) + "\"Parent type\": \"\",\n"
        }
        if(parent[1]!=""){
            geoJson += tab(indent+2) + "\"Parent tag\": ${parent[1]}\n"
        }
        else{
            geoJson += tab(indent+2) + "\"Parent tag\": \"\"\n"
        }
        geoJson += tab(indent+1) + "}\n"
        geoJson += tab(indent) + "}\n"


        return geoJson
    }

}
class Circle(val ref: Ref, val tag: Tag, val exp1: Exp, val exp2: Exp, val effect: Effect) : Specs {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        TODO("Not yet implemented")
        return ""
    }
}
class CircleLine(val ref: Ref, val tag: Tag, val exp1: Exp, val exp2: Exp, val exp3: Exp, val effect: Effect) : Specs {
    override fun eval(env: Environment, indent: Int, parent: MutableList<String>): String {
        TODO("Not yet implemented")
        return ""
    }
}

// Polyargs
class SeqPolyargs(val polyargs: Polyargs, val restOfPolyargs: Polyargs) : Polyargs {
    override fun eval(env: Environment): MutableList<Pair<Double, Double>> {
        var first = polyargs.eval(env)
        var rest = restOfPolyargs.eval(env)
        first.addAll(rest)
        return first
    }
}
class PolyargsExp(val exp: Exp) : Polyargs {

    override fun eval(env: Environment): MutableList<Pair<Double, Double>> {
        var value = exp.eval(env)
        if(value.type != Type.POINT){
            throw Exception("Type mismatch in Polyargs operation")
        }
        var coords = value.value[0].split(",")
        return mutableListOf(Pair(coords[0].toDouble(), coords[1].toDouble()))
    }
}
class EndPolyargs : Polyargs {
    override fun eval(env: Environment): MutableList<Pair<Double, Double>> {
        return mutableListOf()
    }

}

// Stmts
class Define(val variable: String, val data: Data) : Stmts {
    override fun eval( environment: Environment) {
        environment[EnvType.VARIABLE]?.set(variable, data.eval(environment))
    }
}
class Assign(val variable: String, val exp: Data) : Stmts {

    override fun eval(environment: Environment) {
        if(environment[EnvType.VARIABLE]?.get(variable) == null){
            throw Exception("Variable $variable not found")
        }
        environment[EnvType.VARIABLE]?.set(variable, exp.eval(environment))
    }
}
class Forloop(val variable: String, val exp1: Exp, val exp2: Exp, val components: Components) : Stmts {
    override fun eval(environment: Environment) {
        var itertorValue = exp1.eval(environment).value[0].toDouble().toInt()
        var end = exp2.eval(environment).value[0].toDouble().toInt()


        for(i in itertorValue..end){
            environment[EnvType.VARIABLE]?.set(variable, Value(Type.REAL, mutableListOf((i).toString())))

            components.eval(environment, 0)
        }

    }
}
class Print(val exp: Exp) : Stmts {
    override fun eval(environment: Environment) {
        println(exp.eval(environment).value[0])
    }
}
class Call(val variable: String, val args: Arguments) : Stmts {
    override fun eval(env: Environment) {
        var procedure = env[EnvType.PROCEDURE]?.get(variable) as Procedure?: throw Exception("Procedure $variable not found")

        var arguments = args.eval(env, 0)
        var functionBody = procedure.components
        var parameters = procedure.params.eval(0)

        if (arguments.size != parameters.size) {
            throw Exception("Mismatch in size of arguments and parameters")
        }

        val procedureEnv = mutableMapOf<EnvType, MutableMap<String, Any>>()
        procedureEnv[EnvType.VARIABLE] = mutableMapOf()

        for ((key, value) in arguments) {
            val param = parameters[key]
            if (param != null) {
                procedureEnv[EnvType.VARIABLE]?.put(param, value)
            } else {
                throw Exception("Parameter not found for argument $key")
            }
        }

        functionBody.eval(procedureEnv, 0)

    }
}
class DisplayMarkers(val exp1: Exp, val exp2: Exp, val constructnames: Constructnames) : Stmts {
    override fun eval(environment: Environment) {
        var value1 = exp1.eval(environment)
        if(value1.type != Type.POINT){
            throw Exception("Type mismatch in DisplayMarkers operation")
        }
        var value2 = exp2.eval(environment)
        if(value2.type != Type.REAL){
            throw Exception("Type mismatch in DisplayMarkers operation")
        }
        var constructName = constructnames.eval()

        //TODO

    }
}
class ListItemAssign(val variable: String, val exp1: Exp, val exp2: Exp) : Stmts {
    override fun eval(environment: Environment) {
        var index = exp1.eval(environment)
        var listItem = environment[EnvType.VARIABLE]?.get(variable)?: throw Exception("Listitem at $variable not found")
        listItem = listItem as Value

        if(listItem.type != Type.LIST){
            throw Exception("Type mismatch in ListItemAssign operation")
        }

        listItem.value[index.value[0].toDouble().toInt()] = exp2.eval(environment).value[0]
    }

}

// Constructnames
class InfName(val infnames: Infnames) : Constructnames {
    override fun eval(): String {
        return infnames.eval()
    }
}
class ContName(val contnames: Contnames) : Constructnames {
    override fun eval(): String {
        return contnames.eval()
    }
}

// Exp
class Plus(val exp1: Exp, val exp2: Exp) : Exp {

    override fun eval(environment: Environment): Value {

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

    override fun eval(environment: Environment): Value {

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
    override fun eval(environment: Environment): Value {

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
    override fun eval(environment: Environment): Value {

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

    override fun eval(environment: Environment): Value {

        val value1 = exp1.eval(environment)
        val value2 = exp2.eval(environment)
        if(!(value1.type == Type.REAL && value2.type == Type.REAL)){
            throw Exception("Type mismatch in IntegerDivides operation")
        }
        var res = value1.value[0].toDouble().toInt() / value2.value[0].toDouble().toInt()

        return Value(Type.REAL, mutableListOf(res.toString()))
    }
}
class Pow(val exp1: Exp, val exp2: Exp) : Exp {

    override fun eval(environment: Environment): Value {

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

    override fun eval(environment: Environment): Value {

        val value = exp.eval(environment)
        if(!(value.type == Type.REAL)){
            throw Exception("Type mismatch in UnaryPlus operation")
        }
        var res = value.value[0].toDouble()

        return Value(Type.REAL, mutableListOf(res.toString()))
    }

}
class UnaryMinus(val exp: Exp) : Exp {

    override fun eval(environment: Environment): Value {

        val value = exp.eval(environment)
        if(!(value.type == Type.REAL)){
            throw Exception("Type mismatch in UnaryMinus operation")
        }
        var res = value.value[0].toDouble()*-1

        return Value(Type.REAL, mutableListOf(res.toString()))
    }

}
class Real(val double: Double) : Exp {

    override fun eval(environment: Environment): Value {
        return Value(Type.REAL, mutableListOf(double.toString()))
    }
}
class Variable(val string: String) : Exp {

    override fun eval(environment: Environment): Value {
        try {
            return environment[EnvType.VARIABLE]?.get(string) as Value
        } catch (e: Exception) {
            throw Exception("Variable $string not found")
        }
    }
}
class Point(val exp1: Exp, val exp2: Exp) : Exp {

    override fun eval(environment: Environment): Value {
        val value1 = exp1.eval(environment)
        val value2 = exp2.eval(environment)

        if(!(value1.type == Type.REAL && value2.type == Type.REAL)){
            throw Exception("Type mismatch in Point operation")
        }
        var res = value1.value[0]+","+value2.value[0]
        return Value(Type.POINT, mutableListOf(res))
    }
}
class StringExp(val string: String) : Exp {

    override fun eval(environment: Environment): Value {
        return Value(Type.STRING, mutableListOf(string))
    }
}
class ListIndex(val variable: String, val exp: Exp): Exp {

    override fun eval(environment: Environment): Value {
        var index = exp.eval(environment)

        var listItem = environment[EnvType.VARIABLE]?.get(variable) as Value

        if(!(index.type == Type.REAL)){
            throw Exception("Type mismatch in ListIndex operation")
        }

        var resValue = listItem.value[index.value[0].toDouble().toInt()]
        var resType = listItem.type

        return Value(resType, mutableListOf(resValue))
    }
}

// Data
class ListData(val listitems: Listitems) : Data {
    override fun eval(environment:Environment): Value {

        var list = listitems.eval(environment)

        return Value(Type.LIST, list)

    }
}
class ExpData(val exp: Exp) : Data {

    override fun eval(environment: Environment): Value {
        return exp.eval(environment)
    }

}

// Listitems
class SeqListItems(val exp: Exp, val listitems: Listitems) : Listitems {
    override fun eval(environment: Environment): MutableList<String> {
        var firstItem = exp.eval(environment).value

         firstItem += listitems.eval(environment)

        return firstItem
    }
}
class EndListitems : Listitems {
    override fun eval(environment: Environment): MutableList<String> {
        return mutableListOf()
    }
}

