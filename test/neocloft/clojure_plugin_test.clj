(ns neocloft.clojure-plugin-test
  (:require [clojure.test :refer :all]
            [neocloft.clojure-plugin :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(deftest sec-tect
  (testing "converts from second unit to tick unit (a tick is 1/20 sec)"
    (is (= 60 (sec 3.0)))))
