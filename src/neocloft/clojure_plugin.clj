(ns neocloft.clojure-plugin
  (:gen-class
    :name com.github.ujihisa.Neocloft.ClojurePlugin
    :extends org.bukkit.plugin.java.JavaPlugin
    :implements [org.bukkit.event.Listener]
    :exposes-methods {onEnable -onEnable}))

(defn -onEnable [self]
  (-> self
    (.getServer)
    (.getPluginManager)
    (.registerEvent
      org.bukkit.event.player.PlayerJoinEvent
      (com.github.ujihisa.Neocloft.ClojureListner.)
      org.bukkit.event.EventPriority/NORMAL
      (reify org.bukkit.plugin.EventExecutor
        (execute [_ l evt]
          (prn 'player-join-event-2)))
      self)))

(defn -onDisable [self]
  (prn 'clojure-on-disable self))

(defn -onCommand [self sender command label args]
  (prn 'clojure-on-command sender command label args))
