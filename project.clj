(defproject neocloft "1.0.0-SNAPSHOT"
  :description "TBD"
  :dependencies [[org.bukkit/bukkit "1.6.4-R2.1-SNAPSHOT"]
                 [org.clojure/clojure "1.5.1"]
                 #_[org.jruby/jruby-complete "1.7.6"]
                 [org.clojure/tools.nrepl "0.2.3"]]
  :license {:name "GNU GPL v3+"
            :url "http://www.gnu.org/licenses/gpl-3.0.en.html"}
  :repositories {"org.bukkit"
                 "http://repo.bukkit.org/service/local/repositories/snapshots/content/"}
  :javac-options ["-d" "classes/" "-Xlint:deprecation"]
  :aot :all)
