(ns neocloft.clojure-plugin
  (:gen-class
    :name com.github.ujihisa.Neocloft.ClojurePlugin
    :extends org.bukkit.plugin.java.JavaPlugin
    :implements [org.bukkit.event.Listener]
    :exposes-methods {onEnable -onEnable}))

(def event-table (atom {}))

(defmacro defh [evt-name handler args & body]
  `(swap! ~handler
          assoc
          ~(symbol (str "org.bukkit.event." evt-name))
          (fn ~args ~@body)))

(defn -onEnable [self]
  (clojure.lang.Compiler/loadFile "/tmp/sample.clj")
  (doseq [[k v] (ns-interns 'neocloft.sample)
          :when (= k 'handler)]
    ; deref a var, and deref the underlying atom
    (swap! event-table assoc "sample.clj" @@v))

  (let [pm (-> self (.getServer) (.getPluginManager))
        listener (com.github.ujihisa.Neocloft.ClojureListner.)]
    (doseq [[helper-f types-evt]
            {(fn [^org.bukkit.event.player.PlayerEvent evt] (.getPlayer evt))
             [org.bukkit.event.player.PlayerJoinEvent]
             (fn [^org.bukkit.event.block.BlockEvent evt] (.getBlock evt))
             [org.bukkit.event.block.BlockBreakEvent] }
            type-evt types-evt]
      (.registerEvent
        pm
        type-evt
        listener
        org.bukkit.event.EventPriority/NORMAL
        (reify org.bukkit.plugin.EventExecutor
            (execute [_ l evt]
              (doseq [[script-name handler] @event-table
                      :let [f (handler type-evt)]]
                (f evt (helper-f evt)))))
        self))))

(defn -onDisable [self]
  (prn 'clojure-on-disable self))

(defn -onCommand [self sender command label args]
  (prn 'clojure-on-command sender command label args))
