; Auto-generated. Do not edit!


(cl:in-package rss_msgs-msg)


;//! \htmlinclude ResetMsg.msg.html

(cl:defclass <ResetMsg> (roslisp-msg-protocol:ros-message)
  ((reset
    :reader reset
    :initarg :reset
    :type cl:boolean
    :initform cl:nil))
)

(cl:defclass ResetMsg (<ResetMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <ResetMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'ResetMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name rss_msgs-msg:<ResetMsg> is deprecated: use rss_msgs-msg:ResetMsg instead.")))

(cl:ensure-generic-function 'reset-val :lambda-list '(m))
(cl:defmethod reset-val ((m <ResetMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:reset-val is deprecated.  Use rss_msgs-msg:reset instead.")
  (reset m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <ResetMsg>) ostream)
  "Serializes a message object of type '<ResetMsg>"
  (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:if (cl:slot-value msg 'reset) 1 0)) ostream)
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <ResetMsg>) istream)
  "Deserializes a message object of type '<ResetMsg>"
    (cl:setf (cl:slot-value msg 'reset) (cl:not (cl:zerop (cl:read-byte istream))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<ResetMsg>)))
  "Returns string type for a message object of type '<ResetMsg>"
  "rss_msgs/ResetMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'ResetMsg)))
  "Returns string type for a message object of type 'ResetMsg"
  "rss_msgs/ResetMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<ResetMsg>)))
  "Returns md5sum for a message object of type '<ResetMsg>"
  "ba4b0b221fb425ac5eaf73f71ae34971")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'ResetMsg)))
  "Returns md5sum for a message object of type 'ResetMsg"
  "ba4b0b221fb425ac5eaf73f71ae34971")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<ResetMsg>)))
  "Returns full string definition for message of type '<ResetMsg>"
  (cl:format cl:nil "bool reset~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'ResetMsg)))
  "Returns full string definition for message of type 'ResetMsg"
  (cl:format cl:nil "bool reset~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <ResetMsg>))
  (cl:+ 0
     1
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <ResetMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'ResetMsg
    (cl:cons ':reset (reset msg))
))
