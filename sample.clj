(ns neocloft.sample)

(def handler (atom {}))
(swap! handler assoc org.bukkit.event.player.PlayerJoinEvent
       (fn [evt player]
         (prn 'welcome-to-our-server!)
         (prn (.getName player))))
(swap! handler assoc org.bukkit.event.block.BlockBreakEvent
       (fn [evt block]
         (prn 'you-are-breaking)
         (prn (.getType block))))
