(ns clojure-basics.functions
  (:require [clojure.test :refer :all]
            [clojure-basics.core :refer :all]))

(def hello-with-name (fn [] "hello"))

(defn hello
  ([] (hello "test"))
  ([name] (str "hello" " " name))
  ([name other] (str "hello" " " name " " other)))

(defn other-hello [& args] (str "hello, " (apply str args)))

(defn hello-list
  ([[name title]] (str "hello " name " " title)))

(defn hello-config
  ([config] (str "hello " (:name config))))

(defn hello-config-name
  ([{name :name}] (str "hello name: " name)))

(defn hello-name-with-config
  ([{name :name :as config}] (str "hello name: " config)))


(deftest test-function
  (testing "anonymous function"
    (is (= "hello" ((fn [] "hello"))))
    (is (= "hello" (#(str "hello"))))
    (is (= "hello, supermegatest" (other-hello "super" "mega" "test")))
    (is (= "hello felipe rojas" (hello-list ["felipe" "rojas"])))
    (is (= "hello felipe" (hello-config {:name "felipe"})))
    (is (= "hello name: felipe" (hello-config-name {:name "felipe"})))
    (is (= "hello name: {:name \"felipe\"}" (hello-name-with-config {:name "felipe"}))))
  (testing "function with name"
    (is (= "hello" (hello-with-name))))
  (testing "first function"
    (is (= "hello test" (hello)))
    (is (= "hello felipe" (hello "felipe")))
    (is (= "hello felipe rojas" (hello "felipe" "rojas")))))
