(ns example.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [example.handlers]
              [example.subs]
              [example.routes :as routes]
              [example.views :as views]
              [example.config :as config]))

(when config/debug?
  (println "dev mode"))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init [] 
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
