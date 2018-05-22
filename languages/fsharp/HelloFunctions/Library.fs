namespace HelloFunctions

open System
open Microsoft.AspNetCore.Mvc
open Microsoft.AspNetCore.Http
open Microsoft.Azure.WebJobs.Host
open Microsoft.Azure.WebJobs
open FSharp.Azure.Storage.Table
open Microsoft.WindowsAzure.Storage
open Microsoft.WindowsAzure.Storage.Table

module Say =

    let storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=ba2c63fad8624230858b;AccountKey=iUCOW69UnjwZoctocdWR2WE9DTU1qNHq8F8E15StkZetwR69Gsd5N63aXniN5DetV7jhC76GoGx3Z9zy6gF5vg==;EndpointSuffix=core.windows.net"

    let storageAccount = CloudStorageAccount.Parse storageConnectionString
    let tableClient = storageAccount.CreateCloudTableClient()

    type User = 
        {
            [<PartitionKey>] LastName: string
            [<RowKey>] Name: string
            Id: int
        }
       
    let inUserTable user = inTable tableClient "Users" user

    let run(req: HttpRequest, log: TraceWriter) = 
        log.Info("F# Http trigger function processed a request.")
        let user = {LastName = "Rojas"; Name = "Felipe"; Id = 10}
        let result = user |> Insert |> inUserTable
        ContentResult(Content = "Ho Ho Ho Merry Christmas", ContentType = "text/html")

    let private daysUntil (d: DateTime) = 
        (d - DateTime.Now).TotalDays |> int

    let hello (timer: TimerInfo, log: TraceWriter) =
        let christmas = new DateTime(2017, 12, 25)

        daysUntil christmas
        |> sprintf "%d days until Christmas"
        |> log.Info
