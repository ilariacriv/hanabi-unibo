import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt
import json
from io import StringIO
from tensorflow.keras import layers
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report
from sklearn.metrics import confusion_matrix

#data=json.load(open("prova.txt"))

# This opens a handle to your file, in 'r' read mode
file_handle = open('final_states/final_states_2.txt', 'r')

# Read in all the lines of your file into a list of lines
lines_list = file_handle.readlines()
# Do a double-nested list comprehension to get the rest of the data into your matrix
my_data = [[float(val) for val in line.split(',')] for line in lines_list]

data = np.array([np.array(xi) for xi in my_data])

file_handle = open('final_actions/final_actions_2.txt', 'r')
lines_list = file_handle.readlines()
my_data = [[float(val) for val in line.split(',')] for line in lines_list]

actions = np.array([np.array(xi) for xi in my_data])

# Split data into: 70% training, 20% validation, 10% testing
X_2, test_data = train_test_split(data, test_size=0.1, random_state=10)
train_data, val_data = train_test_split(X_2, test_size=len(data) * 0.2 / len(X_2), random_state=10)

X_2, test_actions = train_test_split(actions, test_size=0.1, random_state=10)
train_actions, val_actions = train_test_split(X_2, test_size=len(actions) * 0.2 / len(X_2), random_state=10)


data_tensor=tf.convert_to_tensor(train_data)
action_tensor=tf.convert_to_tensor(train_actions)

#actions= np.genfromtxt(StringIO(actions), delimiter=',')
#data= np.genfromtxt(StringIO(data), delimiter=',')

input = layers.Input((176), dtype=tf.float32)

#def resnet_block(x):

# First component of main path
x1 = layers.Dense(176, activation="relu", name="x1")(input)
x = layers.Dropout(0.1)(x1)

# Second component of main path
x = layers.Dense(64, activation="relu", name="x2")(x)
x = layers.BatchNormalization()(x)

# Second component of main path
x4 = layers.Dense(176, activation="relu", name="x3")(x)

# Final step: Add shortcut value to main path, and pass it through a softmax activation
x = layers.Add(name="Add")([x1, x4])

#X = tf.keras.layers.Attention()(X)
#x = layers.BatchNormalization()(x)
output = layers.Dense(20, activation='softmax', name="output")(x)
model = tf.keras.Model(input, output)

#    return x6

#model=resnet_block(inputs)
model.summary()

print(tf.shape(data_tensor))
print(tf.shape(action_tensor))

model.compile(loss=tf.keras.losses.categorical_crossentropy,optimizer=tf.keras.optimizers.Adam(), metrics="accuracy")
hystory= model.fit(x=data_tensor,y=action_tensor,epochs=10, validation_data=(val_data, val_actions))


score = model.evaluate(test_data, test_actions)
print(score)
format_acc = "{:.0f}".format(score[1]*100)

y_pred=model.predict(test_data)
#np.c_[action_tensor,y_pred]
y_true = np.argmax(test_actions, axis = 1)
y_pred = np.argmax(y_pred, axis = 1)

print(y_true)
print(y_pred)

print(classification_report(y_true, y_pred))

model.save(f'models/hanabi_{format_acc}.h5')
