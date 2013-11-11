(ns neocloft.heartbreaking
  (:use [neocloft.clojure-plugin :only [defh]])
  (:import [org.bukkit Material Bukkit]
           [org.bukkit.entity Player LivingEntity Blaze]))
(def handler (atom {}))
(def worlds #{"world"})

(defh player.PlayerRespawnEvent handler [evt player]
  (let [loc (.getSpawnLocation (Bukkit/getWorld "world_nether"))]
    (.teleport player loc)))

(defn -drop-item [loc istack]
  (.dropItemNaturally (.getWorld loc) loc istack))

(defh entity.EntityDeathEvent handler [evt entity]
  (condp instance? entity
    Blaze
    (when (= 0 (rand-int 10))
      (drop-item (.getLocation entity) (ItemStack. Material/OBSIDIAN 1)))
    nil))

; vim: lispwords+=defh,later :
