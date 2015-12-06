(ns hud.state
  (:require
    [reagent.core :as reagent]))

(defonce open? (reagent/atom false))
(defonce path  (reagent/atom []))
