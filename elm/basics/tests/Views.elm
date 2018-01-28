module Views exposing (..)

import Expect
import Test exposing (..)


suite : Test
suite =
    describe "many tests for this project"
        [ describe "some test"
            [ test "new unit test for this project"
                (\_ -> Expect.equal 3 (2 + 1))
            , test "other test" <|
                \_ -> Expect.equal True True
            ]
        ]
