(ns hud.events
  (:require
    [hud.path    :as p]
    [hud.state   :as s]
    [goog.events :as e]))

(defn toggle-fn
  [open?]
  #(do (swap! open? not) nil))

(def toggle (toggle-fn s/open?))

(def ENTER 13)
(def ESC   27)

(defn ctrl-one?
  [e]
  (and (= 49 (.-keyCode e))
       (.-ctrlKey e)))

(defn key-handler
  [e]
  (when (ctrl-one? e) (toggle)))

(defn listen!
  []
  (e/listen js/document e/EventType.KEYUP key-handler))

(defn unlisten!
  []
  (e/unlisten js/document e/EventType.KEYUP key-handler))

(defn update-path!
  [db e]
  (when-let [path (p/parse-path (.-value (.-target e)))]
    (when (p/fetch-path db path)
      (reset! s/path path))))

(defn copy-to-clipboard
  [db]
  (let [textarea (.createElement js/document "textarea")
        style    (.-style textarea)]
    (set! (.-position      style)       "fixed")
    (set! (.-top           style)             0)
    (set! (.-left          style)             0)
    (set! (.-width         style)         "2em")
    (set! (.-height        style)         "2em")
    (set! (.-padding       style)             0)
    (set! (.-border        style)        "none")
    (set! (.-outline       style)        "none")
    (set! (.-boxShadow     style)        "none")
    (set! (.-background    style) "transparent")
    (set! (.-value      textarea)   (pr-str db))
    (.appendChild js/document.body textarea)
    (.select textarea)
    (try
      (when-not (.execCommand js/document "copy")
        (js/alert "Copy to clipboard not supported on your browser. You're going to need to do it manually."))
      (catch js/Error e
        (js/alert "Copy to clipboard not supported on your browser. You're going to need to do it manually.")))
    (.removeChild js/document.body textarea)))
