(ns ajchemist.templates.package
  (:require
   [clojure.java.io :as jio]
   [ajchemist.templates.core :as t.core]
   [ajchemist.templates.github :as t.github]
   ))


(def data-fn
  (t.core/configure-data-fn
    identity
    [t.core/data-wrap-template-deps
     t.github/data-wrap]))


(defn template-fn
  "Result is used as the EDN for the template."
  [edn {:keys [overwrite] :as data}]
  (tap> [:debug edn])
  (tap> [:debug data])
  edn)


#_:clj-kondo/ignore
(defonce ^:private setup-tap (add-tap t.core/tap))
