(ns web-app.core
  (:require [compojure.core :refer [GET POST DELETE defroutes context routes]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as ring-json]
            [ring.util.response :as rr]
            [web-app.user :as user]))

(user/create-db)

(defn user-routes
  []
  (routes
   (GET "/users" []
     (rr/response (user/get-all)))
   (GET "/users/:id" [id]
     (let [user (user/get-by-id id)]
       (if (nil? user)
         (rr/not-found "Not found")
         (rr/response user))))
   (POST "/users" {user :body}
     (let [user (user/insert! user)]
       (rr/created (format "/users/%d" (:id user)) user)))
   (DELETE "/users/:id" [id]
     (rr/response
      (if (user/delete! id)
        (rr/response "Deleted")
        (rr/not-found "Not found"))))))

(defroutes app-routes
  (user-routes)
  (route/not-found "Not found"))

(def app
  (-> app-routes
      (ring-json/wrap-json-body {:keywords? true :bigdecimals? true})
      (ring-json/wrap-json-response)
      (wrap-defaults (assoc site-defaults :security {:anti-forgery false}))))
