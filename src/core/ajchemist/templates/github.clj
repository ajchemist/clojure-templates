(ns ajchemist.templates.github
  (:require
   [clojure.string :as str]
   ))


(defn ensure-github-user
  [{:as data}]
  (-> data
    (update :github/user
      #(or % (:scm/user data)))))


(defn ensure-github-token-pass-name
  [{:as data}]
  (-> data
    (update :github.token/pass-name
      #(or % (str "io/github/ajchemist/templates/github/pat/" (:scm/user data))))))


(defn ensure-github
  [data]
  (-> data
    (update :github/org #(or % nil))
    (ensure-github-user)
    (ensure-github-token-pass-name)))
