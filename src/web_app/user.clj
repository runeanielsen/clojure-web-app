(ns web-app.user
  (:require [clojure.java.jdbc :as jdbc]))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "database.db"})

(defn db-table-exists?
  "Check if the schema has been migrated to the database"
  [table-name]
  (if (empty? (-> (jdbc/query
                   db
                   [(format "SELECT name from sqlite_master WHERE type='table' AND name='%s'" table-name)])))
    false
    true))

(defn create-db
  "Create db and users table."
  []
  (jdbc/db-do-commands
   db
   (when-not (db-table-exists? "users")
     (jdbc/create-table-ddl
      :users
      [[:id :integer :primary :key]
       [:created :datetime :default :current_timestamp]
       [:first_name :text]
       [:last_name :text]]))))

(defn get-all
  "Get all users."
  []
  (jdbc/query db ["SELECT * FROM users"]))

(defn get-by-id
  "Get users on id."
  [id]
  (jdbc/get-by-id db "users" id))

(defn insert!
  "Insert user"
  [user]
  (->
   (jdbc/insert! db :users
                 {:first_name (user :firstName)
                  :last_name (user :lastName)})
   first
   vals
   first
   (get-by-id)))

(defn delete!
  "Delete user on id"
  [id]
  (if (= (first (jdbc/delete! db :users ["id = ?" id])) 1)
    true
    false))
