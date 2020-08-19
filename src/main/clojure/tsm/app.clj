(ns tsm.app
  (:require [clojure.tools.cli :as cli])
  (:require [tsm.core :refer [merge-files]])
  (:require [tsm.io :refer [to-file]])
  (:gen-class))

(defn -main [& args]
  (let [cli-options
        ;; An option with a required argument
        [["-o" "--output FILE" "Output File"
          :parse-fn #(and % (clojure.java.io/file %))
          :validate [#(not (nil? %)) "File doesn't exist"]]
         ]
        parsed-args (cli/parse-opts args cli-options)
        output-file (-> parsed-args :options :output)
        input-files (map to-file (:arguments parsed-args))]

    (merge-files output-file input-files)

    ))
