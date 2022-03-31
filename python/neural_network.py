import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt
import json
import sys
from io import StringIO
from tensorflow.keras import layers
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report

import sys

def prediction(state_list):
    modelName = 'models/hanabi_52.h5'
    model = tf.keras.models.load_model(modelName)

    my_data = [[float(val) for val in current_state.split(',')] for current_state in state_list]
    data = np.array([np.array(xi) for xi in my_data])

    #print(data)
    #print(data.shape)

    y_pred = model.predict(data)
    action_index = np.argmax(y_pred, axis=1)
    return action_index
    #print "hello and that's your sum:", a + b

def prova(a):
    print(len(a))
    print(len(a.split(",")))

def train():
    ### LOAD MODEL ###
    modelName = 'models/hanabi_51.h5'
    model = tf.keras.models.load_model(modelName)

    ### OPEN FILES ####
    # This opens a handle to your file, in 'r' read mode
    file_handle = open('final_states_new_/final_states_new_4.txt', 'r')

    # Read in all the lines of your file into a list of lines
    lines_list = file_handle.readlines()
    # Do a double-nested list comprehension to get the rest of the data into your matrix
    my_data = [[float(val) for val in line.split(',')] for line in lines_list]

    data = np.array([np.array(xi) for xi in my_data])

    file_handle = open('final_actions_new_/final_actions_new_4.txt', 'r')
    lines_list = file_handle.readlines()
    my_data = [[float(val) for val in line.split(',')] for line in lines_list]

    actions = np.array([np.array(xi) for xi in my_data])

    # Split data into: 70% training, 20% validation, 10% testing
    X_2, test_data = train_test_split(data, test_size=0.1, random_state=10)
    train_data, val_data = train_test_split(X_2, test_size=len(data) * 0.2 / len(X_2), random_state=10)

    X_2, test_actions = train_test_split(actions, test_size=0.1, random_state=10)
    train_actions, val_actions = train_test_split(X_2, test_size=len(actions) * 0.2 / len(X_2), random_state=10)

    ### CONVERT TO TENSORS ###
    data_tensor = tf.convert_to_tensor(train_data)
    action_tensor = tf.convert_to_tensor(train_actions)
    test_data_tensor = tf.convert_to_tensor(test_data)
    test_actions_tensor = tf.convert_to_tensor(test_actions)

    ### KEEP TRAINING ###
    model.compile(loss=tf.keras.losses.categorical_crossentropy, optimizer=tf.keras.optimizers.Adam(), metrics="accuracy")
    hystory = model.fit(x=data_tensor, y=action_tensor, epochs=10, validation_data=(val_data, val_actions))

    ### SAVE MODEL ###
    score = model.evaluate(test_data_tensor, test_actions_tensor)
    print(score)
    format_acc = "{:.0f}".format(score[1] * 100)

    y_pred = model.predict(test_data_tensor)
    # np.c_[action_tensor,y_pred]
    y_true = np.argmax(test_actions_tensor, axis=1)
    y_pred = np.argmax(y_pred, axis=1)

    print(y_true)
    print(y_pred)

    print(classification_report(y_true, y_pred))

    model.save(f'models/hanabi_{format_acc}.h5')

if __name__ == "__main__":

    current_state = [sys.argv[1]]
    action_index = prediction(current_state)
    print(action_index)

    # train()
    ### prova(current_state[0])
    #prediction(current_state)
    #b = int(sys.argv[2])
    #hello(a, b)




