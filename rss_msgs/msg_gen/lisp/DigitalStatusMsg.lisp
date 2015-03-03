; Auto-generated. Do not edit!


(cl:in-package rss_msgs-msg)


;//! \htmlinclude DigitalStatusMsg.msg.html

(cl:defclass <DigitalStatusMsg> (roslisp-msg-protocol:ros-message)
  ((slow
    :reader slow
    :initarg :slow
    :type (cl:vector cl:boolean)
   :initform (cl:make-array 8 :element-type 'cl:boolean :initial-element cl:nil))
   (fast
    :reader fast
    :initarg :fast
    :type (cl:vector cl:boolean)
   :initform (cl:make-array 8 :element-type 'cl:boolean :initial-element cl:nil)))
)

(cl:defclass DigitalStatusMsg (<DigitalStatusMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <DigitalStatusMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'DigitalStatusMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name rss_msgs-msg:<DigitalStatusMsg> is deprecated: use rss_msgs-msg:DigitalStatusMsg instead.")))

(cl:ensure-generic-function 'slow-val :lambda-list '(m))
(cl:defmethod slow-val ((m <DigitalStatusMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:slow-val is deprecated.  Use rss_msgs-msg:slow instead.")
  (slow m))

(cl:ensure-generic-function 'fast-val :lambda-list '(m))
(cl:defmethod fast-val ((m <DigitalStatusMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:fast-val is deprecated.  Use rss_msgs-msg:fast instead.")
  (fast m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <DigitalStatusMsg>) ostream)
  "Serializes a message object of type '<DigitalStatusMsg>"
  (cl:map cl:nil #'(cl:lambda (ele) (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:if ele 1 0)) ostream))
   (cl:slot-value msg 'slow))
  (cl:map cl:nil #'(cl:lambda (ele) (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:if ele 1 0)) ostream))
   (cl:slot-value msg 'fast))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <DigitalStatusMsg>) istream)
  "Deserializes a message object of type '<DigitalStatusMsg>"
  (cl:setf (cl:slot-value msg 'slow) (cl:make-array 8))
  (cl:let ((vals (cl:slot-value msg 'slow)))
    (cl:dotimes (i 8)
    (cl:setf (cl:aref vals i) (cl:not (cl:zerop (cl:read-byte istream))))))
  (cl:setf (cl:slot-value msg 'fast) (cl:make-array 8))
  (cl:let ((vals (cl:slot-value msg 'fast)))
    (cl:dotimes (i 8)
    (cl:setf (cl:aref vals i) (cl:not (cl:zerop (cl:read-byte istream))))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<DigitalStatusMsg>)))
  "Returns string type for a message object of type '<DigitalStatusMsg>"
  "rss_msgs/DigitalStatusMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'DigitalStatusMsg)))
  "Returns string type for a message object of type 'DigitalStatusMsg"
  "rss_msgs/DigitalStatusMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<DigitalStatusMsg>)))
  "Returns md5sum for a message object of type '<DigitalStatusMsg>"
  "7a2af7d23e6ef46fbd1d9066e3666f04")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'DigitalStatusMsg)))
  "Returns md5sum for a message object of type 'DigitalStatusMsg"
  "7a2af7d23e6ef46fbd1d9066e3666f04")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<DigitalStatusMsg>)))
  "Returns full string definition for message of type '<DigitalStatusMsg>"
  (cl:format cl:nil "bool[8] slow~%bool[8] fast~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'DigitalStatusMsg)))
  "Returns full string definition for message of type 'DigitalStatusMsg"
  (cl:format cl:nil "bool[8] slow~%bool[8] fast~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <DigitalStatusMsg>))
  (cl:+ 0
     0 (cl:reduce #'cl:+ (cl:slot-value msg 'slow) :key #'(cl:lambda (ele) (cl:declare (cl:ignorable ele)) (cl:+ 1)))
     0 (cl:reduce #'cl:+ (cl:slot-value msg 'fast) :key #'(cl:lambda (ele) (cl:declare (cl:ignorable ele)) (cl:+ 1)))
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <DigitalStatusMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'DigitalStatusMsg
    (cl:cons ':slow (slow msg))
    (cl:cons ':fast (fast msg))
))
