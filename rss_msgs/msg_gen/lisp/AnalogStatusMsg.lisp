; Auto-generated. Do not edit!


(cl:in-package rss_msgs-msg)


;//! \htmlinclude AnalogStatusMsg.msg.html

(cl:defclass <AnalogStatusMsg> (roslisp-msg-protocol:ros-message)
  ((values
    :reader values
    :initarg :values
    :type (cl:vector cl:float)
   :initform (cl:make-array 8 :element-type 'cl:float :initial-element 0.0)))
)

(cl:defclass AnalogStatusMsg (<AnalogStatusMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <AnalogStatusMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'AnalogStatusMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name rss_msgs-msg:<AnalogStatusMsg> is deprecated: use rss_msgs-msg:AnalogStatusMsg instead.")))

(cl:ensure-generic-function 'values-val :lambda-list '(m))
(cl:defmethod values-val ((m <AnalogStatusMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:values-val is deprecated.  Use rss_msgs-msg:values instead.")
  (values m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <AnalogStatusMsg>) ostream)
  "Serializes a message object of type '<AnalogStatusMsg>"
  (cl:map cl:nil #'(cl:lambda (ele) (cl:let ((bits (roslisp-utils:encode-double-float-bits ele)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream)))
   (cl:slot-value msg 'values))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <AnalogStatusMsg>) istream)
  "Deserializes a message object of type '<AnalogStatusMsg>"
  (cl:setf (cl:slot-value msg 'values) (cl:make-array 8))
  (cl:let ((vals (cl:slot-value msg 'values)))
    (cl:dotimes (i 8)
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:aref vals i) (roslisp-utils:decode-double-float-bits bits)))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<AnalogStatusMsg>)))
  "Returns string type for a message object of type '<AnalogStatusMsg>"
  "rss_msgs/AnalogStatusMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'AnalogStatusMsg)))
  "Returns string type for a message object of type 'AnalogStatusMsg"
  "rss_msgs/AnalogStatusMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<AnalogStatusMsg>)))
  "Returns md5sum for a message object of type '<AnalogStatusMsg>"
  "7e6caa6f77d06950d365d446a6483a22")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'AnalogStatusMsg)))
  "Returns md5sum for a message object of type 'AnalogStatusMsg"
  "7e6caa6f77d06950d365d446a6483a22")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<AnalogStatusMsg>)))
  "Returns full string definition for message of type '<AnalogStatusMsg>"
  (cl:format cl:nil "float64[8] values~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'AnalogStatusMsg)))
  "Returns full string definition for message of type 'AnalogStatusMsg"
  (cl:format cl:nil "float64[8] values~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <AnalogStatusMsg>))
  (cl:+ 0
     0 (cl:reduce #'cl:+ (cl:slot-value msg 'values) :key #'(cl:lambda (ele) (cl:declare (cl:ignorable ele)) (cl:+ 8)))
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <AnalogStatusMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'AnalogStatusMsg
    (cl:cons ':values (values msg))
))
