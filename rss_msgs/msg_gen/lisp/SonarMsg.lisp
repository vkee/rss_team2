; Auto-generated. Do not edit!


(cl:in-package rss_msgs-msg)


;//! \htmlinclude SonarMsg.msg.html

(cl:defclass <SonarMsg> (roslisp-msg-protocol:ros-message)
  ((range
    :reader range
    :initarg :range
    :type cl:float
    :initform 0.0)
   (isFront
    :reader isFront
    :initarg :isFront
    :type cl:boolean
    :initform cl:nil))
)

(cl:defclass SonarMsg (<SonarMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <SonarMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'SonarMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name rss_msgs-msg:<SonarMsg> is deprecated: use rss_msgs-msg:SonarMsg instead.")))

(cl:ensure-generic-function 'range-val :lambda-list '(m))
(cl:defmethod range-val ((m <SonarMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:range-val is deprecated.  Use rss_msgs-msg:range instead.")
  (range m))

(cl:ensure-generic-function 'isFront-val :lambda-list '(m))
(cl:defmethod isFront-val ((m <SonarMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:isFront-val is deprecated.  Use rss_msgs-msg:isFront instead.")
  (isFront m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <SonarMsg>) ostream)
  "Serializes a message object of type '<SonarMsg>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'range))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:if (cl:slot-value msg 'isFront) 1 0)) ostream)
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <SonarMsg>) istream)
  "Deserializes a message object of type '<SonarMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'range) (roslisp-utils:decode-double-float-bits bits)))
    (cl:setf (cl:slot-value msg 'isFront) (cl:not (cl:zerop (cl:read-byte istream))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<SonarMsg>)))
  "Returns string type for a message object of type '<SonarMsg>"
  "rss_msgs/SonarMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'SonarMsg)))
  "Returns string type for a message object of type 'SonarMsg"
  "rss_msgs/SonarMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<SonarMsg>)))
  "Returns md5sum for a message object of type '<SonarMsg>"
  "bca27647dbedaf9bc9f2f5ef58a241bc")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'SonarMsg)))
  "Returns md5sum for a message object of type 'SonarMsg"
  "bca27647dbedaf9bc9f2f5ef58a241bc")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<SonarMsg>)))
  "Returns full string definition for message of type '<SonarMsg>"
  (cl:format cl:nil "float64 range~%bool isFront~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'SonarMsg)))
  "Returns full string definition for message of type 'SonarMsg"
  (cl:format cl:nil "float64 range~%bool isFront~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <SonarMsg>))
  (cl:+ 0
     8
     1
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <SonarMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'SonarMsg
    (cl:cons ':range (range msg))
    (cl:cons ':isFront (isFront msg))
))
