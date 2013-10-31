(ns neocloft.clojure-listener
  (:gen-class
    :name com.github.ujihisa.Neocloft.ClojureListner
    :implements [org.bukkit.event.Listener]
    :methods [[^{org.bukkit.event.EventHandler true} PlayerJoinEvent [org.bukkit.event.player.PlayerJoinEvent] void]]))

(defn -PlayerJoinEvent [_ ^org.bukkit.event.player.PlayerJoinEvent evt]
  (prn 'player-join-event evt))
