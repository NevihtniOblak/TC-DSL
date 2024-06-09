package AST

interface Program {}

interface Predef {}

interface Arguments {}

interface City {}

interface Components {}

interface Infnames {}

interface Contnames {}

interface Ref {}

interface Tag {}

interface Render {}

interface Rendercont {}

interface Effect {}

interface Commands {}

interface Specs {}

interface Polyargs {}

interface Stmts {}

interface Constructnames {}

interface Exp {}

interface Data {}

interface Listitems {}

class Start(val predef: Predef, val city: City) : Program {}

// Predef
class SeqPredef(val predef: Predef, val predef1: Predef) : Predef {}
class Procedure(val arguments: Arguments, val components: Components, val predef: Predef) : Predef {}
class Schema(val infrastructure: Infrastructure, val predef: Predef) : Predef {}
class EndPredef : Predef {}

// Arguments
class ArgumentsExp(val exp: Exp, val arguments: Arguments) : Arguments {}
class EndArguments : Arguments {}

// City
class CityComponents(val components: Components) : City {}

// Components
class Infrastructure(val infnames: Infnames, val ref: Ref, val tag: Tag, val render: Render, val effect: Effect, val components: Components) : Components {}
class Containers(val contnames: Contnames, val ref: Ref, val tag: Tag, val rendercont: Rendercont, val effect: Effect, val components: Components) : Components {}
class Statements(val stmts: Stmts, val components: Components) : Components {}
class Specifications(val specs: Specs, val components: Components) : Components {}
class EndComponents : Components {}

// Infnames
class Building(val string: String) : Infnames {}
class Road(val string: String) : Infnames {}
class Rail(val string: String) : Infnames {}
class Aqua(val string: String) : Infnames {}
class Path(val string: String) : Infnames {}
class ShopTus(val string: String) : Infnames {}
class ShopMercator(val string: String) : Infnames {}

// Contnames
class BuildingComplex(val string: String) : Contnames {}
class Park(val string: String) : Contnames {}

// Ref
class Reffrence(val exp: Exp) : Ref {}
class EndRef : Ref {}

// Tag
class TagExp(val exp: Exp) : Tag {}
class EndTag : Tag {}

// Render
class RenderStmts(val stmts: Stmts, val render: Render) : Render {}
class RenderSpecs(val specs: Specs, val render: Render) : Render {}
class EndRender : Render {}

// Rendercont
class RenderContStmts(val stmts: Stmts, val rendercont: Rendercont) : Rendercont {}
class RenderContSpecs(val specs: Specs, val rendercont: Rendercont) : Rendercont {}
class RenderContInfra(val infrastructure: Infrastructure, val rendercont: Rendercont) : Rendercont {}
class EndRendercont : Rendercont {}

// Effect
class EffectSmts(val stmts: Stmts, val effect: Effect) : Effect {}
class EffectCommands(val commands: Commands, val effect: Effect) : Effect {}
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
class PolyargsExp(val exp1: Exp, val exp2: Exp, val exp3: Exp, val polyargs: Polyargs) : Polyargs {}
class EndPolyargs : Polyargs {}

// Stmts
class Define(val variable: String, val data: Data) : Stmts {}
class Assign(val variable: String, val data: Data) : Stmts {}
class Forloop(val variable: String, val exp1: Exp, val exp2: Exp, val components: Components) : Stmts {}
class Print(val exp: Exp) : Stmts {}
class Call(val variable: String, val arguments: Arguments) : Stmts {}
class DisplayMarkers(val exp1: Exp, val exp2: Exp, val constructnames: Constructnames) : Stmts {}
class ListItemAssign(val variable: String, val exp: Exp, val data: Data) : Stmts {}

// Constructnames
class InfName(val infnames: Infnames) : Constructnames {}
class ContName(val contnames: Contnames) : Constructnames {}

// Exp
class Plus(val exp1: Exp, val exp2: Exp) : Exp {}
class Minus(val exp1: Exp, val exp2: Exp) : Exp {}
class Times(val exp1: Exp, val exp2: Exp) : Exp {}
class Divides(val exp1: Exp, val exp2: Exp) : Exp {}
class IntegerDivides(val exp1: Exp, val exp2: Exp) : Exp {}
class Pow(val exp1: Exp, val exp2: Exp) : Exp {}
class UnaryPlus(val exp: Exp) : Exp {}
class UnaryMinus(val exp: Exp) : Exp {}
class Real(val double: Double) : Exp {}
class Variable(val string: String) : Exp {}
class Point(val exp1: Exp, val exp2: Exp) : Exp {}
class StringExp(val string: String) : Exp {}
class ListExp(val listitems: Listitems) : Exp {}

// Data
class ListData(val listitems: Listitems) : Data {}
class ExpData(val exp: Exp) : Data {}

// Listitems
class ListItems(val exp: Exp, val listitems: Listitems) : Listitems {}
class EndListitems : Listitems {}