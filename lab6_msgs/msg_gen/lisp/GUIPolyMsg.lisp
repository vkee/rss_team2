; Auto-generated. Do not edit!


(cl:in-package lab6_msgs-msg)


;//! \htmlinclude GUIPolyMsg.msg.html

(cl:defclass <GUIPolyMsg> (roslisp-msg-protocol:ros-message)
  ((c
    :reader c
    :initarg :c
    :type lab5_msgs-msg:ColorMsg
    :initform (cl:make-instance 'lab5_msgs-msg:ColorMsg))
   (numVertices
    :reader numVertices
    :initarg :numVertices
    :type cl:integer
    :initform 0)
   (x
    :reader x
    :initarg :x
    :type (cl:vector cl:float)
   :initform (cl:make-array 0 :element-type 'cl:float :initial-element 0.0))
   (y
    :reader y
    :initarg :y
    :type (cl:vector cl:float)
   :initform (cl:make-array 0 :element-type 'cl:float :initial-element 0.0))
   (closed
    :reader closed
    :initarg :closed
    :type cl:integer
    :initform 0)
   (filled
    :reader filled
    :initarg :filled
    :type cl:integer
    :initform 0))
)

(cl:defclass GUIPolyMsg (<GUIPolyMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <GUIPolyMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'GUIPolyMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name lab6_msgs-msg:<GUIPolyMsg> is deprecated: use lab6_msgs-msg:GUIPolyMsg instead.")))

(cl:ensure-generic-function 'c-val :lambda-list '(m))
(cl:defmethod c-val ((m <GUIPolyMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:c-val is deprecated.  Use lab6_msgs-msg:c instead.")
  (c m))

(cl:ensure-generic-function 'numVertices-val :lambda-list '(m))
(cl:defmethod numVertices-val ((m <GUIPolyMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:numVertices-val is deprecated.  Use lab6_msgs-msg:numVertices instead.")
  (numVertices m))

(cl:ensure-generic-function 'x-val :lambda-list '(m))
(cl:defmethod x-val ((m <GUIPolyMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:x-val is deprecated.  Use lab6_msgs-msg:x instead.")
  (x m))

(cl:ensure-generic-function 'y-val :lambda-list '(m))
(cl:defmethod y-val ((m <GUIPolyMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:y-val is deprecated.  Use lab6_msgs-msg:y instead.")
  (y m))

(cl:ensure-generic-function 'closed-val :lambda-list '(m))
(cl:defmethod closed-val ((m <GUIPolyMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:closed-val is deprecated.  Use lab6_msgs-msg:closed instead.")
  (closed m))

(cl:ensure-generic-function 'filled-val :lambda-list '(m))
(cl:defmethod filled-val ((m <GUIPolyMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader lab6_msgs-msg:filled-val is deprecated.  Use lab6_msgs-msg:filled instead.")
  (filled m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <GUIPolyMsg>) ostream)
  "Serializes a message object of type '<GUIPolyMsg>"
  (roslisp-msg-protocol:serialize (cl:slot-value msg 'c) ostream)
  (cl:let* ((signed (cl:slot-value msg 'numVertices)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 4294967296) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) unsigned) ostream)
    )
  (cl:let ((__ros_arr_len (cl:length (cl:slot-value msg 'x))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) __ros_arr_len) ostream))
  (cl:map cl:nil #'(cl:lambda (ele) (cl:let ((bits (roslisp-utils:encode-single-float-bits ele)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)))
   (cl:slot-value msg 'x))
  (cl:let ((__ros_arr_len (cl:length (cl:slot-value msg 'y))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) __ros_arr_len) ostream))
  (cl:map cl:nil #'(cl:lambda (ele) (cl:let ((bits (roslisp-utils:encode-single-float-bits ele)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)))
   (cl:slot-value msg 'y))
  (cl:let* ((signed (cl:slot-value msg 'closed)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 4294967296) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) unsigned) ostream)
    )
  (cl:let* ((signed (cl:slot-value msg 'filled)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 4294967296) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) unsigned) ostream)
    )
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <GUIPolyMsg>) istream)
  "Deserializes a message object of type '<GUIPolyMsg>"
  (roslisp-msg-protocol:deserialize (cl:slot-value msg 'c) istream)
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'numVertices) (cl:if (cl:< unsigned 2147483648) unsigned (cl:- unsigned 4294967296))))
  (cl:let ((__ros_arr_len 0))
    (cl:setf (cl:ldb (cl:byte 8 0) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 8) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 16) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 24) __ros_arr_len) (cl:read-byte istream))
  (cl:setf (cl:slot-value msg 'x) (cl:make-array __ros_arr_len))
  (cl:let ((vals (cl:slot-value msg 'x)))
    (cl:dotimes (i __ros_arr_len)
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:aref vals i) (roslisp-utils:decode-single-float-bits bits))))))
  (cl:let ((__ros_arr_len 0))
    (cl:setf (cl:ldb (cl:byte 8 0) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 8) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 16) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 24) __ros_arr_len) (cl:read-byte istream))
  (cl:setf (cl:slot-value msg 'y) (cl:make-array __ros_arr_len))
  (cl:let ((vals (cl:slot-value msg 'y)))
    (cl:dotimes (i __ros_arr_len)
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:aref vals i) (roslisp-utils:decode-single-float-bits bits))))))
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'closed) (cl:if (cl:< unsigned 2147483648) unsigned (cl:- unsigned 4294967296))))
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'filled) (cl:if (cl:< unsigned 2147483648) unsigned (cl:- unsigned 4294967296))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<GUIPolyMsg>)))
  "Returns string type for a message object of type '<GUIPolyMsg>"
  "lab6_msgs/GUIPolyMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'GUIPolyMsg)))
  "Returns string type for a message object of type 'GUIPolyMsg"
  "lab6_msgs/GUIPolyMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<GUIPolyMsg>)))
  "Returns md5sum for a message object of type '<GUIPolyMsg>"
  "3e548996167a0b4f0dd625274639c5b7")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'GUIPolyMsg)))
  "Returns md5sum for a message object of type 'GUIPolyMsg"
  "3e548996167a0b4f0dd625274639c5b7")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<GUIPolyMsg>)))
  "Returns full string definition for message of type '<GUIPolyMsg>"
  (cl:format cl:nil "lab5_msgs/ColorMsg c~%int32 numVertices~%float32[] x~%float32[] y~%int32 closed~%int32 filled~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'GUIPolyMsg)))
  "Returns full string definition for message of type 'GUIPolyMsg"
  (cl:format cl:nil "lab5_msgs/ColorMsg c~%int32 numVertices~%float32[] x~%float32[] y~%int32 closed~%int32 filled~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <GUIPolyMsg>))
  (cl:+ 0
     (roslisp-msg-protocol:serialization-length (cl:slot-value msg 'c))
     4
     4 (cl:reduce #'cl:+ (cl:slot-value msg 'x) :key #'(cl:lambda (ele) (cl:declare (cl:ignorable ele)) (cl:+ 4)))
     4 (cl:reduce #'cl:+ (cl:slot-value msg 'y) :key #'(cl:lambda (ele) (cl:declare (cl:ignorable ele)) (cl:+ 4)))
     4
     4
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <GUIPolyMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'GUIPolyMsg
    (cl:cons ':c (c msg))
    (cl:cons ':numVertices (numVertices msg))
    (cl:cons ':x (x msg))
    (cl:cons ':y (y msg))
    (cl:cons ':closed (closed msg))
    (cl:cons ':filled (filled msg))
))
