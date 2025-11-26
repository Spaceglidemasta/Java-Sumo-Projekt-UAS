# Java Project WiSe 25 / 26, Group 3
A multithreaded*, GUI based, Java TraaS API wrapper,
which starts up a Sumo simulation and connects to it,
adding and removing objects from the Simulation in real
time.

_*To be implemented_

By Luca De Simone, Joel Stark, Leon Chimentchik and David Cornelius.

## Features

_list future features here_

## Dependencies

- [`Sumo`](https://sumo.dlr.de/docs/Installing/index.html, "SUMO Documentation") - needed for TraaS
- [`Maven`](https://maven.apache.org, "Apache Maven") - used for building the project
- [`Traas.jar`](https://sumo.dlr.de/javadoc/traas/index.html, "TraaS Documentation") -
the core component of the program, used to start, edit and end the SUMO connection.

This automatically gets installed when you install SUMO on you device, or when you clone this repo. 

## Installation

Make sure to have SUMO installed and the environment variable SUMO_HOME set correctly. The TraaS API needs this.

Move the /SumoConfig directory to the parent folder and rename it to "resources":

```
├> JavaProjectWiSe2526: 16.42 MB // <-- Project
├──────┼> .git: 6.30 MB
│      │       ...
│      ├> .gitignore: 490 B
├──────┼> .idea: 6.84 KB
│      │       ...
│      ├> .mvn: 0 B
├──────┼> libs: 122.76 KB
│      │       ...
│      ├> pom.xml: 4.57 KB
│      ├> PROJECTNOTES.md: 569 B
│      ├> README.md: 108 B
├──────┼> src: 48.21 KB
│      │       ...
├──────┼> target: 9.94 MB
│      │       ...
├> JavaProjectWiSe2526-1.0-SNAPSHOT-jar-with-dependencies.jar: 9.83 MB // <-- .jar, if you use one
├> resources: 6.61 MB
│      ├> net.net.xml: 1.71 KB
│      ├> net.rou.xml: 449 B
│      ├> sumo.exe: 6.61 MB     <-- IMPORTANT: Only needed if there is no sumo.exe in %SUMO_HOME%\bin
```

Start / compile using

    mvn javafx:run

or

    mvn clean install -U
    mvn exec:java

or, if you want a .jar file:

    mvn clean package

This creates 2 .jar's in /src/target, make sure move the one named *-with-dependencies.jar to the parent folder,
like showed in the file tree above.

Then, double-click the .jar or, inside the parent folder, 

    java -jar JavaProjectWi[...]-dependencies.jar

Important: if there is no sumo.exe / sumo in your "%sumo_home%\bin" / "$SUMO_HOME/bin", there needs to be one
in the resource folder, although this is redundant, as TraaS needs SUMO_HOME anyway.

## Usage

_add GUI examples with pictures here_