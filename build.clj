(def foreign-libs [{:file "node_modules/react/dist/react.js"
                                :provides ["react"]
                                :module-type :commonjs}
                               {:file "node_modules/react-dom/dist/react-dom.js"
                                :provides ["react-dom"]
                                :module-type :commonjs}
                                {:file "node_modules/react-data-grid/dist/react-data-grid.js"
                                :provides ["rrr"]
                                :module-type :commonjs}])

(require 'cljs.build.api)

(cljs.build.api/build "src"
  {:output-to "out/main.js"
   :verbose true
   :foreign-libs foreign-libs})
