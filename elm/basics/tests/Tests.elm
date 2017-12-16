module Tests exposing (..)

import Test exposing (..)
import Expect


-- Check out http://package.elm-lang.org/packages/elm-community/elm-test/latest to learn more about testing in Elm!

all : Test
all = 
    describe "language basics test suite"
       [ describe "first describe"
            [
                test "first test" <|
                \_ -> Expect.equal True True
                , test "invoke test function" (\() -> Expect.equal True True)
            ]
        , describe "String"
            [
                test "Simple string " <|
                    \_ -> Expect.equal "Hello world" "Hello world"
                , test "String concat" <|
                    \_ -> Expect.equal "hello world" ("hello " ++  "world")
            ]
        , describe "math" 
            [
                test "math operations" <|
                    \_ -> Expect.equal 14 (2 + 3 * 4)
                , test "floating point division" <|
                    \_ -> Expect.equal 4.5 (9 / 2)
                , test "int division" <|
                    \_ -> Expect.equal 4 (9//2)
            ]
        , describe "functions"    
            [
                test "function invokation" <| 
                    \_ -> (isNegative 10) |> Expect.equal False
            ]
        , describe "if"
            [
                test "if expression" <|
                    \_ -> (if True then "hello" else "world") |> Expect.equal "hello"
                , test "if in functions" <| 
                    \_ -> overNineThousand 10000 |> Expect.equal "It is over nine thousand"
            ]
        , describe "Lists"
            [
                test "create list" <|
                    \_ -> ["Alice", "bob", "chuck"] |> Expect.equal ["Alice", "bob", "chuck"]
                , test "is empty" <|
                    \_ -> Expect.equal True (List.isEmpty [])
                , test "list lenght" <| 
                    \_ -> List.length [1, 2, 3] |> Expect.equal 3
                        
            ]
       ] 

isNegative : Int -> Bool
isNegative x = x < 0

overNineThousand : Int -> String
overNineThousand x = if x > 9000 then "It is over nine thousand" else "meeehh"
