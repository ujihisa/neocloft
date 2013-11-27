(ns neocloft.sneaking-jump
  (:use [neocloft.clojure-plugin :only [defh later sec]])
  (:require [clojure.string :as s])
  (:import [org.bukkit Material]))
(def handler (atom {}))
(def worlds #{"world" "world_nether" "world_end"})
; vim: lispwords+=defh,later :

(defh player.AsyncPlayerChatEvent handler [evt player]
  (let [new-msg (-> (.getMessage evt)
                  (s/replace #"benri" "便利")
                  (s/replace #"fuben" "不便"))]
    (.setMessage evt new-msg)))
