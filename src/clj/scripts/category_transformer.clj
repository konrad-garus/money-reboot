(ns scripts.category-transformer
  (:require [clojure.string :refer [split split-lines]]))

(def blacklist #{"do"})

(defn terms [line]
  (filter #(not (blacklist %)) (split line #"\s+")))


(defn rank-categories [counts [cat subcat item]]
  (let [words (terms item)
        my-counts (into {} (map (fn [word] [[cat subcat word] 1]) words))]
    (merge-with + my-counts counts)))

; input = filename
; result = {[cat subcat word] count, [cat subcat word] count, ...}
(defn rank-from-file [filename]
  (let [file (.toLowerCase (slurp filename))
        lines (map #(split % #"\t") (split-lines file))
        lines (filter #(and (= 3 (count %)) (not= "" (% 0))) lines)]
    (reduce rank-categories {} lines)))

; input = {[cat subcat word] count, [cat subcat word] count, ...}
; output = {word [(cat subcat word count) (cat subcat word count)], word [(cat subcat word count) (cat subcat word count)]}
(defn group-on-items [ranked]
  (let [flat (map flatten ranked)]
    (group-by #(nth % 2) flat)))

; {word {[cat subcat] count, [cat subcat] count}
(defn group-on-items-2 [ranked]
  (reduce
    (fn [index [[cat subcat word] count]]
      (let [index-entry (or (index word) {})
            index-entry (assoc index-entry [cat subcat] count)]
            (assoc index word index-entry)))
    {}
    ranked))

(let [ranked (rank-from-file "data/categories.tsv")
      index (group-on-items-2 ranked)]
  (spit "src/cljs/money_reboot/category_index.cljs" (prn-str (list 'ns 'money-reboot.category-index)))
  (spit "src/cljs/money_reboot/category_index.cljs" (prn-str (list 'def 'index index)) :append true))
