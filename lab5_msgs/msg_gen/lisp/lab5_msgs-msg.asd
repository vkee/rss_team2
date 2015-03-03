
(cl:in-package :asdf)

(defsystem "lab5_msgs-msg"
  :depends-on (:roslisp-msg-protocol :roslisp-utils :std_msgs-msg
)
  :components ((:file "_package")
    (:file "GUILineMsg" :depends-on ("_package_GUILineMsg"))
    (:file "_package_GUILineMsg" :depends-on ("_package"))
    (:file "ColorMsg" :depends-on ("_package_ColorMsg"))
    (:file "_package_ColorMsg" :depends-on ("_package"))
    (:file "GUIPointMsg" :depends-on ("_package_GUIPointMsg"))
    (:file "_package_GUIPointMsg" :depends-on ("_package"))
    (:file "GUISegmentMsg" :depends-on ("_package_GUISegmentMsg"))
    (:file "_package_GUISegmentMsg" :depends-on ("_package"))
    (:file "GUIEraseMsg" :depends-on ("_package_GUIEraseMsg"))
    (:file "_package_GUIEraseMsg" :depends-on ("_package"))
  ))