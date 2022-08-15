(ns ajchemist.templates.core
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as jio]
   )
  (:import
   java.io.PushbackReader
   ))


(defn- current-basis
  []
  (with-open [rdr (jio/reader (System/getProperty "clojure.basis"))]
    (edn/read (PushbackReader. rdr))))


(defmulti template-meta
  (fn [coord]
    (cond
      (find coord :git/sha)     :git
      (find coord :mvn/version) :mvn
      (find coord :local/root)  :local
      :else                     (:deps/manifest coord))))


(defmethod template-meta :git
  [coord]
  (reduce-kv
    (fn [ret k v]
      (cond-> ret
        (= (namespace k) "git") (assoc k v)))
    {}
    coord))


(defmethod template-meta :mvn
  [coord]
  (reduce-kv
    (fn [ret k v]
      (cond-> ret
        (= (namespace k) "mvn") (assoc k v)))
    {}
    coord))


(defmethod template-meta :local
  [coord]
  (reduce-kv
    (fn [ret k v]
      (cond-> ret
        (= (namespace k) "local") (assoc k v)))
    {}
    coord))


(defmethod template-meta :default
  [coord]
  (dissoc coord :paths :dependents :parents))


(defn ensure-template-meta
  "data-fn handler.

  Result is merged onto existing options data."
  [data]
  (update data
    :template/meta
    #(or % (-> (current-basis) (get-in [:libs 'io.github.ajchemist/clojure-templates]) (template-meta)))))


;;


(def ^:privates verbose-levels
  {:trace 0
   :debug 1
   :info  2
   :warn  3
   :error 4
   :fatal 5
   :off   6})


(defn tap
  [o]
  (try
    (when (vector? o)
      (let [[verbose & message] o

            m        (meta o)
            print-fn (cond
                       (find m :println) println
                       :else             prn)

            property  (System/getProperty "io.github.ajchemist.templates.verbose")
            criterion (cond
                        (nil? property) 2
                        :else           (parse-long property))]
        (when (<= criterion (get verbose-levels verbose))
          (apply print-fn message))))
    (catch Throwable _)))
