(ns golden-kestrel.core
  (:require
    [clojure.string :as string]
    [clojure.set :as set]
    [goog.dom :as gdom]
    [om.core :as om :include-macros true]
    [om.dom :as dom :include-macros true]))

(enable-console-print!)

(def badge-types
  [["4" "Small badge"]
   ["2" "Medium badge"]
   ["1" "Large badge"]
   ["donut" "Small donut"]
   ["medium-donut" "Medium donut"]
   ["large-donut" "Large donut"]
   ["bar" "Small bar"]
   ["medium-bar" "Medium bar"]
   ["large-bar" "Large bar"]])

(def app-state
  (atom {:badge-type "medium-donut",
         :badge-details ""
         :popover "right"
         :hide-no-mentions true
         :doi "10.1038/nature.2014.14583"}))

(defn update-app
  [data item e]
  (let [target (. e -target)
        new-val (if (= "checkbox" (. target -type))
                  (. target -checked)
                  (. target -value))
        other-vals (cond
                     (and (= item :badge-details)
                          (= new-val "right"))
                     [:popover ""]
                     (and (= item :popover)
                          (not (= new-val "")))
                     [:badge-details ""]
                     :else [])
        all-vals (concat [item new-val] other-vals)]
    (om/update! data (fn [state]
                       (apply assoc state all-vals)))))

(defn attributes-for-embed
  [data]
  (->> {:className "altmetric-embed"
        :data-badge-popover (:popover data)
        :data-badge-details (:badge-details data)
        :data-badge-type (:badge-type data)
        :data-doi (:doi data)
        :data-hide-no-mentions (:hide-no-mentions data)}
       (remove (comp string/blank? second))
       (into {})))

(defn pair-as-attribute
  [[k v]]
  (str (name k) "=\"" v "\""))

(defn attributes-for-pre
  [data]
  (->> (set/rename-keys (attributes-for-embed data)
                        {:className :class})
       (map pair-as-attribute)
       (string/join " ")))

(defn embed-code
  [data]
  (dom/pre #js {:className "prettyprint"}
           (str "<div " (attributes-for-pre data) "></div>")))

(defn embed
  [data]
  (dom/div (clj->js (attributes-for-embed data)) nil))

(defn embed-form
  [data owner]
  (let [change-data (partial update-app data)]
    (dom/form
      nil
      (dom/fieldset
        nil
        (dom/label #js {:htmlFor "kestrel-badge-type"} "Badge type")
        (apply dom/select
               #js {:ref "badgeType"
                    :id "kestrel-badge-type"
                    :value (:badge-type data)
                    :defaultValue (:badge-type data)
                    :onChange (partial change-data :badge-type)}
               (for [[value text] badge-types]
                 (dom/option #js {:value value} text)))
        (dom/label #js {:htmlFor "kestrel-popover"} "Popover")
        (dom/select #js {:ref "popover"
                         :id "kestrel-popover"
                         :value (:popover data)
                         :defaultValue (if (= "" (:badge-details data))
                                         (:popover data)
                                         "")
                         :onChange (partial change-data :popover)}
                    (dom/option #js {:value ""} "None")
                    (dom/option #js {:value "top"} "Top")
                    (dom/option #js {:value "right"} "Right")
                    (dom/option #js {:value "bottom"} "Bottom")
                    (dom/option #js {:value "left"} "Left"))
        (dom/label #js {:htmlFor "kestrel-details"} "Details")
        (dom/select #js {:ref "details"
                         :id "kestrel-details"
                         :value (:badge-details data)
                         :defaultValue (if (= "" (:popover data))
                                         (:badge-details data)
                                         "")
                         :onChange (partial change-data :badge-details)}
                    (dom/option #js {:value ""} "None")
                    (dom/option #js {:value "right"} "Right"))
        (dom/input #js {:ref "hideNoMentions"
                        :id "kestrel-no-mentions"
                        :type "checkbox"
                        :checked (:hide-no-mentions data)
                        :onChange (partial change-data :hide-no-mentions)}
                   nil)
        (dom/label #js {:htmlFor "kestrel-no-mentions"} "Hide no mentions?")
        (dom/label #js {:htmlFor "kestrel-doi"} "DOI")
        (dom/input #js {:ref "doi"
                        :id "kestrel-doi"
                        :type "text"
                        :size "40"
                        :defaultValue (:doi data)
                        :onChange (partial change-data :doi)} nil)))))

(defn find-or-create-element
  [root class-name]
  (let [possibles (gdom/getElementsByClass class-name root)]
    (if (= 0 (.-length possibles))
      (let [form-elem (gdom/createElement "div")]
        (set! (.-className form-elem) class-name)
        (gdom/appendChild root form-elem)
        form-elem)
      (aget possibles 0))))

(defn run-altmetric-embeds
  []
  (try
    (js/_altmetric_embed_init)
    (catch js/Object e
      (js/setTimeout run-altmetric-embeds 200))))

(defn ^:export init
  []
  (when-let [rootdom (gdom/getElement "golden-kestrel")]
    (let [form-element (find-or-create-element rootdom "embed-form")
          embed-element (find-or-create-element rootdom "embed-example")
          code-element (find-or-create-element rootdom "embed-code")]
      (om/root
        app-state
        (fn [app owner]
          (om/component (embed-form app owner)))
        form-element)
      (om/root
        app-state
        (fn [app owner]
          (js/setTimeout run-altmetric-embeds 100)
          (om/component (embed app)))
        embed-element)
      (om/root
        app-state
        (fn [app owner]
          (om/component (embed-code app)))
        code-element))))

(init)
