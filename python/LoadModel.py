import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt
import json
from io import StringIO
from tensorflow.keras import layers
from sklearn.model_selection import train_test_split
#modelName = 'models/model_16-32-64-128-256_25e_81acc_33loss_21min.h5'
modelName = 'models/hanabi_30.h5'
model = tf.keras.models.load_model(modelName)

model.summary()

file_handle = open('final_states/final_states_3.txt', 'r')
# Read in all the lines of your file into a list of lines
lines_list = file_handle.readlines()
# Do a double-nested list comprehension to get the rest of the data into your matrix
my_data = [[float(val) for val in line.split(',')] for line in lines_list]
data = np.array([np.array(xi) for xi in my_data])

file_handle = open('final_actions/final_actions_3.txt', 'r')
lines_list = file_handle.readlines()
my_data = [[float(val) for val in line.split(',')] for line in lines_list]
actions = np.array([np.array(xi) for xi in my_data])

score = model.evaluate(data, actions)
print(score)