import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.model_selection import train_test_split
from xgboost import XGBRegressor
from sklearn import metrics
import joblib
from sklearn.metrics import mean_squared_error

# loading the data from .csv
calories = pd.read_csv('../data/calories.csv')

exercise_data = pd.read_csv('../data/exercise.csv')
calories_data = pd.concat([exercise_data, calories['Calories']], axis = 1)

#converting gender text values to numerical values
calories_data.replace({'Gender': {'male': 0, 'female': 1}}, inplace=True)
#print(calories_data.head())

#separating features and target
X = calories_data.drop(columns=['User_ID', 'Calories'], axis=1) # axis = 1 -> column
Y = calories_data['Calories']

# print(X)
# print(Y)

# Splitting the data into training data and Test data
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.2, random_state=2)
#print(X.shape, X_train.shape, X_test.shape)

# XGBoost Regressor
# load the model
model = XGBRegressor()
# training the model with X_train
model.fit(X_train, Y_train)

test_data_prediction = model.predict(X_test)

#print(Y_test)
#print(test_data_prediction)

mae = metrics.mean_absolute_error(Y_test, test_data_prediction)
rmse = np.sqrt(mean_squared_error(Y_test, test_data_prediction))
print("Mean Absolute error", mae)

print("RMSE:", rmse)

joblib.dump(model, './model.joblib')

