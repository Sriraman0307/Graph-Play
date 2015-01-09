JFLAGS = -g
JC = javac
JVM = java
src = src/
cls = ../bin/
filepath="D:\\Algo\\input.txt"
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) *.java -d $(cls)

CLASSES = \
		Edge.java\
		Graph.java\
		Main.java

MAIN = Main
		 
default: classes

classes: $(CLASSES:.java=.class)
		
run: $(MAIN).class 
	cd $(cls); $(JVM) $(MAIN) $(filepath)

clean:
	$(RM) $(cls)*.class