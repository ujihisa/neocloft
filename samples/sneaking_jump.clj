(ns neocloft.sneaking-jump
  (:use [neocloft.clojure-plugin :only [defh later sec]])
  (:require [neocloft.helper :as helper])
  (:import [org.bukkit Material Sound]))
(def handler (atom {}))
(def worlds #{"world"})
; vim: lispwords+=defh,later :

(def player-sneak-counter (ref {}))

(defh player.PlayerToggleSneakEvent handler [evt player]
  (when (.isSneaking evt)
    (dosync
      (ref-set
        player-sneak-counter
        (assoc @player-sneak-counter player
               (-> (@player-sneak-counter player)
                 (or 0) (inc)))))
    (later (sec 1.5)
      (dosync
        (ref-set
          player-sneak-counter
          (assoc @player-sneak-counter player
                 (-> (@player-sneak-counter player)
                   (or 0) (dec))))))
    (when (= 3 (@player-sneak-counter player))
      (helper/play-sound loc Sound/BAT_TAKEOFF 0.8 (rand-nth [0.5 0.8 1.2]))
      (.setVelocity player (let [v (.getVelocity player)]
                             (.setY v (+ 1.0 (.getY v)))
                             v)))))
