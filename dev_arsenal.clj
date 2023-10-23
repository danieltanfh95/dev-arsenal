#!/usr/bin/env bb

(require '[babashka.process :refer [process]]
         '[clojure.java.io :as io]
         '[clojure.tools.cli :refer [parse-opts]])

(defn fixed-length-password
  ([] (fixed-length-password 12))
  ([n]
   (let [n (Integer/parseInt (str n))
         chars-between #(map char (range (int %1) (inc (int %2))))
         chars (concat (chars-between \0 \9)
                       (chars-between \a \z)
                       (chars-between \A \Z)
                       [\_ \{ \} \[ \] \! \# \$ \% \^ \& \* \+])
         password (take n (repeatedly #(rand-nth chars)))]
     (reduce str password))))

(defn b64-decode [b64]
  (let [decoder (java.util.Base64/getDecoder)
        resultBytes (.decode decoder b64)]
    (String. resultBytes)))

(defn b64-encode [txt]
  (let [txt (str txt)
        encoder (java.util.Base64/getEncoder)
        resultBytes (.encode encoder (.getBytes txt))]
    (String. resultBytes)))

(defn hex->rgb [color]
  (let [colors (-> color
                   (clojure.string/replace #"\#" "")
                   (clojure.string/split #""))
        red (take 2 colors)
        green (take 2 (drop 2 colors))
        blue (take 2 (drop 4 colors))]
    (mapv #(-> (conj % "0x") (clojure.string/join) (read-string)) [red green blue])))

(defn rgb->hex [red green blue]
  (->> [red green blue]
       (mapv #(Integer/toString (Integer/parseInt (str %)) 16))
       (clojure.string/join)
       (str "#")))

(defn copy-to-clipboard [txt]
  (let [p (process '[pbcopy])
        stdin (io/writer (:in p))]
    (binding [*out* stdin]
      (print txt))
    (.close stdin)))

(defn apply-cli-args [fn-map]
  (let [[command & rest-args] *command-line-args*
        fn (get fn-map (keyword command))]
    (cond
      (not (nil? rest-args)) (apply fn rest-args)
      (= "-" (first rest-args)) (fn (slurp *input*))
      :else (fn))))

(let [result (->> {
                   :uuid    random-uuid
                   :pw      fixed-length-password
                   :b64d    b64-decode
                   :b64e    b64-encode
                   :hex2rgb hex->rgb
                   :rgb2hex rgb->hex
                   }
                  (apply-cli-args)
                  (str))]
  (println result)
  (copy-to-clipboard result))

