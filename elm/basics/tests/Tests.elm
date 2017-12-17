module Tests exposing (..)

import Tuple exposing (..)
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
                , test "reverse" <| 
                    \_ -> List.reverse [1, 2, 3] |> Expect.equal [3, 2, 1]
                , test "sort" <|
                    \_ -> List.sort [6, 5, 4, 3, 2, 1] |> Expect.equal [1,2,3,4,5,6]
                , test "map" <|
                    \_ -> List.map (\i -> i * 2) [1, 2, 3, 4] |> Expect.equal [2, 4, 6, 8]
                , test "map with pipe" <|
                    \_ -> [1,2,3,4] |> List.map (\i -> i * 2) |> Expect.equal [2, 4, 6, 8]
                , test "flatmap" <|
                    \_ -> [1, 2, 3, 4] 
                        |> List.concatMap (\i -> [1, i]) 
                        |> Expect.equal [1, 1, 1, 2, 1, 3, 1, 4]
            ]
        , describe "tuples"
            [
                test "create tuple" <|
                    \_ -> (True, "test") |> Expect.equal (True, "test")
                , test "get elements from tuple" <|
                    \_ -> Tuple.first (True, "hello") |> Expect.equal True
                , test "pattern match on tuple" <|
                    \_ -> 
                        let 
                            (first, _, _) =  (True, "felipe", "rojas") 
                        in 
                            first |> Expect.equal True 
            ]
        , describe "records"
            [
                test "creat a record" <|
                    \_ -> {x = 1, y = 2} |> Expect.equal {x = 1, y = 2}
                , test "get value from record" <|
                    \_ ->
                        let
                            value = {x = 1, y = 2}
                        in
                           value.x |> Expect.equal 1 
                , test "get value from record with function" <|
                    \_ -> 
                        let
                            value = {x = 1, y = True}
                        in
                            .y value |> Expect.equal True
                , test "map with tuple function" <|
                    \_ ->
                        let
                            value = {x = 1, y = True}
                        in
                            List.map .y [value, value, value] |> Expect.equal [True, True, True]
                , test "change value in record" <|
                    \_ ->
                        let
                            value = {x = 3, y = 4}
                            value2 = {value | y = 5}
                        in
                            .y value2 |> Expect.equal 5

            ]
       ] 

isNegative : Int -> Bool
isNegative x = x < 0

overNineThousand : Int -> String
overNineThousand x = if x > 9000 then "It is over nine thousand" else "meeehh"
