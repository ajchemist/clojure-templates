(ns ajchemist.templates.github
  (:require
   [clojure.string :as str]
   ))


(defn ensure-github-user
  [{:as data}]
  (-> data
    (update :github/user
      #(str (or % (:scm/user data))))))


(defn ensure-github-repository
  [{:as data}]
  (-> data
    (update :github/repository
      #(or %
           (let [group (str (or (:github/org data) (:github/user data)))]
             (str (symbol group (:scm/repo data))))))))


(defn ensure-github-token-pass-name
  [{:as data}]
  (-> data
    (update :github.token/pass-name
      #(str (or % (str "github.com/tokens/" (:github/user data)))))))


(defn ensure-github
  [data]
  (-> data
    (ensure-github-user)
    (ensure-github-repository)
    (ensure-github-token-pass-name)))


(def data-wrap
  #(fn [data] (% (ensure-github data))))
