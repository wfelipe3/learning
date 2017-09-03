module Rpn

open System
open Xunit
open FsUnit.Xunit

let evalRpnExpr (s: string) =
    let solve items current =
        match (current, items) with
        | "+", y::x::t -> (x + y)::t
        | "-", y::x::t -> (x - y)::t
        | "*", y::x::t -> (x * y)::t
        | "/", y::x::t -> (x / y)::t
        | _ -> (float current)::items
    (s.Split(' ') |> Seq.fold solve []).Head

[<Fact>]
let ``test rpn with two values``() =
    "1 2 +" |> evalRpnExpr |> should equal 3.0
