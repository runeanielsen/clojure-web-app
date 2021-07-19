(ns web-app.core
  (:require [compojure.core :refer [GET POST DELETE defroutes context routes]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as ring-json]
            [ring.util.response :as rr]
            [web-app.users :as users]))

(users/create-db)

(defn user-routes
  []
  (routes
   (GET "/users" []
     (rr/response (users/get-all)))
   (GET "/users/:id" [id]
     (let [users (users/get-by-id id)]
       (if (nil? users)
         (rr/not-found "Not found")
         (rr/response users))))
   (POST "/users" {user :body}
     (rr/response (users/insert! user)))
   (DELETE "/users/:id" [id]
     (rr/response (users/delete! id)))))

(defroutes app-routes
  (user-routes)
  (route/not-found "Not found"))

(def app
  (-> app-routes
      (ring-json/wrap-json-body)
      (ring-json/wrap-json-response)
      (wrap-defaults (assoc site-defaults :security {:anti-forgery false}))))
