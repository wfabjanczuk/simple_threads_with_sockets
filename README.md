To test the project locally, you can simply compile the **LocalRunner** classes (replace `<project root>` with the path
to the project root directory):

`javac -encoding utf8 -classpath <project root>/src
<project root>/src/BackendLocalRunner.java
<project root>/src/GuiLocalRunner.java`

Firstly, run the backend locally with no additional arguments:

`java -classpath <project root>/src zad1.BackendLocalRunner`

The **Proxy** DICT server runs by default on port 2628 and **Translator** servers occupy port numbers 1600-1604.

Secondly, run the **Gui** instance with client port number, for example:

`java -classpath <project root>/src zad1.GuiLocalRunner 1500`

You can run more than one **Gui** in the same time, using different ports.