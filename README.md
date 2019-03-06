# Postfix-Regex-to-NFA


------------
Build Instructions
------------
To compile and run, place the two source files in the same
directory as the file of regexs you want to evaluate, and
type the following commands into the terminal:

To compile:
			javac Main.java State.java

To run:
			java Main <fileName.txt>


Tested on Ubuntu 16.04 LTS

--------------
Included Files
--------------

	State.java - Contains the definition and constructor for
				 the State class
	Main.java  - Contains the main function, input reading, 
				 and all functions for NFA generation and
			     printing.
--------------
Implementation
Overview
-------------

This program takes a plain text file of regular expressions
in postfix notation and outputs a corresponding NFA as a 
list of transitions with start and final states indicated.

The implementation works by treating an NFA as a "linked list"
of NFA state objects, and passing around the start states of
these NFAs for manipulation.

I left (Probably overly) detailed comments in the source files
explaining what's going on.

