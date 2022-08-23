from os import abort
from flask import Flask, request, abort
import pandas as pd
import joblib

model = joblib.load('model/model.joblib')

app = Flask(__name__)

@app.route("/predict", methods=['POST'])
def predict():
    print(request.get_json(), flush=True)
    if request.json['Gender'] is None or request.json['Age'] is None or \
        request.json['Height'] is None or request.json['Weight'] is None or \
        request.json['Duration'] is None or request.json['Heart_Rate'] is None or request.json['Body_Temp'] is None:
        abort(400)
    else:
        calories = get_model_response(request.get_json())
        return str(calories)



def get_model_response(json_data):
    print(type(json_data), flush=True)
    X = pd.json_normalize(json_data)
    prediction = model.predict(X)
    return float(prediction)

if __name__ == '__main__':
    #port = int(os.environ.get('PORT', 5009))
    app.run(host='0.0.0.0', port=7080)