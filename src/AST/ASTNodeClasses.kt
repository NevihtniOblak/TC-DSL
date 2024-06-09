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