(ns money-reboot.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :refer [split split-lines replace]]
            [cljs-time.core :refer []]
            [cljs-time.format :refer [formatter formatters parse unparse]]
            [money-reboot.category-index :refer [index]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}
                         {:cells []}))

(def ddmmyyy (formatter "dd/MM/yyyy"))
(def isodate (formatters :date))

(defn process-row [[item amount date]]
  (let [amount (replace amount #"," ".")
        date (unparse isodate (parse ddmmyyy date))]
    [item amount date]))

(defn on-input [value]
  (let [cells (map #(split % #"\t") (split-lines value))
        cells (map process-row cells)]
    (swap! app-state assoc :cells cells)
    (js/console.log "plz" (clj->js (:cells @app-state)))))

(defn hello-world []
  [:div
    [:h1 (:text @app-state)]
    [:textarea {:rows 15
                :on-change #(-> % .-target .-value on-input)}]
    [:table
     (map (fn [row] [:tr (map (fn [cell] [:td cell]) row)]) (:cells @app-state))
     ]]
  )

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

; TODO: terms in cljx
;(defn guess [index sentence]
;  (let [words (terms sentence)
;        guesses (map index words)]
;    (reverse (sort-by second (apply merge-with + guesses)))
;  ))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
