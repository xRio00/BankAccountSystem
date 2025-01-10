Repository Name
BankAccountSystem

Description
Bank Account System is a Java-based console application that provides core banking functionalities. This demo application allows users to:

Register new accounts with username, email, and phone.
Log in using their username, email, or phone.
Transfer money between accounts.
Reset their password via an OTP-based system (demo OTP is displayed on the console).
Update account details (password, email, phone).
Delete their account.
Features
Registration:
Confirms password during registration.
Generates a unique 13-digit account number.
Login:
Supports login via username, email, or phone.
Validates password during login.
Reset Password:
Generates an OTP for password reset (demo OTP displayed on the console).
Validates OTP for security.
Account Management:
View account details.
Update password, email, or phone number.
Transfer money to another account.
Delete account.
Requirements
Java: Version 8 or later.
JDK Installed: Ensure the javac and java commands are available in your terminal.
How to Run
Clone the repository:
bash
Copy code
git clone https://github.com/xRio00/BankAccountSystem.git
cd BankAccountSystem
Compile the Java file:
bash
Copy code
javac BankAccountSystem.java
Run the program:
bash
Copy code
java BankAccountSystem
Demo Features
The system uses a demo OTP for password reset, which is displayed in the console for testing purposes.
Data is stored locally in the accounts.txt file.
