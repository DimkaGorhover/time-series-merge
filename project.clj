(defproject time-series-merge "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  ;@formatter:off
  :source-paths       ["src/main/clojure"]
  :resource-paths     ["src/main/resources"]
  :java-source-paths  ["src/main/java"]
  :test-paths         ["src/test/clojure"]
  ;@formatter:on

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "1.0.194"]]

  :main tsm.app
  :repl-options {:init-ns tsm.core})
