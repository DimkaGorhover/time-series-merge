(ns tsm.records)

(defn parse-to-record [^String str]
  (let [string-date (subs str 0 10)
        year (Integer/parseInt (subs str 0 4))
        month (Integer/parseInt (subs str 5 7))
        day (Integer/parseInt (subs str 8 10))
        value (BigDecimal. (subs str 11))]
    {:string-date string-date
     :year        year
     :month       month
     :day         day
     :value       value}))

(defn to-entry [line-reader]
  (if (and
        (not (nil? line-reader))
        (not (empty? line-reader)))
    (let [head (first line-reader)]
      {:record (parse-to-record head)
       :tail   (next line-reader)})))

(defn next-entry [entry]
  (if (not (nil? entry))
    (when-let [{tail :tail} entry]
      (to-entry tail))))

(defmacro nils-last [o1 o2 & body]
  `(do
     (if (nil? ~o1)
       (do
         (if (nil? ~o2) 0 1))
       (if (nil? ~o2)
         (do -1)
         (or (do ~@body) 0)))))

(defn record-comparator [record1 record2]
  (nils-last
    record1 record2
    (some
      #(if (not (= % 0)) %)
      (list
        (compare (:year record1) (:year record2))
        (compare (:month record1) (:month record2))
        (compare (:day record1) (:day record2))))))

(defn entry-comparator [entry1 entry2]
  (nils-last
    entry1 entry2
    (record-comparator (:record entry1) (:record entry2))))

(defn merge-records [record1 record2]
  (let [{^BigDecimal value1 :value :or {value1 BigDecimal/ZERO}} record1
        {^BigDecimal value2 :value :or {value2 BigDecimal/ZERO}} record2]
    {:string-date (:string-date record1)
     :year        (:year record1)
     :month       (:month record1)
     :day         (:day record1)
     :value       (+ value1 value2)}))

(defn merge-entries [entry1 entry2]
  (let [{record1 :record tail :tail} entry1
        {record2 :record} entry2]
    {:record (merge-records record1 record2)
     :tail   tail}))

(defn- record-to-string [record]
  (let [{date :string-date
         val  :value} record]
    (format "%s:%s%n" date val)))

(defn to-string [entry]
  (record-to-string (:record entry)))
