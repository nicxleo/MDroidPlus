# MDroidPlus
MDroidPlus is a mutation analysis framework for Android applications.
MDroidPlus implements 35 mutation operators specifically for Android apps, covering the following categories:
- Activity/Intents
- Android Programming
- Back-End Services
- Connectivity
- Data
- Database
- General Programming
- GUI
- I/O
- Non-Functional Requirements

The complete list of mutation operators and their specification is available at the [MDroidPlus website](http://android-mutation.com/#operators).
Given an Android App, MDroidPlus first extracts the Potential Fault Profile (PFP) and then automatically seeds mutants generating mutated copies of the App.

MDroidPlus is a collaborative research effort between the SEMERU group at William & Mary, the University of Lugano, and the University of Sannio.

For more information please visit: http://android-mutation.com

### Cite
If you use MDroidPlus for academic purposes, please cite: Linares-Vásquez, M., Bavota, G., Tufano, M., Moran, K., Di Penta, M., Vendome, C., Bernal-Cárdenas, C., and Poshyvanyk, D., _“Enabling Mutation Testing for Android Apps”_, in Proceedings of 11th Joint Meeting of the European Software Engineering Conference and the 25th ACM SIGSOFT International Symposium on the Foundations of Software Engineering (ESEC/FSE’17), Paderborn, Germany, September 4-8, 2017, to appear 12 pages (24.4% acceptance ratio)

# Compile
Download and compile MDroidPlus with the following commands:
```
git clone https://gitlab.com/SEMERU-Code-Public/Android/Mutation/MDroidPlus.git
cd MDroidPlus
mvn clean
mvn package
```
The generated runnable jar can be found in: ``MDroidPlus/target/MDroidPlus-1.0.0.jar``

# Usage
To run MDroidPlus use the following command, specifying the required arguments:
```
java -jar MDroidPlus-1.0.0.jar <libs4ast> <AppSourceCode> <AppName> <Output>
```
### Arguments
Provide the following list of required arguments when running MDroidPlus: 
1. ``libs4ast``:  path of the lib4ast folder (``MDroidPlus/lib4ast/``);
2. ``AppSourceCode``: path of the Android app source code folder;
3. ``AppName``: name of the Android app;
4. ``Output``: path of the folder where the mutantns will be created;

### Example
```
cd MDroidPlus
java -jar target/MDroidPlus-1.0.0.jar libs4ast/ /tmp/AppFoo/ AppFoo /tmp/mutants/
```

### Output
The output directory will contain a folder for each generated mutant and a log file. 
The mutants folders are named with the corresponding mutant ID (i.e., numerical ID). The log file contains information about the mutation process as well as the type and location of each mutant generated.


# Future Work
We plan to release a new version of MDroidPlus with improved APIs in October.