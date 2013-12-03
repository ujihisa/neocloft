(ns neocloft-lingr.core
  (:use [neocloft.clojure-plugin :only [defh later sec]]
        [compojure.core :only (defroutes GET POST ANY)]
        #_[clojure.data.json :only (read-json)]
        [ring.adapter.jetty :only (run-jetty)])
  (:require [neocloft.helper :as helper]
            [clojure.string :as s])
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

(defn on-enable [plugin]
  (let [port (-> plugin .getConfig (.getLong "neocloft-lingr.port" 8126))]
    (prn 'starting 'neocloft-lingr :port port)
    (run-jetty routes {:port port :join? false})))
