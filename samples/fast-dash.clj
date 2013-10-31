(ns neocloft.samples.fast-dash
  (:use [neocloft.clojure-plugin :only [defh]])
  (:import [org.bukkit Material]))
(def handler (atom {}))

(defh player.PlayerToggleSprintEvent handler [evt player]
  (if (and (.isSprinting evt) (not (.getPassenger player)))
    (if (= Material/SAND (-> player .getLocation .clone (.add 0 -1 0) .getBlock .getType))
      (.setCancelled evt true)
      (.setWalkSpeed player 0.4))
    (.setWalkSpeed player 0.2)))

; vim: lispwords+=defh :
