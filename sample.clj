(ns neocloft.sample
  (:use [neocloft.clojure-plugin :only [defh]]))
(def handler (atom {}))

(defh player.PlayerJoinEvent handler [evt player]
  (prn 'welcome-to-our-server!)
  (prn (.getName player)))

(defh block.BlockBreakEvent handler [evt block]
  (prn 'you-are-breaking)
  (prn (.getType block)))

; vim: lispwords+=defh :
