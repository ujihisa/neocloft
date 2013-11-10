(ns neocloft.chainmail-swordguard
  (:use [neocloft.clojure-plugin :only [defh]])
  (:import [org.bukkit Material]))
(def handler (atom {}))
(def worlds #{"world" "world_nether" "world_end"})
; vim: lispwords+=defh,later :

(defh entity.EntityDamageByEntityEvent handler [evt entity]
  (let [swords #{Material/WOOD_SWORD Material/STONE_SWORD Material/IRON_SWORD
                 Material/GOLD_SWORD Material/DIAMOND_SWORD}]
    (when (and
            (= Material/CHAINMAIL_CHESTPLATE
               (some-> entity .getEquipment .getChestplate .getType))
            (contains? swords (some-> evt .getDamager .getEquipment .getItemInHand)))
      (.setCancelled evt true)
      (prn 'ok))))
