(ns clojure-basics.core-test
  (:require [clojure.test :refer :all]
            [clojure-basics.core :refer :all]))

(deftest a-test
  (testing "first test"
    (is (= 1 1))))
