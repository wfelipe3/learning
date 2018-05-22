module Main exposing (..)

import Array
import Html exposing (text)
import Set


init =
    { question = "why did the chicken cross the road?"
    , answer = "To get to the other side"
    }


view model =
    text
        ("Question: "
            ++ model.question
            ++ " Answer: "
            ++ .answer model
        )


list =
    1 :: [ 2 ] ++ [ 3 ]


transformList list =
    list
        |> List.map (\a -> a + 1)
        |> List.map toString
        |> String.join ", "


set =
    Set.fromList list


main =
    list
        |> transformList
        |> text
