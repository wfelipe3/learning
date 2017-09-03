# FSharp

This is the main folder for f# learning projects

## Create project

to crate a f# project in mac there are two options

1. Use the command new in dotnet example `dotnet new console -lang f#`
  * To create f# test project use `dotnet new xunit -lang f#`
2. Use yoeam generator for aspnet, example `yo aspnet`, aspnet yoman generator can be installed following instruction from this page https://github.com/OmniSharp/generator-aspnet

## Run projects

These are the commands to run projects in dotnet

1. Downlaod dependencies `dotnet restore`
2. Run project `dotnet run`
3. Run tests 'dotnet test`
4. To add a nugget reference use the command `dotnet add package <package> --version <version>` example `dotnet add package FsUnit.xUnit --version 3.0.0`

#Unit testing

There is a library to create more f# idiomatic tests, the project can be found in http://fsprojects.github.io/FsUnit/

