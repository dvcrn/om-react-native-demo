;; Need to set js/React first so that Om can load
(set! js/React (js/require "react-native/Libraries/react-native/react-native.js"))

(ns demo.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.next :as om :refer-macros [defui]]
            [cognitect.transit :as t]
            [cljs.core.async :refer [chan put!]])
  (:import [goog.net XhrIo]))

;; Reset js/React back as the form above loads in an different React
(set! js/React (js/require "react-native/Libraries/react-native/react-native.js"))


;; Setup some methods to help create React Native elements
(defn view [opts & children]
  (apply js/React.createElement js/React.View (clj->js opts) children))

(defn text [opts & children]
  (apply js/React.createElement js/React.Text (clj->js opts) children))

(enable-console-print!)

;; Set up our Om UI
(def app-state (atom {:app/msg "Welcome to om"}))

(defui TimelineComponent
  static om/IQuery
  (query [this]
         '[:app/msg])
  Object
  (render [this]
          (let [{:keys [app/msg]} (om/props this)]
            (view {:style {:flexDirection "column" :margin 40}}
                  (text nil msg)))))

;; om.next parser
(defmulti read om/dispatch)
(defmethod read :default
  [{:keys [state]} k _]
  (let [st @state]
    (if-let [[_ v] (find st k)]
      {:value v}
      {:value :not-found})))

(def reconciler
  (om/reconciler
   {:state app-state
    :parser (om/parser {:read read})
    :root-render #(.render js/React %1 %2)
    :root-unmount #(.unmountComponentAtNode js/React %)}))

;; (defui Post
;;   static om/Ident
;;   (ident [this {:keys [id]}]
;;          [:post/by-id id])
;;   static om/IQuery
;;   (query [this]
;;          '[:id {:user [:username]} :content])
;;   Object
;;   (render [this]
;;           (let [{:keys [id user content]} (om/props this)
;;                 {:keys [username]} user]
;;             (view {:style {:marginBottom 20}}
;;                   (text {:style {:fontSize 25}} username)
;;                   (text {:style {:fontSize 20}} (str ">> " content))
;;                   ))))

;; (def post (om/factory Post))


;; (defui TimelineComponent
;;   static om/IQuery
;;   (query [this]
;;          (let [subquery (om/get-query Post)]
;;            `[{:app/posts ~subquery}]))
;;   Object
;;   (render [this]
;;           (let [{:keys [app/posts]} (om/props this)]
;;             (view {:style {:flexDirection "column" :margin 40}}
;;                   (apply view nil
;;                          (map post posts))))))

;; ;; Parsing
;; (defn transit-post [url]
;;   (fn [edn cb]
;;     (.send XhrIo url
;;            (fn [e]
;;              (this-as this
;;                       (cb (t/read (t/reader :json-verbose) (.getResponseText this)))))
;;            "POST" (t/write (t/writer :json-verbose) edn)
;;            #js {"Content-Type" "application/transit+json"})))

;; (defn get-data [state key]
;;   (let [st @state]
;;     (into [] (map #(get-in st %) (get st key)))))

;; ;; om.next parser
;; (defmulti read om/dispatch)
;; (defmethod read :default
;;   [{:keys [state]} k _]
;;   (let [st @state]
;;     (if-let [[_ v] (find st k)]
;;       {:value v}
;;       {:value :not-found})))

;; (defmethod read :app/posts
;;   [{:keys [state] :as env} key params]
;;   (if (find @state key)
;;     {:value (get-data state key)}
;;     {:remote true}))

;; (def reconciler
;;   (om/reconciler
;;    {:state app-state
;;     :parser (om/parser {:read read})
;;     :normalize true
;;     :send (transit-post "http://localhost:8000/api.json")
;;     :root-render #(.render js/React %1 %2)
;;     :root-unmount #(.unmountComponentAtNode js/React %)}))

(om/add-root! reconciler TimelineComponent 1)

(defn ^:export init []
  ((fn render []
     (.requestAnimationFrame js/window render))))
