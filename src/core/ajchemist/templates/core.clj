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


(defmulti deps-manifest
  (fn [coord]
    (cond
      (find coord :git/sha)     :git
      (find coord :mvn/version) :mvn
      (find coord :local/root)  :local
      :else                     (:deps/manifest coord))))


(defmethod deps-manifest :git
  [coord]
  (reduce-kv
    (fn [ret k v]
      (cond-> ret
        (= (namespace k) "git") (assoc k v)))
    {}
    coord))


(defmethod deps-manifest :mvn
  [coord]
  (reduce-kv
    (fn [ret k v]
      (cond-> ret
        (= (namespace k) "mvn") (assoc k v)))
    {}
    coord))


(defmethod deps-manifest :local
  [coord]
  (reduce-kv
    (fn [ret k v]
      (cond-> ret
        (= (namespace k) "local") (assoc k v)))
    {}
    coord))


(defmethod deps-manifest :default
  [coord]
  (dissoc coord :paths :dependents :parents))


(defn template-deps-manifest
  ([template-name]
   (template-deps-manifest (current-basis) template-name))
  ([basis template-name]
   (-> basis (get-in [:libs template-name]) (deps-manifest))))


(defn ensure-template-deps-name
  [data]
  (-> data
    (update :template/deps-name #(or % 'io.github.ajchemist/clojure-templates))))


(defn ensure-template-deps-manifest
  [data]
  (-> data
    (update :template/deps-manifest #(or % (template-deps-manifest (:template/deps-name data))))))


(defn ensure-template-deps
  "data-fn handler.

  Result is merged onto existing options data."
  [data]
  (-> data
    (ensure-template-deps-name)
    (ensure-template-deps-manifest)))


(def data-wrap-template-deps
  #(fn [data] (% (ensure-template-deps data))))


;;


(defn configure-data-fn
  "`data-fn` -> chain [wrap1, wrap2]

  return (wrap2 (wrap1 data-fn))"
  [data-fn & middleware-chains]
  (let [data-fn' (reduce
                   (fn [ret chain]
                     (reduce #(%2 %1) ret (reverse chain)))
                   data-fn
                   middleware-chains)]
    (fn
      [data]
      (tap> [:debug {:step :before-data-fn :data data}])
      (let [data' (data-fn' data)]
        (tap> [:debug {:step :after-data-fn :data data'}])
        data'))))


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
