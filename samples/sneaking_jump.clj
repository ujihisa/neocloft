(ns neocloft.sneaking-jump
  (:use [neocloft.clojure-plugin :only [defh later sec]])
  (:require [neocloft.helper :as helper])
  (:import [org.bukkit Material Sound]
           [org.bukkit.entity Player]))
(def handler (atom {}))
(def worlds #{"world"})

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
      (helper/play-sound (.getLocation player) Sound/BAT_TAKEOFF 0.8 (rand-nth [0.5 0.8 1.2]))
      (.setFallDistance player 0.0)
      (.setVelocity player (let [v (.getVelocity player)]
                             (.setY v (+ 0.9 (.getY v)))
                             v)))))

(def during-knockback (atom #{}))
(def on-ground (atom #{}))
(defh player.PlayerMoveEvent handler [evt player]
  (when (and (.isSneaking player)
             (@on-ground player)
             (not (@during-knockback player))
             (> (.getY (.getTo evt)) (.getY (.getFrom evt)))
             (not (.isOnGround player)))
    (helper/play-sound (.getLocation player) Sound/BAT_TAKEOFF 0.8 0.5)
    (helper/play-sound (.getLocation player) Sound/BAT_TAKEOFF 0.8 1.0)
    (.setFallDistance player 0.0)
    (.setVelocity player (doto (.getVelocity player)
                           (.setY 0.9))))
  (if (.isOnGround player)
    (when-not (@on-ground player) (swap! on-ground conj player))
    (when (@on-ground player) (swap! on-ground disj player))))

; only to give information to move event that if it was triggered as knockback or not.
(defh entity.EntityDamageByEntityEvent handler [evt entity]
  (when (instance? Player entity)
    (swap! during-knockback conj entity)
    (later (sec 1)
      (swap! during-knockback disj entity))))

; vim: lispwords+=defh,later :
