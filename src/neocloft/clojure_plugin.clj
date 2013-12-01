(ns neocloft.clojure-plugin
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as s])
  (:require [clojure.tools.nrepl.server :as nrepl.server])
  (:gen-class
    :name io.github.ujihisa.Neocloft.ClojurePlugin
    :extends org.bukkit.plugin.java.JavaPlugin
    :implements [org.bukkit.event.Listener]
    :exposes-methods {onEnable -onEnable}))

(def event-table (atom {}))
(def plugin-obj (ref nil))
(def nrepl-server (ref nil))

; helper for user plugins
(defmacro defh [evt-name handler args & body]
  `(swap! ~handler
          assoc
          ~(symbol (str "org.bukkit.event." evt-name))
          (fn ~args ~@body)))

(defn sec [n]
  (int (* 20 n)))

(defmacro later [tick & exps]
  `(.scheduleSyncDelayedTask
     (org.bukkit.Bukkit/getScheduler)
     @neocloft.clojure-plugin/plugin-obj
     (fn [] ~@exps)
     ~tick))

; private
(defn clj-filename->ns-symbol
  "aaa_bbb.clj as string -> 'neocloft.aaa-bbb as symbol"
  [clj-filename]
  (assert (.endsWith clj-filename ".clj"))
  (let [base-name (-> clj-filename
                    (s/replace #"\.clj" "")
                    (s/replace #"_" "-"))]
    (symbol (format "neocloft.%s" base-name))))

(def map-of-helper-evttypes
  {(fn [^org.bukkit.event.player.PlayerEvent evt] (.getPlayer evt))
    [org.bukkit.event.player.AsyncPlayerChatEvent
      org.bukkit.event.player.AsyncPlayerPreLoginEvent
      org.bukkit.event.player.PlayerAnimationEvent
      org.bukkit.event.player.PlayerBedEnterEvent
      org.bukkit.event.player.PlayerBedLeaveEvent
      org.bukkit.event.player.PlayerBucketEmptyEvent
      org.bukkit.event.player.PlayerBucketFillEvent
      org.bukkit.event.player.PlayerChangedWorldEvent
      org.bukkit.event.player.PlayerChannelEvent
      org.bukkit.event.player.PlayerChatEvent
      org.bukkit.event.player.PlayerChatTabCompleteEvent
      org.bukkit.event.player.PlayerCommandPreprocessEvent
      org.bukkit.event.player.PlayerDropItemEvent
      org.bukkit.event.player.PlayerEditBookEvent
      org.bukkit.event.player.PlayerEggThrowEvent
      org.bukkit.event.player.PlayerExpChangeEvent
      org.bukkit.event.player.PlayerFishEvent
      org.bukkit.event.player.PlayerGameModeChangeEvent
      org.bukkit.event.player.PlayerInteractEntityEvent
      org.bukkit.event.player.PlayerInteractEvent
      org.bukkit.event.player.PlayerInventoryEvent
      org.bukkit.event.player.PlayerItemBreakEvent
      org.bukkit.event.player.PlayerItemConsumeEvent
      org.bukkit.event.player.PlayerItemHeldEvent
      org.bukkit.event.player.PlayerJoinEvent
      org.bukkit.event.player.PlayerKickEvent
      org.bukkit.event.player.PlayerLevelChangeEvent
      org.bukkit.event.player.PlayerLoginEvent
      org.bukkit.event.player.PlayerMoveEvent
      org.bukkit.event.player.PlayerPickupItemEvent
      org.bukkit.event.player.PlayerPortalEvent
      org.bukkit.event.player.PlayerPreLoginEvent
      org.bukkit.event.player.PlayerQuitEvent
      org.bukkit.event.player.PlayerRegisterChannelEvent
      org.bukkit.event.player.PlayerRespawnEvent
      org.bukkit.event.player.PlayerShearEntityEvent
      org.bukkit.event.player.PlayerTeleportEvent
      org.bukkit.event.player.PlayerToggleFlightEvent
      org.bukkit.event.player.PlayerToggleSneakEvent
      org.bukkit.event.player.PlayerToggleSprintEvent
      org.bukkit.event.player.PlayerUnleashEntityEvent
      org.bukkit.event.player.PlayerUnregisterChannelEvent
      org.bukkit.event.player.PlayerVelocityEvent]
    (fn [^org.bukkit.event.entity.EntityEvent evt] (.getEntity evt))
    [org.bukkit.event.entity.CreatureSpawnEvent
      org.bukkit.event.entity.CreeperPowerEvent
      org.bukkit.event.entity.EntityChangeBlockEvent
      org.bukkit.event.entity.EntityCombustByBlockEvent
      org.bukkit.event.entity.EntityCombustByEntityEvent
      org.bukkit.event.entity.EntityCreatePortalEvent
      org.bukkit.event.entity.EntityDamageByBlockEvent
      org.bukkit.event.entity.EntityDamageByEntityEvent
      org.bukkit.event.entity.EntityDeathEvent
      org.bukkit.event.entity.EntityExplodeEvent
      org.bukkit.event.entity.EntityInteractEvent
      org.bukkit.event.entity.EntityPortalEnterEvent
      org.bukkit.event.entity.EntityRegainHealthEvent
      org.bukkit.event.entity.EntityShootBowEvent
      org.bukkit.event.entity.EntityTameEvent
      org.bukkit.event.entity.EntityTargetEvent
      org.bukkit.event.entity.EntityTargetLivingEntityEvent
      org.bukkit.event.entity.EntityTeleportEvent
      org.bukkit.event.entity.EntityPortalEvent
      org.bukkit.event.entity.EntityPortalExitEvent
      org.bukkit.event.entity.EntityUnleashEvent
      org.bukkit.event.entity.ExplosionPrimeEvent
      org.bukkit.event.entity.FoodLevelChangeEvent
      org.bukkit.event.entity.HorseJumpEvent
      org.bukkit.event.entity.ItemDespawnEvent
      org.bukkit.event.entity.ItemSpawnEvent
      org.bukkit.event.entity.PigZapEvent
      org.bukkit.event.entity.ExpBottleEvent
      org.bukkit.event.entity.PotionSplashEvent
      org.bukkit.event.entity.ProjectileLaunchEvent
      org.bukkit.event.entity.SheepDyeWoolEvent
      org.bukkit.event.entity.SheepRegrowWoolEvent
      org.bukkit.event.entity.SlimeSplitEvent
      ]
    (fn [^org.bukkit.event.block.BlockEvent evt] (.getBlock evt))
    [org.bukkit.event.block.BlockBreakEvent
      org.bukkit.event.block.BlockBurnEvent
      org.bukkit.event.block.BlockCanBuildEvent
      org.bukkit.event.block.BlockDamageEvent
      org.bukkit.event.block.BlockDispenseEvent
      org.bukkit.event.block.BlockExpEvent
      org.bukkit.event.block.BlockFadeEvent
      org.bukkit.event.block.BlockFormEvent
      org.bukkit.event.block.BlockFromToEvent
      org.bukkit.event.block.BlockGrowEvent
      org.bukkit.event.block.BlockIgniteEvent
      org.bukkit.event.block.BlockPhysicsEvent
      org.bukkit.event.block.BlockPistonExtendEvent
      org.bukkit.event.block.BlockPistonRetractEvent
      org.bukkit.event.block.BlockPlaceEvent
      org.bukkit.event.block.BlockRedstoneEvent
      org.bukkit.event.block.BlockSpreadEvent
      org.bukkit.event.block.EntityBlockFormEvent
      org.bukkit.event.block.LeavesDecayEvent
      org.bukkit.event.block.NotePlayEvent
      org.bukkit.event.block.SignChangeEvent]})

(defn -onEnable [self]
  (dosync
    (ref-set plugin-obj self)
    (let [port (-> self .getConfig (.getLong "nrepl-port" 7888))]
      (prn 'debug 'nrepl-port port)
      (ref-set nrepl-server
               (nrepl.server/start-server :port 7888))))
  (doseq [file (file-seq (io/file (.getDataFolder self)))
          :when (.endsWith (.getName file) ".clj")]
    (clojure.lang.Compiler/loadFile (.getAbsolutePath file))
    (let [hashmap (ns-interns (clj-filename->ns-symbol (.getName file)))]
      (if-let [handler (hashmap 'handler)]
        (if-let [worlds (hashmap 'worlds)]
          ; @@ for (1) deref a var, and (2) deref the underlying atom
          (swap! event-table assoc (.getName file) [@worlds @@handler])
          (prn (format "skipping %s due to its missing worlds." (.getAbsolutePath file))))
        (prn (format "skipping %s due to its missing handler." (.getAbsolutePath file))))))

  (let [pm (-> self (.getServer) (.getPluginManager))]
    (doseq [[helper-f types-evt] map-of-helper-evttypes
            type-evt types-evt]
      (try
        (.registerEvent
          pm type-evt self
          org.bukkit.event.EventPriority/NORMAL
          (reify org.bukkit.plugin.EventExecutor
            (execute [_ l evt]
              ; this shouldn't be required though.
              (when-not (contains? (parents type-evt) (class evt))
                (doseq [[script-name [worlds handler]] @event-table
                        :let [f (handler type-evt)]
                        :when f]
                  (let [obj (helper-f evt)]
                    (when (contains? worlds (.getName (.getWorld obj)))
                      (f evt obj)))))))
          self)
        (catch org.bukkit.plugin.IllegalPluginAccessException e
          (prn 'ignoring type-evt))))))

(defn -onDisable [self]
  (nrepl.server/stop-server @nrepl-server)
  (prn 'clojure-on-disable self))

#_(defn -onCommand [self sender command label args]
  (prn 'clojure-on-command sender command label args))
