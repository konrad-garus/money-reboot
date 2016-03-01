(ns money-reboot.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :refer [split split-lines]]
            [money-reboot.category-index :refer [index]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}
                         {:cells []}))

(defn on-input [value]
  (swap! app-state assoc :cells (map #(split % #"\t") (split-lines value)))
  (js/console.log "plz" (clj->js (:cells @app-state)))
  )

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
