; Auto-generated. Do not edit!


(cl:in-package lab5_msgs-msg)


;//! \htmlinclude GUIPointMsg.msg.html

(cl:defclass <GUIPointMsg> (roslisp-msg-protocol:ros-message)
  ((x
    :reader x
    :initarg :x
    :type cl:float
    :initform 0.0)
   (y
    :reader y
    :initarg :y
    :type cl:float
    :initform 0.0)
   (shape
    :reader shape
    :initarg :shape
    :type cl:integer
    :initform 0)
   (color
    :reader color
    :initarg :color
    :type lab5_msgs-msg:ColorMsg
    :initform (cl:make-instance 'lab5_msgs-msg:ColorMsg)))
)

(cl:defclass GUIPointMsg (<GUIPointMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <GUIPointMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'GUIPointMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name lab5_msgs-msg:<GUIPointMsg> is deprecated: use lab5_msgs-msg:GUIPointMsg instead.")))

(cl:ensure-generic-function 'x-val :lambda-list '(m))
(cl:defmethod x-val ((m <GUIPointMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:x-val is deprecated.  Use lab5_msgs-msg:x instead.")
  (x m))

(cl:ensure-generic-function 'y-val :lambda-list '(m))
(cl:defmethod y-val ((m <GUIPointMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:y-val is deprecated.  Use lab5_msgs-msg:y instead.")
  (y m))

(cl:ensure-generic-function 'shape-val :lambda-list '(m))
(cl:defmethod shape-val ((m <GUIPointMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:shape-val is deprecated.  Use lab5_msgs-msg:shape instead.")
  (shape m))

(cl:ensure-generic-function 'color-val :lambda-list '(m))
(cl:defmethod color-val ((m <GUIPointMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:color-val is deprecated.  Use lab5_msgs-msg:color instead.")
  (color m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <GUIPointMsg>) ostream)
  "Serializes a message object of type '<GUIPointMsg>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'x))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'y))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let* ((signed (cl:slot-value msg 'shape)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 18446744073709551616) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) unsigned) ostream)
    )
  (roslisp-msg-protocol:serialize (cl:slot-value msg 'color) ostream)
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <GUIPointMsg>) istream)
  "Deserializes a message object of type '<GUIPointMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'x) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'y) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'shape) (cl:if (cl:< unsigned 9223372036854775808) unsigned (cl:- unsigned 18446744073709551616))))
  (roslisp-msg-protocol:deserialize (cl:slot-value msg 'color) istream)
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<GUIPointMsg>)))
  "Returns string type for a message object of type '<GUIPointMsg>"
  "lab5_msgs/GUIPointMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'GUIPointMsg)))
  "Returns string type for a message object of type 'GUIPointMsg"
  "lab5_msgs/GUIPointMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<GUIPointMsg>)))
  "Returns md5sum for a message object of type '<GUIPointMsg>"
  "88fef43974ed58433a24c05c8e4d27fd")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'GUIPointMsg)))
  "Returns md5sum for a message object of type 'GUIPointMsg"
  "88fef43974ed58433a24c05c8e4d27fd")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<GUIPointMsg>)))
  "Returns full string definition for message of type '<GUIPointMsg>"
  (cl:format cl:nil "float64 x~%float64 y~%int64 shape~%ColorMsg color~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'GUIPointMsg)))
  "Returns full string definition for message of type 'GUIPointMsg"
  (cl:format cl:nil "float64 x~%float64 y~%int64 shape~%ColorMsg color~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <GUIPointMsg>))
  (cl:+ 0
     8
     8
     8
     (roslisp-msg-protocol:serialization-length (cl:slot-value msg 'color))
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <GUIPointMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'GUIPointMsg
    (cl:cons ':x (x msg))
    (cl:cons ':y (y msg))
    (cl:cons ':shape (shape msg))
    (cl:cons ':color (color msg))
))
