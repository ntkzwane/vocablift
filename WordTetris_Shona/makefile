#slbsun001

JAVAC = javac
JFLAGS = -g
JARFILE = blah.jar
CLASSFILES = Lex.class ParseTreePrinter.class Parse.class 

#  build rules
WordApp.class: Score.class WordApp.java
	$(JAVAC)  $(JFLAGS)  WordApp.java

Score.class:  WordDictionary.class Score.java 
	$(JAVAC)  $(JFLAGS)  Score.java

WordDictionary.class:  WordPanel.class WordDictionary.java
	$(JAVAC)  $(JFLAGS)  WordDictionary.java

WordPanel.class:  WordRecord.class WordPanel.java
	$(JAVAC)  $(JFLAGS)  WordPanel.java

WordRecord.class:  MyListener.class WordRecord.java
	$(JAVAC)  $(JFLAGS)  WordRecord.java

MyListener.class:  MyListener.java
	$(JAVAC)  $(JFLAGS)  MyListener.java

clean:
	@rm -f  $(CLASSFILES) $(JARFILE)

jar:
	jar cvf $(JARFILE) $(CLASSFILES)
