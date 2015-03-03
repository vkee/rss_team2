; Auto-generated. Do not edit!


(cl:in-package lab5_msgs-msg)


;//! \htmlinclude GUIEraseMsg.msg.html

(cl:defclass <GUIEraseMsg> (roslisp-msg-protocol:ros-message)
  ((erase
    :reader erase
    :initarg :erase
    :type std_msgs-msg:String
    :initform (cl:make-instance 'std_msgs-msg:String)))
)

(cl:defclass GUIEraseMsg (<GUIEraseMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <GUIEraseMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'GUIEraseMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name lab5_msgs-msg:<GUIEraseMsg> is deprecated: use lab5_msgs-msg:GUIEraseMsg instead.")))

(cl:ensure-generic-function 'erase-val :lambda-list '(m))
(cl:defmethod erase-val ((m <GUIEraseMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:erase-val is deprecated.  Use lab5_msgs-msg:erase instead.")
  (erase m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <GUIEraseMsg>) ostream)
  "Serializes a message object of type '<GUIEraseMsg>"
  (roslisp-msg-protocol:serialize (cl:slot-value msg 'erase) ostream)
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <GUIEraseMsg>) istream)
  "Deserializes a message object of type '<GUIEraseMsg>"
  (roslisp-msg-protocol:deserialize (cl:slot-value msg 'erase) istream)
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<GUIEraseMsg>)))
  "Returns string type for a message object of type '<GUIEraseMsg>"
  "lab5_msgs/GUIEraseMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'GUIEraseMsg)))
  "Returns string type for a message object of type 'GUIEraseMsg"
  "lab5_msgs/GUIEraseMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<GUIEraseMsg>)))
  "Returns md5sum for a message object of type '<GUIEraseMsg>"
  "25d2385573bedcaf3e10c201d306a3be")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'GUIEraseMsg)))
  "Returns md5sum for a message object of type 'GUIEraseMsg"
  "25d2385573bedcaf3e10c201d306a3be")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<GUIEraseMsg>)))
  "Returns full string definition for message of type '<GUIEraseMsg>"
  (cl:format cl:nil "std_msgs/String erase~%================================================================================~%MSG: std_msgs/String~%string data~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'GUIEraseMsg)))
  "Returns full string definition for message of type 'GUIEraseMsg"
  (cl:format cl:nil "std_msgs/String erase~%================================================================================~%MSG: std_msgs/String~%string data~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <GUIEraseMsg>))
  (cl:+ 0
     (roslisp-msg-protocol:serialization-length (cl:slot-value msg 'erase))
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <GUIEraseMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'GUIEraseMsg
    (cl:cons ':erase (erase msg))
))
