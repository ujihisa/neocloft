(ns neocloft.sneaking-jump
  (:use [neocloft.clojure-plugin :only [defh later sec]])
  (:import [org.bukkit Material]))
(def handler (atom {}))
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
      (.setVelocity player (let [v (.getVelocity player)]
                             (.setY v (+ 1.0 (.getY v)))
                             v)))
    (prn (get @player-sneak-counter player 0))))
