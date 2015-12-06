(ns hud.views
  (:require
    [hud.colors :as c]
    [hud.events :as e]
    [hud.path   :as p]))

(defn hud-outer
  [{:keys [font-family] :as opts} & children]
  (into [:div.hud {:style {:position "fixed"
                           :top 0
                           :left 0
                           :right 0
                           :width "100%"
                           :background c/base02
                           :color "#fff"
                           :overflow "hidden"
                           :white-space "pre"
                           :font-family font-family
                           :height "50vh"
                           :border-bottom (str "2px solid " c/base03)
                           :border-bottom-left-radius "10px"
                           :border-bottom-right-radius "10px"}}] children))

(defn hud-control-bar
  [& children]
  (into [:div {:style {:height "47px"
                       :border-bottom (str "2px solid " c/base03)}}] children))

(defn hud-title
  []
  [:span {:style {:display "inline-block"
                  :padding "5px 10px"
                  :margin "6px 5px"
                  :border-radius "5px"
                  :border (str "2px solid " c/base03)}}
   "HUD v 0.1.0"])

(defn hud-path-control
  [db path]
  [:div {:style {:display "inline-block"
                 :margin "6px 5px"
                 :border-radius "5px"
                 :position "relative"
                 :border (str "2px solid " c/base03)}}
   [:span {:style {:color c/cyan
                   :display "inline-block"
                   :padding "5px 0 5px 10px "
                   :font-weight "bold"}} "Path: "]
   [:input {:style {:width "500px"
                    :vertical-align "top"
                    :height "100%"
                    :font-size "1em"
                    :padding "5px"
                    :display "inline-block"
                    :font-weight "bold"
                    :color c/blue
                    :border-top-right-radius "5px"
                    :background-color c/base2
                    :border-bottom-right-radius "5px"}
            :auto-focus true
            :type "text"
            :list "hud-suggestions"
            :default-value (apply pr-str path)
            :on-change (partial e/update-path! db)}]
   (into [:datalist#hud-suggestions]
         (for [s (p/suggestions db path)] [:option {:value s}]))])

(defn hud-path-summary
  [path]
  [:span {:style {:margin-left "10px"}} (when (not-empty path) (pr-str path))])

(defn hud-copy-control
  [db]
  [:button {:on-click #(e/copy-to-clipboard db)
            :style {:float "right"
                    :display "inline-block"
                    :margin "8px 5px"
                    :cursor "pointer"
                    :padding "5px 10px"
                    :font-size "1em"
                    :background-color c/base03
                    :color c/cyan
                    :border-radius "5px"}} "copy"])

(defn hud-display
  [& children]
  (into [:div {:style {:border-bottom-left-radius "15px"
                       :padding "10px"
                       :padding-bottom "55px"
                       :height "100%"
                       :width "100%"
                       :overflow-y "auto"
                       :border-bottom-right-radius "15px"}}] children))
