(defproject neocloft-lingr "0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "GNU GPL v3+"
            :url "http://www.gnu.org/licenses/gpl-3.0.en.html"}
  :profiles
  {:dev
   {:repositories {"org.bukkit"
                   "http://repo.bukkit.org/service/local/repositories/snapshots/content/"}
   :dependencies [[org.clojure/clojure "1.5.1"]
                  [org.bukkit/bukkit "1.7.2-R0.3-SNAPSHOT"]
                  [neocloft "1.0.0-SNAPSHOT"]]}}
  :dependencies [[compojure "1.1.6"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [clj-http "0.7.7"]]
  :aot :all)
