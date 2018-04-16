(ns clojure-solve.core
  (:gen-class))

(require '[clojure.string :as str])

(defn get-values 
  [] (str/split (read-line) #"\s+"))

(defn to-int
  [value] (Integer/parseInt value))

(defn int-values 
  [values] (vec (map to-int values)))

(defn to-point 
  [x y] (if (> x  y)
            [1 0]
            (if (> y x)
              [0 1]
              [0 0])))

(defn get-points 
  [x y] (let [
    range3 (take 3 (range)) 
  ]
  (vec (map #(to-point (nth x %1) (nth y %1)) range3))))

(defn final-score 
  [p] (reduce #(do [(+ (nth %1 0)(nth %2 0)) (+ (nth %1 1)(nth %2 1))]) p))

(defn -main
  [& args]
  (let [points (final-score (get-points (int-values (get-values)) (int-values (get-values))))]
      (print (nth points 0)(nth points 1))))
