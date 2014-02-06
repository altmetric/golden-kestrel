(ns golden-kestrel.core
  (:require
    [om.core :as om :include-macros true]
    [om.dom :as dom :include-macros true]))

(enable-console-print!)

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

(defn embed [data]
  (dom/div #js {:className "altmetric-embed",
                :data-badge-popover (:popover data)
                :data-badge-details (:badge-details data)
                :data-badge-type (:badge-type data)
                :data-doi (:doi data)
                :data-hide-no-mentions (:hide-no-mentions data)}
           nil))

(defn embed-form [data owner]
  (let [change-data (partial update-app data)]
    (dom/form
      #js {:className "form-horizontal"}
      (dom/div
        #js {:className "control-group"}
        (dom/label #js {:className "control-label"}
                   "Badge type")
        (dom/div
          #js {:className "controls"}
          (dom/select #js {:ref "badgeType"
                           :value (:badge-type data)
                           :defaultValue (:badge-type data)
                           :onChange (partial change-data :badge-type)}
                      (dom/option #js {:value ""} "Default")
                      (dom/option #js {:value "1"} "1")
                      (dom/option #js {:value "4"} "4")
                      (dom/option #js {:value "donut"} "Donut")
                      (dom/option #js {:value "medium-donut"} "Medium donut")
                      (dom/option #js {:value "large-donut"} "Large donut"))))
      (dom/div
        #js {:className "control-group"}
        (dom/label #js {:className "control-label"}
                   "Popover")
        (dom/div
          #js {:className "controls"}
          (dom/select #js {:ref "popover"
                           :value (:popover data)
                           :defaultValue (if (= "" (:badge-details data))
                                           (:popover data)
                                           "")
                           :onChange (partial change-data :popover)}
                      (dom/option #js {:value ""} "None")
                      (dom/option #js {:value "top"} "Top")
                      (dom/option #js {:value "right"} "Right")
                      (dom/option #js {:value "bottom"} "Bottom")
                      (dom/option #js {:value "left"} "Left"))))
      (dom/div
        #js {:className "control-group"}
        (dom/label #js {:className "control-label"}
                   "Details")
        (dom/div
          #js {:className "controls"}
          (dom/select #js {:ref "details"
                           :value (:badge-details data)
                           :defaultValue (if (= "" (:popover data))
                                           (:badge-details data)
                                           "")
                           :onChange (partial change-data :badge-details)}
                      (dom/option #js {:value ""} "None")
                      (dom/option #js {:value "right"} "Right"))))
      (dom/div
        #js {:className "control-group"}
        (dom/label #js {:className "control-label"}
                   "Hide no mentions?")
        (dom/div
          #js {:className "controls"}
          (dom/input #js {:ref "hideNoMentions"
                          :type "checkbox"
                          :checked (:hide-no-mentions data)
                          :onChange (partial change-data :hide-no-mentions)}
                     nil)))
      (dom/div
        #js {:className "control-group"}
        (dom/label #js {:className "control-label"}
                   "DOI")
        (dom/div
          #js {:className "controls"}
          (dom/input #js {:ref "doi"
                          :type "text"
                          :size "40"
                          :defaultValue (:doi data)
                          :onChange (partial change-data :doi)} nil))))))

(defn find-or-create-element
  [root class-name]
  (let [possibles (. root (getElementsByClassName class-name))]
    (if (= 0 (.-length possibles))
      (let [form-elem (. js/document (createElement "div"))]
        (set! (.-className form-elem) class-name)
        (. root (appendChild form-elem))
        form-elem)
      (aget possibles 0))))

(defn ^:export init
  []
  (when-let [rootdom (. js/document (getElementById "golden-kestrel"))]
    (let [form-element (find-or-create-element rootdom "embed-form")
          embed-element (find-or-create-element rootdom "embed-example")]
      (om/root
        app-state
        (fn [app owner]
          (om/component (embed-form app owner)))
        form-element)
      (om/root
        app-state
        (fn [app owner]
          (js/setTimeout js/_altmetric_embed_init 500)
          (om/component (embed app)))
        embed-element))))
