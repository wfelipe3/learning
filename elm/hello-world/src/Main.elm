module Main exposing (..)

import Html exposing (text)


question =
    "why did the chicken cross the road?"


answer =
    "To get to the other side"


view model =
    text model


init =
    "Question: "
        ++ question
        ++ " Answer: "
        ++ answer


sum a b =
    a + b


sumWithOne =
    sum 1


toUpperLambda =
    \str -> String.toUpper str


main =
    init
        ++ " "
        ++ toString (sumWithOne 2)
        |> toUpperLambda
        |> view
