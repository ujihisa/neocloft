(ns neocloft.clojure-plugin
  (:gen-class
    :name com.github.ujihisa.Neocloft.ClojurePlugin
    :extends org.bukkit.plugin.java.JavaPlugin
    :implements [org.bukkit.event.Listener]
    :exposes-methods {onEnable -onEnable}))

(def event-table (atom {}))

(defn -onEnable [self]
  (clojure.lang.Compiler/loadFile "/tmp/sample.clj")
  (doseq [[k v] (ns-interns 'neocloft.sample)
          :when (= k 'handler)]
    ; deref a var, and deref the underlying atom
    (swap! event-table assoc "sample.clj" @@v))

  (let [pm (-> self (.getServer) (.getPluginManager))
        listener (com.github.ujihisa.Neocloft.ClojureListner.)]
    (.registerEvent
      pm
      org.bukkit.event.player.PlayerJoinEvent
      listener
      org.bukkit.event.EventPriority/NORMAL
      (reify org.bukkit.plugin.EventExecutor
        (execute [_ l evt]
          (doseq [[script-name handler] @event-table
                  :let [f (handler org.bukkit.event.player.PlayerJoinEvent)]]
            (f evt (.getPlayer evt)))))
      self)
    (.registerEvent
      pm
      org.bukkit.event.block.BlockBreakEvent
      listener
      org.bukkit.event.EventPriority/NORMAL
      (reify org.bukkit.plugin.EventExecutor
        (execute [_ l evt]
          (doseq [[script-name handler] @event-table
                  :let [f (handler org.bukkit.event.block.BlockBreakEvent)]]
            (f evt (.getBlock evt)))))
      self)))

(defn -onDisable [self]
  (prn 'clojure-on-disable self))

(defn -onCommand [self sender command label args]
  (prn 'clojure-on-command sender command label args))
