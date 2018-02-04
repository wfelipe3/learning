(ns clojure-solve.core
  (:gen-class))

(defn solve-me-first [x y]
    (+ x y))

(defn get-value []
    (read-line))

(defn to-int [x]
    (Integer/parseInt x))

(defn get-int-value []
    (to-int (get-value)))

(defn -main
  [& args]
  (let [a (get-int-value) b (get-int-value)]
    (println (solve-me-first a b))))
