(ns clojure-basics.collections
  (:require [clojure.test :refer :all]
            [clojure-basics.core :refer :all]))

(deftest testng-list
  (testing "test list"
    (is (= [1 2 3] (list 1 2 3))))
  (testing "prepend to list"
    (is (= [1 2 3] (cons 1 [2 3]))))
  (testing "prepend to array with conj"
    (is (= [2 3 1] (conj [2 3] 1))))
  (testing "prepend to list with conj"
    (is (= [1 2 3] (conj (list 2 3) 1))))
  (testing "get fist element of a list"
    (is (= 1 (first (list 1 2 3)))))
  (testing "get last element of list"
    (is (= 3 (last (list 1 2 3)))))
  (testing "get tail of a list"
    (is (= [2 3] (rest (list 1 2 3)))))
  (testing "get arbitrary index"
    (is (= 2 (nth (list 1 2 3) 1))))
  (testing "get arbitrary index that does not exist"
    (is (thrown? IndexOutOfBoundsException (nth [1 2 3] 4))))
  (testing "concat list"
    (is (= [1 2 3 4 5 6] (concat [1 2 3] [4 5 6])))))

(deftest testing-maps
  (testing "array map"
    (is (= {:a 1 :b 2} (array-map :a 1 :b 2))))
  (testing "hash map"
    (is (= {:a 1 :b 2} (hash-map :a 1 :b 2))))
  (testing "add elements to map"
    (is (= {:a 1 :b 2} (assoc {:a 1} :b 2))))
  (testing "change values in map"
    (is (= {:settings {:a "a" :b 2}} (assoc-in {:settings {:a 3 :b 2}} [:settings :a] "a"))))
  (testing "update the value in a map with a function"
    (is (= {:settings {:a 4 :b 2}} (update-in {:settings {:a 3 :b 2}} [:settings :a] inc))))
  (testing "get element from map"
    (is (= 2 (get {:a 2} :a)))
    (is (= 2 (:a {:a 2})))
    (is (= 2 ({:a 2} :a)))))

(deftest testing-sets
  (testing "create a set"
    (is (= #{1 2 3} (hash-set 1 2 3))))
  (testing "add elements to set"
    (is (= #{1 2 3 4} (conj #{1 2 3} 4))))
  (testing "remove element from set"
    (is (= #{2 3} (disj #{1 2 3} 1)))
    (is (= #{1 2} (disj #{1 2 3} 3))))
  (testing "testing if contains value"
    (is (= true (contains? #{1 2 3} 3))))
  (testing "get value"
    (is (= 2 (get #{2 1 3} 2)))
    (is (= nil (get #{2 1 3} 4)))))
