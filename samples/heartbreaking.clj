(ns neocloft.heartbreaking
  (:use [neocloft.clojure-plugin :only [defh]])
  (:import [org.bukkit Material]
           [org.bukkit.entity Player LivingEntity]))
(def handler (atom {}))
(def worlds #{"world"})
; vim: lispwords+=defh,later :

(defh player.PlayerRespawnEvent handler [evt player]
  (let [loc (.getSpawnLocation (Bukkit/getWorld "world_nether"))]
    (.teleport player loc)))
