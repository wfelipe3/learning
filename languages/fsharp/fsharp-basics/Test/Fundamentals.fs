module Fundamentals

open System
open Xunit
open FsUnit.Xunit

[<Fact>]
let ``test for things that do not retuen a value`` ()=
    Console.WriteLine "test" |> should be (null)

[<Fact>]
let ``let bindings`` ()=
    let x = 5
    x |> should equal 5
    let add x y = x + y
    add 1 2 |> should equal 3

[<Literal>]
let Gravity = 9.8

[<Fact>]
let ``a literal is a constant value in c#`` ()=
    Gravity |> should equal Gravity

[<Fact>]
let ``bindings are immutable by default but they can be also mutable`` ()=
    let mutable name = "felipe"
    name <- "rojas"
    name |> should equal "rojas"

[<Fact>]
let ``references are like mutable bindings`` ()=
    let x = ref 0
    x := 10
    ! x |> should equal 10
 
