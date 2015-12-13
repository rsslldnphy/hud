(ns example.views
  (:require
    [re-frame.core :as re-frame]
    [re-frame.db   :as db]
    [hud.core      :as hud]))


;; home

(defn home-panel []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [:div (str "Hello from " @name ". This is the Home Page.")
       [:h1 "Press Ctrl+Enter to toggle the HUD."]
       [:div [:a {:href "#/about"} "go to About Page"]]])))


;; about

(defn about-panel []
  (fn []
    [:div "This is the About Page."
     [:h1 "Press Ctrl+Enter to toggle the HUD."]
     [:div [:a {:href "#/"} "go to Home Page"]]]))


;; main

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:div [hud/hud {:font-family "Roboto Mono"} @db/app-db]
       (panels @active-panel)])))
