(defproject web-app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [compojure "1.6.2"]
                 [ring/ring-defaults "0.3.3"]
                 [ring/ring-json "0.5.1"]
                 [org.xerial/sqlite-jdbc "3.23.1"]
                 [org.clojure/java.jdbc "0.7.12"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler web-app.core/app}
  :repl-options {:init-ns web-app.core})
