; Auto-generated. Do not edit!


(cl:in-package lab5_msgs-msg)


;//! \htmlinclude GUILineMsg.msg.html

(cl:defclass <GUILineMsg> (roslisp-msg-protocol:ros-message)
  ((lineA
    :reader lineA
    :initarg :lineA
    :type cl:float
    :initform 0.0)
   (lineB
    :reader lineB
    :initarg :lineB
    :type cl:float
    :initform 0.0)
   (lineC
    :reader lineC
    :initarg :lineC
    :type cl:float
    :initform 0.0)
   (color
    :reader color
    :initarg :color
    :type lab5_msgs-msg:ColorMsg
    :initform (cl:make-instance 'lab5_msgs-msg:ColorMsg)))
)

(cl:defclass GUILineMsg (<GUILineMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <GUILineMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'GUILineMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name lab5_msgs-msg:<GUILineMsg> is deprecated: use lab5_msgs-msg:GUILineMsg instead.")))

(cl:ensure-generic-function 'lineA-val :lambda-list '(m))
(cl:defmethod lineA-val ((m <GUILineMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:lineA-val is deprecated.  Use lab5_msgs-msg:lineA instead.")
  (lineA m))

(cl:ensure-generic-function 'lineB-val :lambda-list '(m))
(cl:defmethod lineB-val ((m <GUILineMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:lineB-val is deprecated.  Use lab5_msgs-msg:lineB instead.")
  (lineB m))

(cl:ensure-generic-function 'lineC-val :lambda-list '(m))
(cl:defmethod lineC-val ((m <GUILineMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:lineC-val is deprecated.  Use lab5_msgs-msg:lineC instead.")
  (lineC m))

(cl:ensure-generic-function 'color-val :lambda-list '(m))
(cl:defmethod color-val ((m <GUILineMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab5_msgs-msg:color-val is deprecated.  Use lab5_msgs-msg:color instead.")
  (color m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <GUILineMsg>) ostream)
  "Serializes a message object of type '<GUILineMsg>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'lineA))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'lineB))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'lineC))))
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
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <GUILineMsg>) istream)
  "Deserializes a message object of type '<GUILineMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'lineA) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'lineB) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'lineC) (roslisp-utils:decode-double-float-bits bits)))
  (roslisp-msg-protocol:deserialize (cl:slot-value msg 'color) istream)
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<GUILineMsg>)))
  "Returns string type for a message object of type '<GUILineMsg>"
  "lab5_msgs/GUILineMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'GUILineMsg)))
  "Returns string type for a message object of type 'GUILineMsg"
  "lab5_msgs/GUILineMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<GUILineMsg>)))
  "Returns md5sum for a message object of type '<GUILineMsg>"
  "5ee858aa33ac4230e8ab893c71e571e8")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'GUILineMsg)))
  "Returns md5sum for a message object of type 'GUILineMsg"
  "5ee858aa33ac4230e8ab893c71e571e8")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<GUILineMsg>)))
  "Returns full string definition for message of type '<GUILineMsg>"
  (cl:format cl:nil "float64 lineA~%float64 lineB~%float64 lineC~%ColorMsg color~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'GUILineMsg)))
  "Returns full string definition for message of type 'GUILineMsg"
  (cl:format cl:nil "float64 lineA~%float64 lineB~%float64 lineC~%ColorMsg color~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <GUILineMsg>))
  (cl:+ 0
     8
     8
     8
     (roslisp-msg-protocol:serialization-length (cl:slot-value msg 'color))
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <GUILineMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'GUILineMsg
    (cl:cons ':lineA (lineA msg))
    (cl:cons ':lineB (lineB msg))
    (cl:cons ':lineC (lineC msg))
    (cl:cons ':color (color msg))
))
