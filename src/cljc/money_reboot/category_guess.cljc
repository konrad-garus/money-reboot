(ns money-reboot.category-guess
(:require [clojure.string :refer [split]]))

(def stopwords #{"do" "w"})

(defn terms [line] (filter #(not (stopwords %)) (split line #"\s+")))
