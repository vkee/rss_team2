; Auto-generated. Do not edit!


(cl:in-package rss_msgs-msg)


;//! \htmlinclude MotionMsg.msg.html

(cl:defclass <MotionMsg> (roslisp-msg-protocol:ros-message)
  ((translationalVelocity
    :reader translationalVelocity
    :initarg :translationalVelocity
    :type cl:float
    :initform 0.0)
   (rotationalVelocity
    :reader rotationalVelocity
    :initarg :rotationalVelocity
    :type cl:float
    :initform 0.0))
)

(cl:defclass MotionMsg (<MotionMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <MotionMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'MotionMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name rss_msgs-msg:<MotionMsg> is deprecated: use rss_msgs-msg:MotionMsg instead.")))

(cl:ensure-generic-function 'translationalVelocity-val :lambda-list '(m))
(cl:defmethod translationalVelocity-val ((m <MotionMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:translationalVelocity-val is deprecated.  Use rss_msgs-msg:translationalVelocity instead.")
  (translationalVelocity m))

(cl:ensure-generic-function 'rotationalVelocity-val :lambda-list '(m))
(cl:defmethod rotationalVelocity-val ((m <MotionMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:rotationalVelocity-val is deprecated.  Use rss_msgs-msg:rotationalVelocity instead.")
  (rotationalVelocity m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <MotionMsg>) ostream)
  "Serializes a message object of type '<MotionMsg>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'translationalVelocity))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'rotationalVelocity))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <MotionMsg>) istream)
  "Deserializes a message object of type '<MotionMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'translationalVelocity) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'rotationalVelocity) (roslisp-utils:decode-double-float-bits bits)))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<MotionMsg>)))
  "Returns string type for a message object of type '<MotionMsg>"
  "rss_msgs/MotionMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'MotionMsg)))
  "Returns string type for a message object of type 'MotionMsg"
  "rss_msgs/MotionMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<MotionMsg>)))
  "Returns md5sum for a message object of type '<MotionMsg>"
  "dcd1efab7b0193f9e7f726b14abbf015")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'MotionMsg)))
  "Returns md5sum for a message object of type 'MotionMsg"
  "dcd1efab7b0193f9e7f726b14abbf015")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<MotionMsg>)))
  "Returns full string definition for message of type '<MotionMsg>"
  (cl:format cl:nil "float64 translationalVelocity~%float64 rotationalVelocity~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'MotionMsg)))
  "Returns full string definition for message of type 'MotionMsg"
  (cl:format cl:nil "float64 translationalVelocity~%float64 rotationalVelocity~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <MotionMsg>))
  (cl:+ 0
     8
     8
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <MotionMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'MotionMsg
    (cl:cons ':translationalVelocity (translationalVelocity msg))
    (cl:cons ':rotationalVelocity (rotationalVelocity msg))
))
