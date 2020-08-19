(ns tsm.core
  (:require [clojure.java.io :refer [writer reader]])
  (:require [tsm.io :as io])
  (:require [tsm.records :as records])
  (:import [java.util PriorityQueue Collection]
           [java.io Writer File]))

(defn- build-heap [^Collection entries]
  (loop [heap (PriorityQueue. (count entries) records/entry-comparator)
         head (first entries)
         tail (next entries)]
    (if (not (nil? head))
      (do
        (.add heap head)
        (recur heap (first tail) (next tail)))
      (do
        heap))))

(defn- poll [^PriorityQueue heap]
  (when-let [entry (.poll heap)]

    (when-let [next-entry (records/next-entry entry)]
      (.add heap next-entry))

    entry))

(defn- heap-merge-files [^PriorityQueue heap
                         ^Writer output-file-writer]
  ; merge-count - debug info
  (loop [last-entry (poll heap)
         entry (poll heap)
         merge-count 1]
    (if (not (nil? last-entry))
      (if (nil? entry)
        (do
          ; last element -> flush
          (.write output-file-writer ^String (records/to-string last-entry)))
        (do
          (if (= 0 (records/entry-comparator last-entry entry))
            (do
              ; elements have the same timestamp -> merge
              (recur
                (records/merge-entries last-entry entry)
                (poll heap)
                (+ merge-count 1)))
            (do
              ; next timestamp found -> write last, last = entry
              ;(println "record merged" merge-count "times")
              (.write output-file-writer ^String (records/to-string last-entry))
              (recur entry (poll heap) 1))))))))

(defn merge-files
  ""
  [^File output-file ^Iterable input-files]

  (let [readers (map reader input-files)
        heap (build-heap (->> readers
                              (map line-seq)
                              (map records/to-entry)))
        output-file-writer (writer output-file)]
    (try

      (heap-merge-files heap output-file-writer)

      (catch Exception e
        (do
          (.printStackTrace e)))
      (finally
        (do
          (io/flush output-file-writer)
          (io/close output-file-writer)
          (io/close-all readers))))))
