// Learn more about F# at http://fsharp.org

open System

[<EntryPoint>]
let main argv =
    let a = Console.ReadLine() |> int
    let b = Console.ReadLine() |> int
    a + b |> printfn "%d"
    0 // return an integer exit code
