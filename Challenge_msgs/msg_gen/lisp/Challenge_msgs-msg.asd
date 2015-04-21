
(cl:in-package :asdf)

(defsystem "Challenge_msgs-msg"
  :depends-on (:roslisp-msg-protocol :roslisp-utils :lab5_msgs-msg
)
  :components ((:file "_package")
    (:file "GUIEllipseMessage" :depends-on ("_package_GUIEllipseMessage"))
    (:file "_package_GUIEllipseMessage" :depends-on ("_package"))
    (:file "GUIStringMessage" :depends-on ("_package_GUIStringMessage"))
    (:file "_package_GUIStringMessage" :depends-on ("_package"))
  ))