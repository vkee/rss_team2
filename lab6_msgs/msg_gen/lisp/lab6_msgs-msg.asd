
(cl:in-package :asdf)

(defsystem "lab6_msgs-msg"
  :depends-on (:roslisp-msg-protocol :roslisp-utils :lab5_msgs-msg
)
  :components ((:file "_package")
    (:file "GUIRectMsg" :depends-on ("_package_GUIRectMsg"))
    (:file "_package_GUIRectMsg" :depends-on ("_package"))
    (:file "GUIPolyMsg" :depends-on ("_package_GUIPolyMsg"))
    (:file "_package_GUIPolyMsg" :depends-on ("_package"))
  ))