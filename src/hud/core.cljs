(ns hud.core
  (:require
    [hud.colors    :as c]
    [hud.events    :as e]
    [hud.opts      :as o]
    [hud.path      :as p]
    [hud.renderers :as r]
    [hud.state     :as s]
    [hud.views     :as v]
    [reagent.core  :as reagent]))

(defn hud
  ([db]
   [hud {} db])
  ([opts db]
   (reagent/create-class
     {:component-did-mount e/listen!
      :component-will-unmount e/unlisten!
      :reagent-render
      (fn [opts db]
        (let [path @s/path
              db*  (p/fetch-path db path)
              opts (merge o/default-opts opts)]
          [:div
           (when @s/open?
             [v/hud-outer opts
              [v/hud-control-bar
               [v/hud-title]
               [v/hud-path-control db path]
               [v/hud-path-summary path]
               [v/hud-copy-control]]
              [v/hud-display
               [r/render db* opts 0]]])]))})))

(defn re-frame-hud
  []
  [hud @re-frame.db/app-db])
