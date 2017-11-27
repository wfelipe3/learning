(ns clojure-basics.core
  (:gen-class))

(def add (fn [x y] (+ x y)))

(defn fib
  ([x] (do (println x)
           (case x
                0 0
                1 1
                (let [value (+ (fib (- x 1)) (fib (- x 2)))]
                  (do (println (str "fib " value))
                      value))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (fib 1)))
