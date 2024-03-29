from base64 import decode
import json
from flask import Flask, jsonify
import os
import paho.mqtt.client as mqtt
from pymongo import MongoClient
from grpc_client import grpcClient

client_grpc = grpcClient()
os.environ['GRPC_TRACE'] = 'all' 
os.environ['GRPC_VERBOSITY'] = 'DEBUG'

MONGO_URI = 'mongodb://' + os.environ['MONGODB_USERNAME'] + ':' + os.environ['MONGODB_PASSWORD'] + '@' + os.environ['MONGODB_HOSTNAME'] + ':27017/' + os.environ['MONGODB_DATABASE'] + '?authSource=admin'
print(MONGO_URI)
mongo_client = MongoClient(MONGO_URI, username = os.environ['MONGODB_USERNAME'], password=os.environ['MONGODB_PASSWORD'])
mongo_db = mongo_client['analyticsdb']
parameters = mongo_db.parameters


# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc), flush = True)

    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.subscribe("analysis/high-pulse")
    client.subscribe("analysis/high-pressure")
    client.subscribe("analysis/low-pulse")
    client.subscribe("analysis/low-pressure")
    client.subscribe("analysis/calories")
    client.subscribe("analysis/avg-pulse")
    client.subscribe("analysis/max-pulse")
    print("sub", flush=True)

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    print(msg.topic+" "+str(msg.payload), flush = True)
    decoded_message = str(msg.payload.decode("utf-8"))
    jsonMsg = json.loads(decoded_message)
    param = jsonMsg[0]
    print(jsonMsg, flush=True)

    entry = {
        'event': '',
    }

    if(msg.topic == 'analysis/calories'):
        client_grpc.get_url_calories('Burned calories', param)
        entry = {
            'event': 'Burned calories',
            'calories': param['calories'],
            'userID': param['userID']
        }
    elif(msg.topic == 'analysis/avg-pulse'):
        client_grpc.get_url_pulse('Average pulse', param)
        entry = {
            'event': 'Average pulse',
            'avg': param['pulse'],
            'userID': param['userID']
        }
    elif(msg.topic == 'analysis/max-pulse'):
        client_grpc.get_url_pulse('Max pulse', param)
        entry = {
            'event': 'Max pulse',
            'avg': param['pulse'],
            'userID': param['userID']
        }
    else:
        entry = {
        'event': '',
        'sys': param['sys'],
        'dias': param['dias'],
        'pulse': param['pulse'],
        'userID': param['userID']
        }
        if(msg.topic == 'analysis/high-pulse'):
            entry['event'] = 'High pulse'
            client_grpc.get_url('High pulse', param)
        elif(msg.topic == 'analysis/high-pressure'):
            entry['event'] = 'High pressure'
            client_grpc.get_url('High pressure', param)
        elif(msg.topic == 'analysis/low-pressure'):
            entry['event'] = 'Low pressure'
            client_grpc.get_url('Low pressure', param)   
        else:
            entry['event'] = 'Low pulse'
            client_grpc.get_url('Low pulse', param)

    result = parameters.insert_one(entry)
    print(str(result), flush=True)



def on_disconnect(client, userdata, rc):
    if rc != 0:
        print("Unexpected disconnection.", flush = True)




# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.


if __name__ == "__main__":
    print("main", flush=True)
    #port = int(os.environ.get('PORT', 5005))
    #app.run(debug=True, host='0.0.0.0', port=port)

    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message
    client.on_disconnect = on_disconnect

    print("Trying to connect", flush = True)
    client.connect("mqtt", 1883, 60)
    client.loop_forever()