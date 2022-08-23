from crypt import methods
from curses.ascii import FF
import string
from flask import request, abort, Response
from flask import Flask
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


if __name__ == '__main__':
    #port = int(os.environ.get('PORT', 5009))
    app.run(host='0.0.0.0', port=6080)
