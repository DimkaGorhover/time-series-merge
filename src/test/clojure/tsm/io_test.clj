(ns tsm.io-test
  (:require [clojure.test :refer :all]
            [tsm.io :as io])
  (:import (java.io Flushable)
           (java.lang AutoCloseable)))

(deftest close-test
  (testing "resource must be closed after close operation \"close\""
    (let [c (atom 0)
          closeable (proxy [AutoCloseable] []
                      (close [] (swap! c inc)))]
      (io/close closeable)
      (is (= @c 1)))))

(deftest close-all-test
  (testing "all resources must be closed after close operation \"close-all\""
    (let [c (atom 0)
          map-fn (fn [_] (proxy [AutoCloseable] []
                           (close [] (swap! c inc))))
          cc (map map-fn (range 0 5))]
      (io/close-all cc)
      (is (= @c 5)))))

(deftest flush-test
  (testing "resource must be closed after close operation \"close\""
    (let [c (atom 0)
          flushable (proxy [Flushable] []
                      (flush [] (swap! c inc)))]
      (io/flush flushable)
      (is (= @c 1)))))
