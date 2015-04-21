; Auto-generated. Do not edit!


(cl:in-package Challenge_msgs-msg)


;//! \htmlinclude GUIStringMessage.msg.html

(cl:defclass <GUIStringMessage> (roslisp-msg-protocol:ros-message)
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
   (text
    :reader text
    :initarg :text
    :type cl:string
    :initform ""))
)

(cl:defclass GUIStringMessage (<GUIStringMessage>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <GUIStringMessage>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'GUIStringMessage)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name Challenge_msgs-msg:<GUIStringMessage> is deprecated: use Challenge_msgs-msg:GUIStringMessage instead.")))

(cl:ensure-generic-function 'c-val :lambda-list '(m))
(cl:defmethod c-val ((m <GUIStringMessage>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader Challenge_msgs-msg:c-val is deprecated.  Use Challenge_msgs-msg:c instead.")
  (c m))

(cl:ensure-generic-function 'x-val :lambda-list '(m))
(cl:defmethod x-val ((m <GUIStringMessage>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader Challenge_msgs-msg:x-val is deprecated.  Use Challenge_msgs-msg:x instead.")
  (x m))

(cl:ensure-generic-function 'y-val :lambda-list '(m))
(cl:defmethod y-val ((m <GUIStringMessage>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader Challenge_msgs-msg:y-val is deprecated.  Use Challenge_msgs-msg:y instead.")
  (y m))

(cl:ensure-generic-function 'text-val :lambda-list '(m))
(cl:defmethod text-val ((m <GUIStringMessage>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader Challenge_msgs-msg:text-val is deprecated.  Use Challenge_msgs-msg:text instead.")
  (text m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <GUIStringMessage>) ostream)
  "Serializes a message object of type '<GUIStringMessage>"
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
  (cl:let ((__ros_str_len (cl:length (cl:slot-value msg 'text))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) __ros_str_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) __ros_str_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) __ros_str_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) __ros_str_len) ostream))
  (cl:map cl:nil #'(cl:lambda (c) (cl:write-byte (cl:char-code c) ostream)) (cl:slot-value msg 'text))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <GUIStringMessage>) istream)
  "Deserializes a message object of type '<GUIStringMessage>"
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
    (cl:let ((__ros_str_len 0))
      (cl:setf (cl:ldb (cl:byte 8 0) __ros_str_len) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) __ros_str_len) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) __ros_str_len) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) __ros_str_len) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'text) (cl:make-string __ros_str_len))
      (cl:dotimes (__ros_str_idx __ros_str_len msg)
        (cl:setf (cl:char (cl:slot-value msg 'text) __ros_str_idx) (cl:code-char (cl:read-byte istream)))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<GUIStringMessage>)))
  "Returns string type for a message object of type '<GUIStringMessage>"
  "Challenge_msgs/GUIStringMessage")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'GUIStringMessage)))
  "Returns string type for a message object of type 'GUIStringMessage"
  "Challenge_msgs/GUIStringMessage")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<GUIStringMessage>)))
  "Returns md5sum for a message object of type '<GUIStringMessage>"
  "171646c1eb3d834632dc6fe2c47d66aa")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'GUIStringMessage)))
  "Returns md5sum for a message object of type 'GUIStringMessage"
  "171646c1eb3d834632dc6fe2c47d66aa")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<GUIStringMessage>)))
  "Returns full string definition for message of type '<GUIStringMessage>"
  (cl:format cl:nil "lab5_msgs/ColorMsg c~%float32 x~%float32 y~%string text~%~%~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'GUIStringMessage)))
  "Returns full string definition for message of type 'GUIStringMessage"
  (cl:format cl:nil "lab5_msgs/ColorMsg c~%float32 x~%float32 y~%string text~%~%~%================================================================================~%MSG: lab5_msgs/ColorMsg~%int64 r~%int64 g~%int64 b~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <GUIStringMessage>))
  (cl:+ 0
     (roslisp-msg-protocol:serialization-length (cl:slot-value msg 'c))
     4
     4
     4 (cl:length (cl:slot-value msg 'text))
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <GUIStringMessage>))
  "Converts a ROS message object to a list"
  (cl:list 'GUIStringMessage
    (cl:cons ':c (c msg))
    (cl:cons ':x (x msg))
    (cl:cons ':y (y msg))
    (cl:cons ':text (text msg))
))
