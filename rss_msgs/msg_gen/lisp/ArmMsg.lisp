; Auto-generated. Do not edit!


(cl:in-package rss_msgs-msg)


;//! \htmlinclude ArmMsg.msg.html

(cl:defclass <ArmMsg> (roslisp-msg-protocol:ros-message)
  ((pwms
    :reader pwms
    :initarg :pwms
    :type (cl:vector cl:integer)
   :initform (cl:make-array 8 :element-type 'cl:integer :initial-element 0)))
)

(cl:defclass ArmMsg (<ArmMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <ArmMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'ArmMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name rss_msgs-msg:<ArmMsg> is deprecated: use rss_msgs-msg:ArmMsg instead.")))

(cl:ensure-generic-function 'pwms-val :lambda-list '(m))
(cl:defmethod pwms-val ((m <ArmMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:pwms-val is deprecated.  Use rss_msgs-msg:pwms instead.")
  (pwms m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <ArmMsg>) ostream)
  "Serializes a message object of type '<ArmMsg>"
  (cl:map cl:nil #'(cl:lambda (ele) (cl:let* ((signed ele) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 18446744073709551616) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) unsigned) ostream)
    ))
   (cl:slot-value msg 'pwms))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <ArmMsg>) istream)
  "Deserializes a message object of type '<ArmMsg>"
  (cl:setf (cl:slot-value msg 'pwms) (cl:make-array 8))
  (cl:let ((vals (cl:slot-value msg 'pwms)))
    (cl:dotimes (i 8)
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) unsigned) (cl:read-byte istream))
      (cl:setf (cl:aref vals i) (cl:if (cl:< unsigned 9223372036854775808) unsigned (cl:- unsigned 18446744073709551616))))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<ArmMsg>)))
  "Returns string type for a message object of type '<ArmMsg>"
  "rss_msgs/ArmMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'ArmMsg)))
  "Returns string type for a message object of type 'ArmMsg"
  "rss_msgs/ArmMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<ArmMsg>)))
  "Returns md5sum for a message object of type '<ArmMsg>"
  "6de3c9cc70fe3f9be077ade811350687")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'ArmMsg)))
  "Returns md5sum for a message object of type 'ArmMsg"
  "6de3c9cc70fe3f9be077ade811350687")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<ArmMsg>)))
  "Returns full string definition for message of type '<ArmMsg>"
  (cl:format cl:nil "int64[8] pwms~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'ArmMsg)))
  "Returns full string definition for message of type 'ArmMsg"
  (cl:format cl:nil "int64[8] pwms~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <ArmMsg>))
  (cl:+ 0
     0 (cl:reduce #'cl:+ (cl:slot-value msg 'pwms) :key #'(cl:lambda (ele) (cl:declare (cl:ignorable ele)) (cl:+ 8)))
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <ArmMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'ArmMsg
    (cl:cons ':pwms (pwms msg))
))
