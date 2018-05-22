module Tests

open System
open Xunit
open FsUnit.Xunit

[<Fact>]
let ``My first unit test in f#`` () =
    1 |> should equal 1
