(ns ajchemist.templates.scratch
  (:require
   [clojure.java.io :as jio]
   [ajchemist.templates.core :as core]
   ))


(defn data-fn
  "Example data-fn handler.

  Result is merged onto existing options data."
  [data]
  ;; returning nil means no changes to options data
  (tap> [:debug data])
  (core/ensure-template-meta data))


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
(defonce ^:private setup-tap (add-tap core/tap))
