# Quiz App Set up instructions

This project is a simple Java Servlet-based web application with a front-end built using vanilla JavaScript. Maven is used to manage dependencies and build the project.

## Prerequisites

Before you can run this project, ensure that you have the following installed on your machine:

1. Java Development Kit (JDK) 
   - Version 8 or above

2. Apache Maven 
   - Version 3.6 or above

3. Apache Tomcat 
   - Version 9 or above

## Build and run the project

### Step 1: Clone the Repository
First, clone the repository to your local machine:
```
git clone <repository-url>
cd my-servlet-project
```

### Step 2: Build the Project with Maven
Use Maven to build the project and generate a .war (Web Application Archive) file:
```
mvn clean package
```

### Step 3: Deploy the WAR File to Tomcat
1. Copy the target/my-servlet-project.war file into the webapps/ directory of your local Tomcat installation.

2. Start Tomcat by running the following command from the Tomcat bin/ directory:
   - On Windows:
   ```
   startup.bat
   ```
   - On Linux/MacOS:
   ```
   ./startup.sh
   ```
   
### Step 4: Access the Application
Once Tomcat is running, you can access the application by opening your browser and navigating to:
```
http://localhost:8080/my-servlet-project/
```
This will load the HTML front-end (served as static files) that interacts with the back-end Java Servlet.








