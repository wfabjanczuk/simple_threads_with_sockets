## Local configuration

### 1. Compilation

Java version: *1.8.0_281*

To test the project locally, you can simply compile the **LocalRunner** classes with the following command
(replace `<project_root>` with the path to the project root directory):

`javac -encoding utf8 -classpath <project_root>/src
<project_root>/src/zad1/BackendLocalRunner.java
<project_root>/src/zad1/GuiLocalRunner.java`

### 2. Running the backend

Make sure the **LocalRunner** classes are already compiled. To run the backend locally use command:

`java -classpath <project_root>/src zad1.BackendLocalRunner <proxy_port> <first_translator_port>`

For example arguments `<proxy_port> = 2628` and `<first_translator_port> = 1600` will make the **Proxy** DICT server run
on port *2628* and **Translator** servers occupy port numbers *1600-1604*.

### 3. Running the graphical user interface

Make sure the backend is up and running. To run the **Gui** instance use command:

`java -classpath <project_root>/src zad1.GuiLocalRunner <client_port> <proxy_port>`

For example arguments  `<client_port> = 1500` and `<proxy_port> = 2628` will make the **Client** run on port *1500* and
try to connect to **Proxy** on port *2628*.

You can run more than one **Gui** in the same time, using different `<client_port>` and the same `<proxy_port>`.