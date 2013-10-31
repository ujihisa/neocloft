(ns neocloft.samples.sneaking-jump
  (:use [neocloft.clojure-plugin :only [defh]])
  (:import [org.bukkit Material]))
(def handler (atom {}))
; vim: lispwords+=defh,later :

(def player-sneak-counter (ref {}))

(defh player.PlayerToggleSneakEvent handler [evt player]
  (when (.isSneaking evt)
    (dosync
      (ref-set
        player-sneak-counter
        (assoc player (inc (get @player-sneak-counter player 0)))))
    (prn (get @player-sneak-counter player 0))))
