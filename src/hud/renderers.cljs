(ns hud.renderers
  (:require
    [hud.colors   :as c]
    [hud.events   :as e]
    [hud.path     :as p]
    [reagent.core :as reagent]))

(declare render)

(defn punctuation
  [c open?]
  [:span {:on-click (e/toggle-fn open?) :style {:color c/red :cursor "pointer"}} c])

(def left-brace           (partial punctuation "{"))
(def right-brace          (partial punctuation "}"))
(def left-bracket         (partial punctuation "("))
(def right-bracket        (partial punctuation ")"))
(def left-square-bracket  (partial punctuation "["))
(def right-square-bracket (partial punctuation "]"))
(def hash-sign            (partial punctuation "#"))

(defn ellipsis
  [open?]
  [:a {:on-click (e/toggle-fn open?) :style {:color c/green} :href "#"} ",,,"])

(def space
  [:span " "])

(def br
  [:br])

(defn with-indentation
  [n & forms]
  (into [:span] (concat (repeat n space) forms)))

(defn map-entry
  [[k v] opts indent]
  [:span [render k opts indent] space [render v opts (+ indent (count (str k)))]])

(defn- render*
  [e opts indent open?]
  (let [open?* @open?]

  (cond
    (keyword? e)
    [:span {:style {:color c/yellow}} (pr-str e)]

    (string? e)
    [:span {:style {:color c/blue}} (pr-str e)]

    (number? e)
    [:span {:style {:color c/green}} (pr-str e)]

    (map? e)
    (if open?*
      (let [e (sort-by first e)]
        (conj (into [:span [left-brace open?] [map-entry (first e) opts 0] (when (not-empty (rest e)) br)]
                    (interpose br (mapv #(with-indentation (+ 1 indent) (map-entry % opts (+ 2 indent))) (rest e))))
              [right-brace open?]))
      [:span [left-brace open?] [ellipsis open?] [right-brace open?]])

    (or (vector? e) (list? e))
    (if open?*
      (conj (into [:span [left-square-bracket open?] [render (first e) opts (+ 1 indent)] (when (not-empty (rest e)) br)]
                  (interpose br (mapv #(with-indentation (+ 1 indent) [render % opts (+ 1 indent)]) (rest e))))
            [right-square-bracket open?])
      [:span [left-square-bracket open?] [ellipsis open?] [right-square-bracket open?]])

    (set? e)
    (if open?*
      (let [e (sort e)]
        (conj (into [:span [hash-sign open?] [left-brace open?] [render (first e) opts (+ 2 indent)] (when (not-empty (rest e)) " ")]
                    (interpose " " (mapv #(with-indentation 0 [render % opts (+ 2 indent)]) (rest e))))
              [right-brace open?]))
      [:span [hash-sign open?] [left-brace open?] [ellipsis open?] [right-brace open?]])

    (seq e)
    (if open?*
      (conj (into [:span [left-bracket open?] [render (first e) opts (+ 1 indent)] (when (not-empty (rest e)) br)]
                  (interpose br (mapv #(with-indentation (+ 1 indent) [render % opts (+ 1 indent)]) (rest e))))
            [right-bracket open?])
      [:span [left-bracket open?] [ellipsis open?] [right-bracket open?]])

    :else
    [:span (pr-str e)])))

(defn- expanded-on-load?
  [e opts]
  (boolean (and (coll? e)
                (< (count e) (:collapse-threshold opts)))))

(defn render
  [e opts indent]
  (let [open? (reagent/atom (expanded-on-load? e opts))]
    (fn [e opts indent]
      [render* e opts indent open?])))
