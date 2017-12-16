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
            ]
        , describe "Simple String"
            [
                test "Simple string " <|
                    \_ -> Expect.equal "Hello world" "Hello world"
                , test "String concat" <|
                    \_ -> Expect.equal "hello world" ("hello " ++  "world")
            ]
       ] 
