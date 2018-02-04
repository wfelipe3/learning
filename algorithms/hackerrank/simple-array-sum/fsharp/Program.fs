// Learn more about F# at http://fsharp.org

open System

let getSize =
    Console.ReadLine() |> int

let getValues =
    let numbers  = Console.ReadLine()
    numbers.Split ' ' |> Array.map int
        

[<EntryPoint>]
let main argv =
    let size = getSize
    getValues |> Array.sum |> printfn "%d"
    0 // return an integer exit code
