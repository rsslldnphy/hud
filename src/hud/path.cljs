(ns hud.path
  (:require
    [cljs.reader    :as r]
    [clojure.string :as s]))

(defn parse-path
  [s]
  (try (mapv r/read-string (s/split s #" "))
       (catch js/Error e)))

(defn fetch-path
  [db path]
  (try (get-in db path)
       (catch js/Error e)))

(defn suggestions
  [db path]
  (let [db (fetch-path db path)]
    (try (doall (map (comp (partial apply pr-str)
                           (partial conj path))
                     (cond (map? db)
                           (keys db)
                           (vector? db)
                           (range 0 (count db))
                           (set? db)
                           db)))
         (catch js/Error e))))
