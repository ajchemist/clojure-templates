(ns ajchemist.templates.scratch
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
  "Example template-fn handler.

  Result is used as the EDN for the template."
  [edn {:keys [overwrite] :as data}]
  ;; must return the whole EDN hash map
  #_(with-open [w (jio/writer (doto (jio/file (:target-dir data) "INFO") (jio/make-parents)))]
      (binding [*out* w]
        (prn data)))
  (tap> ^:println [:debug "template-fn [edn]"])
  (tap> [:debug edn])
  (tap> ^:println [:debug "template-fn [data]"])
  (tap> [:debug data])
  (println "template-fn returning edn")
  edn)


#_:clj-kondo/ignore
(defonce ^:private setup-tap (add-tap t.core/tap))
