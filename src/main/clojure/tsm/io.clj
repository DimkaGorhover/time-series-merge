(ns tsm.io
  (:refer-clojure :exclude [flush])
  (:import [java.lang AutoCloseable]
           [java.io IOException Flushable]))

(defn to-file [^String file]
  (let [f (clojure.java.io/file file)]
    (if (not (.exists f))
      (throw (RuntimeException. (format "file %s doesn't exist" f))))
    f))

(defn close [closeable]
  (if (and closeable (instance? AutoCloseable closeable))
    (try
      (.close closeable)
      (catch Exception _))))

(defn close-all [^Iterable resources]
  (loop [head (first resources)
         tail (next resources)]
    (if (not (nil? head))
      (do
        (close head)
        (recur (first tail) (next tail))))))

(defn flush [^Flushable flushable]
  (if (and flushable (instance? Flushable flushable))
    (try
      (.flush flushable)
      (catch IOException _))))
