(ns test-runner
  (:require
   [cognitect.test-runner.api]
   ))


(defn exec
  [opts]
  (try
    (cognitect.test-runner.api/test opts)
    (finally
      (shutdown-agents)
      (System/exit 0))))
