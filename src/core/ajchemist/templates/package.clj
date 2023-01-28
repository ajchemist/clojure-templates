(ns ajchemist.templates.package
  (:require
   [clojure.java.io :as jio]
   [ajchemist.templates.core :as t.core]
   [ajchemist.templates.github :as t.github]
   ))


(defn data-fn
  "data-fn handler.

  Result is merged onto existing options data."
  [data]
  ;; returning nil means no changes to options data
  (tap> [:debug data])
  (-> data
    (t.core/ensure-template-deps)
    (t.github/ensure-github)))


(defn template-fn
  "Result is used as the EDN for the template."
  [edn {:keys [overwrite] :as data}]
  (tap> [:debug edn])
  (tap> [:debug data])
  edn)


#_:clj-kondo/ignore
(defonce ^:private setup-tap (add-tap t.core/tap))
