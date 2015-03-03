; Auto-generated. Do not edit!


(cl:in-package rss_msgs-msg)


;//! \htmlinclude EncoderMsg.msg.html

(cl:defclass <EncoderMsg> (roslisp-msg-protocol:ros-message)
  ((left
    :reader left
    :initarg :left
    :type cl:integer
    :initform 0)
   (right
    :reader right
    :initarg :right
    :type cl:integer
    :initform 0))
)

(cl:defclass EncoderMsg (<EncoderMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <EncoderMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'EncoderMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name rss_msgs-msg:<EncoderMsg> is deprecated: use rss_msgs-msg:EncoderMsg instead.")))

(cl:ensure-generic-function 'left-val :lambda-list '(m))
(cl:defmethod left-val ((m <EncoderMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:left-val is deprecated.  Use rss_msgs-msg:left instead.")
  (left m))

(cl:ensure-generic-function 'right-val :lambda-list '(m))
(cl:defmethod right-val ((m <EncoderMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:right-val is deprecated.  Use rss_msgs-msg:right instead.")
  (right m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <EncoderMsg>) ostream)
  "Serializes a message object of type '<EncoderMsg>"
  (cl:let* ((signed (cl:slot-value msg 'left)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 18446744073709551616) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) unsigned) ostream)
    )
  (cl:let* ((signed (cl:slot-value msg 'right)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 18446744073709551616) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) unsigned) ostream)
    )
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <EncoderMsg>) istream)
  "Deserializes a message object of type '<EncoderMsg>"
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'left) (cl:if (cl:< unsigned 9223372036854775808) unsigned (cl:- unsigned 18446744073709551616))))
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'right) (cl:if (cl:< unsigned 9223372036854775808) unsigned (cl:- unsigned 18446744073709551616))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<EncoderMsg>)))
  "Returns string type for a message object of type '<EncoderMsg>"
  "rss_msgs/EncoderMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'EncoderMsg)))
  "Returns string type for a message object of type 'EncoderMsg"
  "rss_msgs/EncoderMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<EncoderMsg>)))
  "Returns md5sum for a message object of type '<EncoderMsg>"
  "10ec03ad83d596042691890fad8ead6e")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'EncoderMsg)))
  "Returns md5sum for a message object of type 'EncoderMsg"
  "10ec03ad83d596042691890fad8ead6e")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<EncoderMsg>)))
  "Returns full string definition for message of type '<EncoderMsg>"
  (cl:format cl:nil "int64 left~%int64 right~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'EncoderMsg)))
  "Returns full string definition for message of type 'EncoderMsg"
  (cl:format cl:nil "int64 left~%int64 right~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <EncoderMsg>))
  (cl:+ 0
     8
     8
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <EncoderMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'EncoderMsg
    (cl:cons ':left (left msg))
    (cl:cons ':right (right msg))
))
