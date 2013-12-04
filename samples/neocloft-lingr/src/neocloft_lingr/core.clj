(ns neocloft-lingr.core
  (:use [neocloft.clojure-plugin :only [defh later sec]]
        [compojure.core :only (defroutes GET POST ANY)]
        #_[clojure.data.json :only (read-json)])
  (:require [neocloft.helper :as helper]
            [clojure.string :as s]
            [ring.adapter.jetty :as jetty]
            [clj-http.client])
  (:import [org.bukkit Material])
  (:gen-class))
(def handler (atom {}))
(def worlds #{"world"})

(defn handle-post [body]
  (prn 'handle-post body))

(defroutes routes
  (let [start-time (java.util.Date.)]
    (GET "/" []
         (str {:homepage "https://github.com/ujihisa/neocloft"
               :from start-time
               :author "ujihisa"})))
  (POST "/" {body :body}
        (s/join "\n" (handle-post body)))
  #_(POST "/dev" {body :body headers :headers}
    (when (#{"64.46.24.16"} (headers "x-forwarded-for"))
      (my-safe-eval (slurp body))
      #_(let [body-parsed (try
                          (read-string (slurp body))
                          (catch RuntimeException e e))]
        (my-safe-eval (get body-parsed "code" nil)))))
  #_(ANY "*" {body :body headers :headers}
    (any-handler body headers)))

(def jetty-server (ref nil))
(def bot-verifier (ref nil))

(defn on-enable [plugin]
  (let [config (.getConfig plugin)
        port (.getLong config "lingr.port" 8126)]
    (dosync
      (ref-set bot-verifier (.getString config "lingr.bot-verifier")))
    (prn 'starting 'neocloft-lingr :port port)
    (dosync
      (ref-set jetty-server
               (jetty/run-jetty routes {:port port :join? false :max-threads 2})))))

(defn on-disable [plugin]
  (some-> @jetty-server .stop))

(defn post [room msg]
  (clj-http.client/post
    "http://lingr.com/api/room/say"
    {:form-params
      {:room room
      :bot 'cloft
      :text (str msg)
      :bot_verifier @bot-verifier} }))

(defn post-in-mcujm [msg]
  (post "mcujm" msg))

(defh player.PlayerJoinEvent handler [evt player]
  (let [pname (.getName player)]
    (post-in-mcujm (format "%s logged in." pname))))

(defh player.PlayerQuitEvent handler [evt player]
  (let [pname (.getName player)]
    (post-in-mcujm (format "%s logged out." pname))))

(defh player.PlayerRespawnEvent handler [evt player]
  (let [pname (.getName player)]
    (post-in-mcujm (format "%s respanwed." pname))))

(defh player.AsyncPlayerChatEvent handler [evt player]
  (let [pname (.getName player)
        message (.getMessage evt)]
    (post-in-mcujm (format "%s: %s" pname message))))

; vim: lispwords+=defh,later :
