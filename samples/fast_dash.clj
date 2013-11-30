(ns neocloft.fast-dash
  (:use [neocloft.clojure-plugin :only [defh later sec]])
  (:import [org.bukkit Material Effect]))
(def handler (atom {}))
(def worlds #{"world"})

(def dash-id-table (atom {}))

(defh player.PlayerToggleSprintEvent handler [evt player]
  (if (and (.isSprinting evt) (not (.getPassenger player)))
    (if (= Material/SAND (-> player .getLocation .clone (.add 0 -1 0) .getBlock .getType))
      (.setCancelled evt true)
      (let [dash-id (rand)]
        (.setWalkSpeed player 0.4)
        (swap! dash-id-table assoc player dash-id)
        (later (sec 4)
          (when (= dash-id (@dash-id-table player))
            (let [world (.getWorld player)
                  loc (.getLocation player)]
              (.playEffect world loc Effect/SMOKE (byte 0)))
            (.setWalkSpeed player 0.6)))))
    (do
      (.setWalkSpeed player 0.2)
      (swap! dash-id-table assoc player nil))))
; vim: lispwords+=defh,later :
