(ns neocloft.chainmail-swordguard
  (:use [neocloft.clojure-plugin :only [defh]])
  (:import [org.bukkit Material]
           [org.bukkit.entity Player LivingEntity]))
(def handler (atom {}))
(def worlds #{"world" "world_nether" "world_end"})

(defn- equipment [entity]
  (condp instance? entity
    Player (.getInventory entity)
    LivingEntity (.getEquipment entity)
    nil))

(defn- debug [msg]
  #_(prn msg))

(defh entity.EntityDamageByEntityEvent handler [evt entity]
  (let [swords #{Material/WOOD_SWORD Material/STONE_SWORD Material/IRON_SWORD
                 Material/GOLD_SWORD Material/DIAMOND_SWORD}
        damager (-> evt .getDamager)]
    (when (and
            (= Material/CHAINMAIL_CHESTPLATE
               (some-> entity equipment .getChestplate .getType))
            (contains? swords (some-> damager equipment .getItemInHand .getType)))
      (debug (format "A %s attacked a %s, but the attacker used sword and the target wore chainmail, no damage occured but knockback for both of them."
                     damager entity))
      (.setDamage evt 0)
      (.damage damager 0 entity))))

; vim: lispwords+=defh,later :
