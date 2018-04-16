(ns clojure-basics.core-test
  (:require [clojure.test :refer :all]
            [clojure-basics.core :refer :all]))

(deftest fibonacci
  (testing "several values"
    (is (= 8 (fib 6)))))


(deftest a-test
  (testing "first test"
    (is (= 1 1))))
