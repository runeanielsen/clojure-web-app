(ns web-app.users
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
            [:timestamp :datetime :default :current_timestamp]
            [:firstname :text :not_null]
            [:lastname :text :not_null]]))))

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
  (jdbc/insert! db :users user))

(defn delete!
  "Delete user on id"
  [id]
  (jdbc/delete! db :users ["id = ?" id]))
