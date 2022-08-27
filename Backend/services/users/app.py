from crypt import methods
from curses.ascii import FF
import string
from urllib.robotparser import RequestRate
from flask import request, abort, Response
from flask import Flask
import random as rand
from flask import jsonify
from bson.objectid import ObjectId
from bson.json_util import dumps
import os
from pymongo import ReturnDocument
from pymongo import MongoClient

MONGO_URI = 'mongodb://' + os.environ['MONGODB_USERNAME'] + ':' + os.environ['MONGODB_PASSWORD'] + '@' + os.environ['MONGODB_HOSTNAME'] + ':27017/' + os.environ['MONGODB_DATABASE'] + '?authSource=admin'
print(MONGO_URI)
mongo_client = MongoClient(MONGO_URI, username = os.environ['MONGODB_USERNAME'], password=os.environ['MONGODB_PASSWORD'])

mongo_db = mongo_client['usersdb']
users = mongo_db.users
fcm = mongo_db.fcm

avatars = ["1nBY3lcEQGKQGXb5SHFjqE9Sj56-kPNiw", "1muX0Mdx5-3qMK41KuCAaJhbveBTxnRAQ", "1ENU_a_tlrgmr0nXSqIqsGhIbuhpNz-In", "1IhZcOe8N1oEv5A6TJAh2pjnIng30pO07", "1aEiSPxkXrqd5C9Tk0L8EyievQY_8dGj-",
"1z7u9ghHvsPNZnSX2uQD2f3BI05zvsQ4g", "1MNFYpLcm2DcVgfYuSRlubdwEFLHJDNQk", "1MJw0FS_n7kZDLuFUTOy4RJ2R4BzZvkqj"]

app = Flask(__name__)

@app.route("/")
def hello():
    return "Hello"

@app.route("/user", methods=["POST"])
def add_user():
    print(request.json, flush=True)
    if request.json['username'] is None or request.json['age'] is None or \
        request.json['_id'] is None or request.json['gender'] is None or request.json['height'] is None or request.json['weight'] is None:
        abort(400)
    else:
        request.json['_id'] = rand.randint(1000, 9999)
        request.json['imgSrc'] = "https://drive.google.com/uc?id=" + avatars[rand.randint(0, 7)]
        user_id = users.insert_one(request.json).inserted_id
        return str(user_id)

@app.route("/user/getFriends/<int:user_id>", methods=["GET"])
def get_friends(user_id: int):
    pipeline = [
        {
            "$facet": {
              "searched_user": [
                {
                  '$match': {
                    '_id': user_id
                  }
                }
              ],
              "other_users": [
                {
                  '$match': {
                    '_id': {
                      '$ne': user_id
                    }
                  }
                }
              ]
            }
          },
          {
            "$unwind": "$searched_user"
          },
          {
            '$project': {
              'user_friends': {
                '$filter': {
                  'input': "$other_users",
                  'as': "other_users",
                  'cond': {
                    '$in': [
                      "$$other_users._id",
                      "$searched_user.friends"
                    ]
                  }
                }
              }
            }
        }
    ]
    result = users.aggregate(pipeline)
    #print(result, flush=True)
    resultList = list(result)
    if(len(resultList) > 0):
        print(resultList[0], flush=True)
        return resultList[0]['user_friends']
    else:
        return []

@app.route("/user/<int:user_id>", methods=["GET"])
def get_user(user_id: int):
    print("get")
    user = users.find_one({'_id': user_id})
    print(user, flush=True)
    return dumps(user)

@app.route("/user/addPoints", methods=["PUT"])
def update_rank():
  if request.json['points'] is None or request.json['_id'] is None:
    abort(400)
  else:
    user = users.find_one_and_update({'_id': request.json['_id']}, {'$inc': {'rank': request.json['points']}}, return_document=ReturnDocument.AFTER)
    return str(user['rank'])

    # if None == user:
    #     return Response("User not found", status=404, mimetype="application/json")
    # return Response(json.dumps(user), status=200, mimetype="application/json")

@app.route("/user/addFriend/<int:user_id>/<int:friend_id>", methods=["PUT"])
def add_friend(user_id: int, friend_id: int):
    print("put")
    user = users.find_one_and_update({'_id': user_id}, {'$push': {'friends': friend_id}}, return_document=ReturnDocument.AFTER)
    friend = users.find_one_and_update({'_id': friend_id}, {'$push': {'friends': user_id}}, return_document=ReturnDocument.AFTER)
    print(user, flush=True)
    return dumps(user)

@app.route("/user/addFcm", methods = ["POST"])
def add_fcm():
  print(request.get_json(), flush=True)
  if request.json['fcm'] is None:
        abort(400)
  else:
    res = fcm.insert_one(request.json).inserted_id
    return dumps(res)
    

@app.route("/user/updateFcm", methods = ["PUT"])
def update_fcm():
  if request.json['fcm'] is None:
        abort(400)
  else:
    fcm.update_one({
      '_id': request.json['_id']
    },{
      '$set': {
        'fcm': request.json['fcm']
      }
    }, upsert = False)
    return dumps(request.json['_id'])

@app.route("/user/getFcm/<int:user_id>", methods=["GET"])
def get_fcm(user_id:int):
  res = fcm.find_one({'_id': user_id})
  return dumps(res)



if __name__ == '__main__':
    #port = int(os.environ.get('PORT', 5009))
    app.run(host='0.0.0.0', port=6080)
