; Auto-generated. Do not edit!


(cl:in-package lab5_msgs-msg)


;//! \htmlinclude GUISegmentMsg.msg.html

(cl:defclass <GUISegmentMsg> (roslisp-msg-protocol:ros-message)
  ((startX
    :reader startX
    :initarg :startX
    :type cl:float
    :initform 0.0)
   (endX
    :reader endX
    :initarg :endX
    :type cl:float
    :initform 0.0)
   (startY
    :reader startY
    :initarg :startY
    :type cl:float
    :initform 0.0)
   (endY
    :reader endY
    :initarg :endY
    :type cl:float
    :initform 0.0)
   (color
    :reader color
    :initarg :color
    :type lab5_msgs-msg:ColorMsg
    :initform (cl:make-instance 'lab5_msgs-msg:ColorMsg)))
)

(cl:defclass GUISegmentMsg (<GUISegmentMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <GUISegmentMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'GUISegmentMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name lab5_msgs-msg:<GUISegmentMsg> is deprecated: use lab5_msgs-msg:GUISegmentMsg instead.")))

(cl:ensure-generic-function 'startX-val :lambda-list '(m))
(cl:defmethod startX-val ((m <GUISegmentMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:startX-val is deprecated.  Use lab5_msgs-msg:startX instead.")
  (startX m))

(cl:ensure-generic-function 'endX-val :lambda-list '(m))
(cl:defmethod endX-val ((m <GUISegmentMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:endX-val is deprecated.  Use lab5_msgs-msg:endX instead.")
  (endX m))

(cl:ensure-generic-function 'startY-val :lambda-list '(m))
(cl:defmethod startY-val ((m <GUISegmentMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:startY-val is deprecated.  Use lab5_msgs-msg:startY instead.")
  (startY m))

(cl:ensure-generic-function 'endY-val :lambda-list '(m))
(cl:defmethod endY-val ((m <GUISegmentMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:endY-val is deprecated.  Use lab5_msgs-msg:endY instead.")
  (endY m))

(cl:ensure-generic-function 'color-val :lambda-list '(m))
(cl:defmethod color-val ((m <GUISegmentMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:color-val is deprecated.  Use lab5_msgs-msg:color instead.")
  (color m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <GUISegmentMsg>) ostream)
  "Serializes a message object of type '<GUISegmentMsg>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'startX))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'endX))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'startY))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'endY))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (roslisp-msg-protocol:serialize (cl:slot-value msg 'color) ostream)
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <GUISegmentMsg>) istream)
  "Deserializes a message object of type '<GUISegmentMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'startX) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'endX) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'startY) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'endY) (roslisp-utils:decode-double-float-bits bits)))
  (roslisp-msg-protocol:deserialize (cl:slot-value msg 'color) istream)
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<GUISegmentMsg>)))
  "Returns string type for a message object of type '<GUISegmentMsg>"
  "lab5_msgs/GUISegmentMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'GUISegmentMsg)))
  "Returns string type for a message object of type 'GUISegmentMsg"
  "lab5_msgs/GUISegmentMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<GUISegmentMsg>)))
  "Returns md5sum for a message object of type '<GUISegmentMsg>"
  "0c882de09f5b3bc620b839f4d58554ba")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'GUISegmentMsg)))
  "Returns md5sum for a message object of type 'GUISegmentMsg"
  "0c882de09f5b3bc620b839f4d58554ba")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<GUISegmentMsg>)))
  "Returns full string definition for message of type '<GUISegmentMsg>"
  (cl:format cl:nil "float64 startX~%float64 endX~%float64 startY~%float64 endY~%ColorMsg color~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'GUISegmentMsg)))
  "Returns full string definition for message of type 'GUISegmentMsg"
  (cl:format cl:nil "float64 startX~%float64 endX~%float64 startY~%float64 endY~%ColorMsg color~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <GUISegmentMsg>))
  (cl:+ 0
     8
     8
     8
     8
     (roslisp-msg-protocol:serialization-length (cl:slot-value msg 'color))
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <GUISegmentMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'GUISegmentMsg
    (cl:cons ':startX (startX msg))
    (cl:cons ':endX (endX msg))
    (cl:cons ':startY (startY msg))
    (cl:cons ':endY (endY msg))
    (cl:cons ':color (color msg))
))
