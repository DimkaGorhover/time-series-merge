(ns tsm.records-test
  (:require [clojure.test :refer :all]
            [tsm.records :as records]))

(deftest parse-to-record-test
  (testing "Test: Parsing String to Record"
    (let [string-record "2020-10-26:-123.5734"
          expected {:string-date "2020-10-26"
                    :year        2020
                    :month       10
                    :day         26
                    :value       -123.5734M}
          actual (records/parse-to-record string-record)]
      (is (= expected actual)))))

(defn- create-line-reader
  ([] (create-line-reader 0))
  ([value]
   (lazy-seq (cons (str "2020-10-26:" value) (create-line-reader (inc value))))))

(deftest to-entry-test
  (testing ""
    (let [line-reader (create-line-reader)
          actual (records/to-entry line-reader)
          expected-record {:string-date "2020-10-26"
                           :year        2020
                           :month       10
                           :day         26
                           :value       0M}]

      (is (= expected-record (:record actual))))))

(deftest next-entry-test
  (testing ""
    (let [line-reader (create-line-reader)]
      (loop [value 0M
             entry (records/to-entry line-reader)]
        (let [record (:record entry)]

          (is (= value (:value record)))

          (if (< value 10M)
            (do
              (recur (inc value) (records/next-entry entry))
              )))))))

(deftest record-comparator-test
  (testing "records comparator must compare properly"

    (let [record1 nil
          record2 nil]
      (is (= 0 (records/record-comparator record1 record2))))

    (let [record1 nil
          record2 {:year 2020 :month 10 :day 26}]
      (is (= 1 (records/record-comparator record1 record2))))

    (let [record1 {:year 2020 :month 10 :day 26}
          record2 nil]
      (is (= -1 (records/record-comparator record1 record2))))

    (let [record1 {:year 2020 :month 10 :day 26}
          record2 {:year 2020 :month 10 :day 26}]
      (is (= 0 (records/record-comparator record1 record2))))

    (let [record1 {:year 2020 :month 10 :day 27}
          record2 {:year 2020 :month 10 :day 26}]
      (is (= 1 (records/record-comparator record1 record2))))

    (let [record1 {:year 2020 :month 10 :day 26}
          record2 {:year 2020 :month 10 :day 27}]
      (is (= -1 (records/record-comparator record1 record2))))

    ))
