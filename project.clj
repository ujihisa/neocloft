(defproject neocloft "1.0.0-SNAPSHOT"
  :description "TBD"
  :dependencies [[org.bukkit/bukkit "1.7.2-R0.1-SNAPSHOT"]
                 [org.clojure/clojure "1.5.1"]
                 #_[org.jruby/jruby-complete "1.7.6"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [org.clojure/core.match "0.2.0"]
                 [com.cemerick/pomegranate "0.2.0"]
                 ; This is tricky. just to disable legacy httpclient-4.1.2 for neocloft apps to be able to use buggy clj-http
                 [org.apache.httpcomponents/httpclient "4.3"]]
  :license {:name "GNU GPL v3+"
            :url "http://www.gnu.org/licenses/gpl-3.0.en.html"}
  :repositories {"org.bukkit"
                 "http://repo.bukkit.org/service/local/repositories/snapshots/content/"}
  :javac-options ["-d" "classes/" "-Xlint:deprecation"]
  :aot :all)
