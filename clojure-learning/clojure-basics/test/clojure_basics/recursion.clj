(ns clojure-basics.recursion
  (:require [clojure.test :refer :all]
            [clojure-basics.core :refer :all]))

(defn sum-values
  ([vals] (sum-values 0 vals))
  ([total vals]
   (if (empty? vals)
     total
     (sum-values (+ (first vals) total) (rest vals)))))

(defn sum-values-recur
  ([vals] (sum-values 0 vals))
  ([total vals]
   (if (empty? vals)
     total
     (recur (+ (first vals) total) (rest vals)))))

(defn loop-sum [vals]
  (loop [total 0 vals vals]
    (if (empty? vals)
      total
      (recur (+ total (first vals)) (rest vals)))))

(defn reduce-sum [vals]
  (reduce (fn [total val] (+ total val)) 0 vals))

(defn reduce-sum-reduced [vals]
  (reduce + vals))

(defn filter-even [acc next-val]
  (if (even? next-val)
    (conj acc next-val)
    acc))

(defn map-inc [acc next-val]
  (conj acc (inc next-val)))

(defn group-even [acc next-val]
  (let [key (if (even? next-val) :even :odd)]
    (update-in acc [key] #(conj % next-val))))

(deftest test-recursion
  (testing "recursive sum of values"
    (is (= 6 (sum-values [1 2 3]))))
  (testing "tail recursive sum of values"
    (is (= 6 (sum-values-recur [1 2 3]))))
  (testing "loop sum of values"
    (is (= 6 (loop-sum [1 2 3]))))
  (testing "reduce sum of values"
    (is (= 6 (reduce-sum [1 2 3]))))
  (testing "reduce sum reduced of values"
    (is (= 6 (reduce-sum-reduced [1 2 3]))))
  (testing "reduce to filter values"
    (is (= [2 4 6] (reduce filter-even [] [1 2 3 4 5 6]))))
  (testing "filter values"
    (is (= [2 4 6] (filter even? [1 2 3 4 5 6]))))
  (testing "map with reduce"
    (is (= [2 3 4] (reduce map-inc [] [1 2 3]))))
  (testing "map values"
    (is (= [2 3 4] (map inc [1 2 3])))))
