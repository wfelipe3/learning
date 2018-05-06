// Learn more about F# at http://fsharp.org

open System

let inputPoints =
    let values = Console.ReadLine() 
    values.Split ' ' |> Array.map int 

[<EntryPoint>]
let main argv =
    let p1 = Console.ReadLine()
    let p2 = Console.ReadLine()
    printf "%s" p1
    printf "%s" p2
    0 // return an integer exit code
