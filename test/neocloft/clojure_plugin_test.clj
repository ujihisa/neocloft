(ns neocloft.clojure-plugin-test
  (:require [clojure.test :refer :all]
            [neocloft.clojure-plugin :refer :all]))

(deftest sec-tect
  (testing "converts from second unit to tick unit (a tick is 1/20 sec)"
    (is (= 60 (sec 3.0)))))

(deftest clj-filename->ns-symbol-test
  (testing "aaa_bbb.clj as string -> 'neocloft.aaa-bbb as symbol"
    (is (= 'neocloft.aaa-bbb
           (clj-filename->ns-symbol "aaa_bbb.clj")))))

(deftest jar-filename->ns-symbol-test
  (testing "jar filename as string -> 'neocloft-aaa-bbb.core as symbol"
    (is (= 'neocloft-lingr.core
           (jar-filename->ns-symbol "neocloft-lingr-0.1-SNAPSHOT-standalone.jar")))))
