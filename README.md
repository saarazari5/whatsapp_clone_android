# whatsapp Clone Android Application
This is a clone of the popular instant messaging app, WhatsApp. It is a chat application that allows users to send and receive text messages.

## Features
Registration and login with email and username.
Chatting with contacts.
Adding new contacts.
Sending and receiving text messages.
Search for contacts by name.
ChatApp supports 50KB maximum size of profile pictures.

## Technologies Used
For the server: Nodejs, MongoDB, Mongoose
For the client application: Java, Firebase, Room

## Requirements
A mobile phone with the application installed in it. Or open the project with Android Studio, and run the emulator and the application will pop out.
This project relies on **mongodb**, and **mongoose**, and many other android dependencies available through **Android Studio** to run properly. Make sure you have those installed before continuing with the installation process.

## Limitations 
1) Provide a full path URL with PORT and protocol (for example: http://localhost:5000/api) otherwise the App will crush.
2) live messages might not work correctly at some times if there is no network.
3) each time you open the app, it will create a new jwt token for the current user, this might cause issues if the user is connected for both web client and mobile client.

## App Flow
<img width="400" alt="image" src="https://github.com/saarazari5/whatsapp_clone_android/assets/51089069/9e188fc1-1fd7-4f17-a888-d50a476b06bc">

our App uses only framgents and navgraph for navigation.
it is a single Activity application. 
App will always start on login page and if it can it will navigate to the current user chat screen.


## Settings
settings is a modal bottom sheet and not a new Activity

<img width="339" alt="Screenshot 2023-07-01 at 16 36 19" src="https://github.com/saarazari5/whatsapp_clone_android/assets/51089069/aefb4d0c-35b0-4a68-aa7a-b373b45f97f1">
it support 

1) set the server URL (provide a *full* URL in order for it to work)
2) dark mode by switching the toggle for example :

<img width="386" alt="Screenshot 2023-07-01 at 16 39 44" src="https://github.com/saarazari5/whatsapp_clone_android/assets/51089069/88dff078-5c5e-4cdb-beb4-03edec2e2c5a">

3) logging out from the user


## Installation and Setup
Clone the repository git clone [https://github.com/idoziv15/Advance_programming_2_ex1.git](https://github.com/saarazari5/whatsapp_clone_android.git)
Now, there is a Server folder in the project. Simply open the server folder in the terminal/cmd and run the command: npm install and then npm start - to run the application. 
Now the server is running on port 5000 in your local machine, and you are able to open the emulator with Android Studio, by clicking the run button and the application will start.
Also, you can connect an Android device to your computer and Android Studio will recognize the connected the device, and will run the application there. Make sure to set USB debuging
to active on your android device. 
That`s it!

```
# Usage

* In terminal:
$ cd {path}/Server
$ npm install
$ npm start
```

Replace {path} with the folder path to the project's repository on your personal computer.
Register and log in to the app.
Start chatting with your contacts!
