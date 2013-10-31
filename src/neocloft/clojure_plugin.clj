(ns neocloft.clojure-plugin
  (:gen-class
    :name com.github.ujihisa.Neocloft.ClojurePlugin
    :extends org.bukkit.plugin.java.JavaPlugin
    :implements [org.bukkit.event.Listener]
    :exposes-methods {onEnable -onEnable}))

(defn -onEnable [self]
  (prn 'clojure-on-enable)
  (let [x (-> self (.getServer) (.getPluginManager))]
    (prn 'x x)
    (.registerEvents x (com.github.ujihisa.Neocloft.ClojureListner.) self)))

(defn -onDisable [self]
  (prn 'clojure-on-disable))

(defn -onCommand [self sender command label args]
  (prn 'clojure-on-command sender command label args))
