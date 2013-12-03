(ns neocloft-lingr.core
  (:use [neocloft.clojure-plugin :only [defh later sec]])
  (:require [neocloft.helper :as helper])
  (:import [org.bukkit Material]))
(def handler (atom {}))
(def worlds #{"world"})

(defn -main []
  (prn 'ok))
