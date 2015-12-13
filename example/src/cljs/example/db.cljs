(ns example.db)

(def default-db
  {:name "hud example"
   :floob [#{:a :b :c :d :e :f :g :h}
           #{:a :b :c :d :e :f :g :h}
           #{:a :b :c :d :e :f :g :h}]
   :cats [{:name "russell"  :favourite-food "cheese"  :x 1 :y 2 :z 3 :a 4 :b 5 :c 6}
          {:name "gestalta" :favourite-food "spinach" :x 1 :y 2 :z 3 :a 4 :b 5 :c 6}]})
