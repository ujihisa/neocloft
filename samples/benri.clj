(ns neocloft.benri
  (:use [neocloft.clojure-plugin :only [defh later sec]])
  (:require [clojure.string :as s])
  (:import [org.bukkit Material]))
(def handler (atom {}))
(def worlds #{"world" "world_nether" "world_end"})
; vim: lispwords+=defh,later :

(defh player.AsyncPlayerChatEvent handler [evt player]
  (let [new-msg (-> (.getMessage evt)
                  (s/replace #"benri" "便利")
                  (s/replace #"fuben" "不便")
                  (s/replace #"wa-i" "わーい[^。^]")
                  (s/replace #"dropper|ドロッパ" "泥(・ω・)ﾉ■ ｯﾊﾟ")
                  (s/replace #"hopper|ホッパ" "穂(・ω・)ﾉ■ ｯﾊﾟ")
                  (s/replace #"kiken" "危険")
                  (s/replace #"anzen" "安全")
                  (s/replace #"wkwk" "((o(´∀｀)o))ﾜｸﾜｸ")
                  (s/replace #"unko" "unko大量生産!ブリブリo(-\"-;)o⌒ξ~ξ~ξ~ξ~ξ~ξ~ξ~ξ~")
                  (s/replace #"dks" "溺((o(´o｀)o))死")
                  (s/replace #"tkm" "匠"))]
    (.setMessage evt new-msg)))
