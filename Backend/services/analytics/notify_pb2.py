# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: notify.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x0cnotify.proto\x12\x0cnotification\"R\n\x13NotifyRequestVitals\x12\x11\n\teventName\x18\x01 \x01(\t\x12(\n\x06params\x18\x02 \x01(\x0b\x32\x18.notification.Parameters\"X\n\x15NotifyRequestCalories\x12\x11\n\teventName\x18\x01 \x01(\t\x12,\n\x06params\x18\x02 \x01(\x0b\x32\x1c.notification.BurnedCalories\"L\n\x12NotifyRequestPulse\x12\x11\n\teventName\x18\x01 \x01(\t\x12#\n\x06params\x18\x02 \x01(\x0b\x32\x13.notification.Pulse\"2\n\x0e\x42urnedCalories\x12\x10\n\x08\x63\x61lories\x18\x01 \x01(\x02\x12\x0e\n\x06userID\x18\x02 \x01(\x05\"&\n\x05Pulse\x12\r\n\x05pulse\x18\x01 \x01(\x02\x12\x0e\n\x06userID\x18\x02 \x01(\x05\"Y\n\nParameters\x12\x0b\n\x03sys\x18\x01 \x01(\x05\x12\x0c\n\x04\x64ias\x18\x02 \x01(\x05\x12\r\n\x05pulse\x18\x03 \x01(\x05\x12\x0e\n\x06userID\x18\x04 \x01(\x05\x12\x11\n\ttimestamp\x18\x05 \x01(\t\"\x1e\n\x0bNotifyReply\x12\x0f\n\x07message\x18\x01 \x01(\t2\x89\x02\n\x0cNotification\x12Q\n\x11NotifyEventVitals\x12!.notification.NotifyRequestVitals\x1a\x19.notification.NotifyReply\x12U\n\x13NotifyEventCalories\x12#.notification.NotifyRequestCalories\x1a\x19.notification.NotifyReply\x12O\n\x10NotifyEventPulse\x12 .notification.NotifyRequestPulse\x1a\x19.notification.NotifyReplyb\x06proto3')



_NOTIFYREQUESTVITALS = DESCRIPTOR.message_types_by_name['NotifyRequestVitals']
_NOTIFYREQUESTCALORIES = DESCRIPTOR.message_types_by_name['NotifyRequestCalories']
_NOTIFYREQUESTPULSE = DESCRIPTOR.message_types_by_name['NotifyRequestPulse']
_BURNEDCALORIES = DESCRIPTOR.message_types_by_name['BurnedCalories']
_PULSE = DESCRIPTOR.message_types_by_name['Pulse']
_PARAMETERS = DESCRIPTOR.message_types_by_name['Parameters']
_NOTIFYREPLY = DESCRIPTOR.message_types_by_name['NotifyReply']
NotifyRequestVitals = _reflection.GeneratedProtocolMessageType('NotifyRequestVitals', (_message.Message,), {
  'DESCRIPTOR' : _NOTIFYREQUESTVITALS,
  '__module__' : 'notify_pb2'
  # @@protoc_insertion_point(class_scope:notification.NotifyRequestVitals)
  })
_sym_db.RegisterMessage(NotifyRequestVitals)

NotifyRequestCalories = _reflection.GeneratedProtocolMessageType('NotifyRequestCalories', (_message.Message,), {
  'DESCRIPTOR' : _NOTIFYREQUESTCALORIES,
  '__module__' : 'notify_pb2'
  # @@protoc_insertion_point(class_scope:notification.NotifyRequestCalories)
  })
_sym_db.RegisterMessage(NotifyRequestCalories)

NotifyRequestPulse = _reflection.GeneratedProtocolMessageType('NotifyRequestPulse', (_message.Message,), {
  'DESCRIPTOR' : _NOTIFYREQUESTPULSE,
  '__module__' : 'notify_pb2'
  # @@protoc_insertion_point(class_scope:notification.NotifyRequestPulse)
  })
_sym_db.RegisterMessage(NotifyRequestPulse)

BurnedCalories = _reflection.GeneratedProtocolMessageType('BurnedCalories', (_message.Message,), {
  'DESCRIPTOR' : _BURNEDCALORIES,
  '__module__' : 'notify_pb2'
  # @@protoc_insertion_point(class_scope:notification.BurnedCalories)
  })
_sym_db.RegisterMessage(BurnedCalories)

Pulse = _reflection.GeneratedProtocolMessageType('Pulse', (_message.Message,), {
  'DESCRIPTOR' : _PULSE,
  '__module__' : 'notify_pb2'
  # @@protoc_insertion_point(class_scope:notification.Pulse)
  })
_sym_db.RegisterMessage(Pulse)

Parameters = _reflection.GeneratedProtocolMessageType('Parameters', (_message.Message,), {
  'DESCRIPTOR' : _PARAMETERS,
  '__module__' : 'notify_pb2'
  # @@protoc_insertion_point(class_scope:notification.Parameters)
  })
_sym_db.RegisterMessage(Parameters)

NotifyReply = _reflection.GeneratedProtocolMessageType('NotifyReply', (_message.Message,), {
  'DESCRIPTOR' : _NOTIFYREPLY,
  '__module__' : 'notify_pb2'
  # @@protoc_insertion_point(class_scope:notification.NotifyReply)
  })
_sym_db.RegisterMessage(NotifyReply)

_NOTIFICATION = DESCRIPTOR.services_by_name['Notification']
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  _NOTIFYREQUESTVITALS._serialized_start=30
  _NOTIFYREQUESTVITALS._serialized_end=112
  _NOTIFYREQUESTCALORIES._serialized_start=114
  _NOTIFYREQUESTCALORIES._serialized_end=202
  _NOTIFYREQUESTPULSE._serialized_start=204
  _NOTIFYREQUESTPULSE._serialized_end=280
  _BURNEDCALORIES._serialized_start=282
  _BURNEDCALORIES._serialized_end=332
  _PULSE._serialized_start=334
  _PULSE._serialized_end=372
  _PARAMETERS._serialized_start=374
  _PARAMETERS._serialized_end=463
  _NOTIFYREPLY._serialized_start=465
  _NOTIFYREPLY._serialized_end=495
  _NOTIFICATION._serialized_start=498
  _NOTIFICATION._serialized_end=763
# @@protoc_insertion_point(module_scope)
