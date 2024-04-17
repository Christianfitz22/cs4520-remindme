Final Assignment: RemindMe App

Code for Northeastern University's CS4520, Spring 2024
Written by: Audrey Lin, Christian Fitzpatrick, Aminah Statham-Adams

GitHub Repo: https://github.com/Christianfitz22/cs4520-remindme

Additional Information:
The app starts on the home screen, which allows the user to choose one of two buttons. The Create Reminder button directs the player to create a reminder, whereas the View Reminder button directs them to the screen with all previously created reminders.

The create reminder screen will allow the user to fill in three fields: Name, Category, and Description. Once all three fields are filled out, the app will create a reminder and send it to the database and API. If any of the three fields are not filled out, then the app will show a toast message and not create the reminder.

The view reminder screen shows the list of previously created reminders. The player may scroll through the list and tap on any existing reminder, which brings them to the specific reminder screen. When viewing that reminder, the user can see the previously created reminder's name, description, and category, as well as being able to delete the reminder if it is completed.

The App will also call an API to see if it is a national holiday and work is off, and tell the user so on the View Reminder screen. This will allow the user to better determine how urgent tasks are on their list, as well as if they have the time to do so.