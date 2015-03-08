; Auto-generated. Do not edit!


(cl:in-package lab6_msgs-msg)


;//! \htmlinclude GUIRectMsg.msg.html

(cl:defclass <GUIRectMsg> (roslisp-msg-protocol:ros-message)
  ((c
    :reader c
    :initarg :c
    :type lab5_msgs-msg:ColorMsg
    :initform (cl:make-instance 'lab5_msgs-msg:ColorMsg))
   (x
    :reader x
    :initarg :x
    :type cl:float
    :initform 0.0)
   (y
    :reader y
    :initarg :y
    :type cl:float
    :initform 0.0)
   (width
    :reader width
    :initarg :width
    :type cl:float
    :initform 0.0)
   (height
    :reader height
    :initarg :height
    :type cl:float
    :initform 0.0)
   (filled
    :reader filled
    :initarg :filled
    :type cl:integer
    :initform 0))
)

(cl:defclass GUIRectMsg (<GUIRectMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <GUIRectMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'GUIRectMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name lab6_msgs-msg:<GUIRectMsg> is deprecated: use lab6_msgs-msg:GUIRectMsg instead.")))

(cl:ensure-generic-function 'c-val :lambda-list '(m))
(cl:defmethod c-val ((m <GUIRectMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:c-val is deprecated.  Use lab6_msgs-msg:c instead.")
  (c m))

(cl:ensure-generic-function 'x-val :lambda-list '(m))
(cl:defmethod x-val ((m <GUIRectMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:x-val is deprecated.  Use lab6_msgs-msg:x instead.")
  (x m))

(cl:ensure-generic-function 'y-val :lambda-list '(m))
(cl:defmethod y-val ((m <GUIRectMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:y-val is deprecated.  Use lab6_msgs-msg:y instead.")
  (y m))

(cl:ensure-generic-function 'width-val :lambda-list '(m))
(cl:defmethod width-val ((m <GUIRectMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:width-val is deprecated.  Use lab6_msgs-msg:width instead.")
  (width m))

(cl:ensure-generic-function 'height-val :lambda-list '(m))
(cl:defmethod height-val ((m <GUIRectMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:height-val is deprecated.  Use lab6_msgs-msg:height instead.")
  (height m))

(cl:ensure-generic-function 'filled-val :lambda-list '(m))
(cl:defmethod filled-val ((m <GUIRectMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:filled-val is deprecated.  Use lab6_msgs-msg:filled instead.")
  (filled m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <GUIRectMsg>) ostream)
  "Serializes a message object of type '<GUIRectMsg>"
  (roslisp-msg-protocol:serialize (cl:slot-value msg 'c) ostream)
  (cl:let ((bits (roslisp-utils:encode-single-float-bits (cl:slot-value msg 'x))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-single-float-bits (cl:slot-value msg 'y))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-single-float-bits (cl:slot-value msg 'width))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-single-float-bits (cl:slot-value msg 'height))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream))
  (cl:let* ((signed (cl:slot-value msg 'filled)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 4294967296) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) unsigned) ostream)
    )
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <GUIRectMsg>) istream)
  "Deserializes a message object of type '<GUIRectMsg>"
  (roslisp-msg-protocol:deserialize (cl:slot-value msg 'c) istream)
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'x) (roslisp-utils:decode-single-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'y) (roslisp-utils:decode-single-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'width) (roslisp-utils:decode-single-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'height) (roslisp-utils:decode-single-float-bits bits)))
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'filled) (cl:if (cl:< unsigned 2147483648) unsigned (cl:- unsigned 4294967296))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<GUIRectMsg>)))
  "Returns string type for a message object of type '<GUIRectMsg>"
  "lab6_msgs/GUIRectMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'GUIRectMsg)))
  "Returns string type for a message object of type 'GUIRectMsg"
  "lab6_msgs/GUIRectMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<GUIRectMsg>)))
  "Returns md5sum for a message object of type '<GUIRectMsg>"
  "c0bf17dcdde695b717319df27b23e5f8")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'GUIRectMsg)))
  "Returns md5sum for a message object of type 'GUIRectMsg"
  "c0bf17dcdde695b717319df27b23e5f8")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<GUIRectMsg>)))
  "Returns full string definition for message of type '<GUIRectMsg>"
  (cl:format cl:nil "lab5_msgs/ColorMsg c~%float32 x~%float32 y~%float32 width~%float32 height~%int32 filled~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'GUIRectMsg)))
  "Returns full string definition for message of type 'GUIRectMsg"
  (cl:format cl:nil "lab5_msgs/ColorMsg c~%float32 x~%float32 y~%float32 width~%float32 height~%int32 filled~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <GUIRectMsg>))
  (cl:+ 0
     (roslisp-msg-protocol:serialization-length (cl:slot-value msg 'c))
     4
     4
     4
     4
     4
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <GUIRectMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'GUIRectMsg
    (cl:cons ':c (c msg))
    (cl:cons ':x (x msg))
    (cl:cons ':y (y msg))
    (cl:cons ':width (width msg))
    (cl:cons ':height (height msg))
    (cl:cons ':filled (filled msg))
))
