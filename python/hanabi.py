import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt
import json
from io import StringIO
from tensorflow.keras import layers
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report
from sklearn.metrics import confusion_matrix

def loadDataset(data_type):

    for i in range(1,5):
        print(f"Reading {data_type}_{i}...", end=" ")
        file_handle = open(f'final_{data_type}/final_{data_type}_unordered_{i}.txt', 'r')
        lines_list = file_handle.readlines()
        my_data = [[float(val) for val in line.split(',')] for line in lines_list]
        temp = np.array([np.array(xi) for xi in my_data])
        if i==1:
            data = temp
        else:
            data = np.concatenate([data, temp])
        print("ok", end = " ")
        print(data.shape)
    return data

def neural_network():
    input = layers.Input((195), dtype=tf.float32)

    # def resnet_block(x):

    # First component of main path
    x1 = layers.Dense(195, activation="relu", name="x1")(input)
    x = layers.Dropout(0.1)(x1)

    # Second component of main path
    x = layers.Dense(512, activation="relu", name="x2")(x)
    x = layers.Dropout(0.1)(x)
    x = layers.Dense(128, activation="relu", name="x3")(x)
    x = layers.Dropout(0.1)(x)
    x = layers.Dense(32, activation="relu", name="x4")(x)
    #x = layers.BatchNormalization()(x)
    #x = layers.Dropout(0.1)(x)

    # Second component of main path
    #x4 = layers.Dense(195, activation="relu", name="x5")(x)

    # Final step: Add shortcut value to main path, and pass it through a softmax activation
    #x = layers.Add(name="Add")([x1, x4])

    #x = layers.Dense(128, activation="relu", name="x6")(x)
    #x = layers.Dropout(0.1)(x)
    #x = layers.Dense(64, activation="relu", name="x7")(x)
    #x = layers.Dropout(0.1)(x)
    #x = layers.Dense(32, activation="relu", name="x8")(x)

    x = layers.BatchNormalization()(x)
    output = layers.Dense(20, activation='softmax', name="output")(x)
    model = tf.keras.Model(input, output)
    return model

def makeplot(fit):
    # Plot stats.
    hist = fit.history
    print(hist.keys())
    loss_values = hist["loss"]
    val_loss_values = hist["val_loss"]
    acc = hist["accuracy"]
    val_acc = hist["val_accuracy"]

    epochs = range(1, len(loss_values) + 1)

    plt.plot(epochs, loss_values, "b", label="Training loss")
    plt.plot(epochs, val_loss_values, "g", label="Validation loss")
    plt.plot(epochs, acc, "r", label="Accuracy")
    plt.plot(epochs, val_acc, "purple", label="Validation accuracy")
    plt.grid(linewidth=0.25)
    plt.yticks(np.arange(0, 1.1, 0.1))
    plt.title("Training and validation loss/accuracy")
    plt.xlabel("Epochs")
    plt.ylabel("Loss/accuracy")
    plt.legend()

    filename = f'images/graph_large.png'
    plt.savefig(filename)

    plt.show()

if __name__ == "__main__":
    # load the dataset from the states and the corresponding actions
    states = loadDataset("states")
    actions = loadDataset("actions")

    # Split data into: 70% training, 20% validation, 10% testing
    X_2, test_states = train_test_split(states, test_size=0.1, random_state=10)
    train_states, val_states = train_test_split(X_2, test_size=len(states) * 0.2 / len(X_2), random_state=10)

    X_2, test_actions = train_test_split(actions, test_size=0.1, random_state=10)
    train_actions, val_actions = train_test_split(X_2, test_size=len(actions) * 0.2 / len(X_2), random_state=10)

    # convert to tensors
    train_states_tensor = tf.convert_to_tensor(train_states)
    train_actions_tensor = tf.convert_to_tensor(train_actions)
    val_states_tensor = tf.convert_to_tensor(val_states)
    val_actions_tensor = tf.convert_to_tensor(val_actions)
    test_states_tensor = tf.convert_to_tensor(test_states)
    test_actions_tensor = tf.convert_to_tensor(test_actions)

    # build the network architecture
    model = neural_network()
    model.summary()

    model.compile(loss=tf.keras.losses.categorical_crossentropy, optimizer=tf.keras.optimizers.Adam(),
                  metrics="accuracy")
    fit = model.fit(x=train_states_tensor, y=train_actions_tensor, epochs=20, validation_data=(val_states_tensor, val_actions_tensor))

    score = model.evaluate(test_states_tensor, test_actions_tensor)
    format_acc = "{:.0f}".format(score[1] * 100)

    y_pred = model.predict(test_states_tensor)
    # np.c_[action_tensor,y_pred]
    y_true = np.argmax(test_actions_tensor, axis=1)
    y_pred = np.argmax(y_pred, axis=1)

    print(classification_report(y_true, y_pred))

    model.save(f'models/hanabi_{format_acc}_large.h5')

    makeplot(fit)
