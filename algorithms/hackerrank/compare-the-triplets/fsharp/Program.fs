// Learn more about F# at http://fsharp.org

open System

let inputPoints =
    let values = Console.ReadLine() 
    values.Split ' ' |> Array.map int 

[<EntryPoint>]
let main argv =
    printf "%A" inputPoints
    printf "%A" inputPoints
    0 // return an integer exit code
