(ns money-reboot.core
  (:require [reagent.core :as reagent :refer [atom create-element adapt-react-class create-class]]
            [clojure.string :refer [split split-lines replace]]
            [cljs-time.core :refer []]
            [cljs-time.format :refer [formatter formatters parse unparse]]
            [money-reboot.category-index :refer [index]]
            [money-reboot.category-guess :refer [terms]]
            [cljsjs.react.dom]
            ))

(enable-console-print!)

(defonce app-state (atom {:text "Hello world!"}
                         {:cells []}))

(def ddmmyyy (formatter "dd/MM/yyyy"))
(def isodate (formatters :date))

(defn process-row [[item amount date]]
  (let [amount (replace amount #"," ".")
        ; date (unparse isodate (parse ddmmyyy date)) TODO support several formats
        ]
    [item amount date]))

(defn on-input [value]
  (let [cells (map #(split % #"\t") (split-lines value))
        cells (map process-row cells)]
    (swap! app-state assoc :cells cells)))

(defn hello-world []
  [:div
    [:h1 (:text @app-state)]
    [:textarea {:rows 15
                :on-change #(-> % .-target .-value on-input)}]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn grid2 []
(let [el (create-element js/ReactDataGrid
                         (clj->js {
                                    :columns [ {:key "item", :name "Item", :width 80 }
                                               {:key "value", :name "Value", :width 80 :editable true }
                                               {:key "date", :name "Date", :width 80 }]
                                    :rowGetter (fn [row]
                                                 (let [[i v d] (nth (:cells @app-state) row)]
                                                   #js {:item i :value v :date d}))
                                    :enableCellSelect true
                                    :rowsCount (count (:cells @app-state))}))]
  el))

(defn render []
  (reagent/render-component [grid2]
                          (. js/document (getElementById "grid"))))

(aset  js/document "onreadystatechange" render)

;; (defn on-js-reload []
;;   (js/console.log "RRRRR" js/ReactDataGrid)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
;; )



