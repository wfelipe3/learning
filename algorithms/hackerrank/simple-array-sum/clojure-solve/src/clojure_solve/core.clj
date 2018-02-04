(ns clojure-solve.core
  (:gen-class))

(require '[clojure.string :as str])

(defn get-value []
  (read-line))

(defn to-int [x]
  (Integer/parseInt x))

(defn to-strings [x]
  (str/split x #" "))

(defn sum [x]
  (reduce + x))

(defn sum-rec 
  ([x](sum-rec x 0))
  ([x a]
    (if (empty? x) 
      a
      (sum-rec 
          (rest x) 
          (+ a (first x))))))

(defn -main
  [& args]
  (let [a (get-value) v (map to-int (to-strings (get-value)))]
    (println (sum-rec v))))
