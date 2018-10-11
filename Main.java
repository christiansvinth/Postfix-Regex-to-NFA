import java.util.Stack;
import java.lang.String;
import java.io.*;

public class Main {
  int totalStates = 0;

  /*
   ******** Main Loop / Input *******
   **********************************
   */

  public  void main(String args[]) throws IOException {
    String fileName = args[0];
    try {
      BufferedReader input = new BufferedReader( new FileReader(fileName));
      String thisLine;
      while((thisLine = input.readLine()) != null) {
        String thisExpression = thisLine;
        State thisNFA = createNFA(thisExpression);
        printNFA(thisLine,thisNFA);
      }
      input.close();
    } catch(Exception e) {
      System.out.println("Error reading input file.");
      System.exit(1);
    }
  }

  /* The function createNFA takes a regular expression, turns it
   * into an NFA using the conversion rules specified below in the
   * program, and returns the state at the head of the new NFA.
   *
   * Before popping from the state stack, "validateStack" is called
   * to make sure we have adequate items for our operation; useful
   * for avoiding empty stack exceptions.
   */
  public State createNFA(String Expression) {
    Stack<State> StateStack = new Stack();
    totalStates = 0; //Starting a new NFA, reset this.

    // Loop through the expression
    for(int i = 0; i<Expression.length();i++) {
      char c = Expression.charAt(i);

      // Create new concatenation NFA
      if(c == '&') {
        validateStack(StateStack);
        State Start1 = StateStack.pop();

        validateStack(StateStack);
        State Start2 = StateStack.pop();

        State newState = concat(Start1,Start2);
        StateStack.push(newState);
      }
      // Create new Union NFA
      if(c == '|') {
        validateStack(StateStack);
        State Start1 = StateStack.pop();

        validateStack(StateStack);
        State Start2 = StateStack.pop();

        State newState = union(Start1,Start2);
        StateStack.push(newState);
      }
      // Create new Kleene NFA
      if(c == '*') {
        validateStack(StateStack);
        State Start1 = StateStack.pop();
        State newState = kleeneStar(Start1);
        StateStack.push(newState);
      }
      // Create new NFA which accepts char c
      else {
        State newState = singleChar(c);
        StateStack.push(newState);
      }
    }
    State Done;
    validateStack(StateStack);
    Done = StateStack.pop();
    return Done;
  }

  /*
   ******** Conversion Rules ********
   **********************************
   */


  /* Returns the first state of an NFA which accepts
    a single character c */
  public State singleChar(char c) {
   State newStart = new State(0, c, null, null,  true,false,false);
   assignState(newStart);
   State newFin = new State(0,'0', null, null, false,true,false);
   assignState(newFin);
   return newStart;
  }

  /* Returns the first state of an NFA which accepts
   * the union of of the first and second NFA
   */
  private State union(State first, State second) {
    //Craft start state for the new NFA
    State newStart = new State(0,'E', first, second,  true,false,false);
    assignState(newStart);

    //Craft final state for the new NFA
    State newFin = new State(0,'0',null,null,false,true,false);
    assignState(newFin);

    //Toggle the old Start states to not be that way anymore
    first.startState = false;
    second.startState = false;

    //Toggle the old final states so they E jump to the new final state
    State oldFin1 = fetchFinal(first);
    State oldFin2 = fetchFinal(second);
    oldFin1.finalState = false;
    oldFin2.finalState = false;
    oldFin1.label = 'E';
    oldFin2.label = 'E';

    //Take care we don't accidentally overwrite other transitions...
    if(oldFin1.j != null) {
      oldFin1.j2 = newFin;
    }
    else {
      oldFin1.j = newFin;
    }
    if(oldFin2.j2 != null) {
      oldFin2.j = newFin;
    }
    else {
      oldFin2.j = newFin;
    }

    return newStart;
  }

  /* Returns the start state of a NFA which accepts the concatenation of
   * NFAs first and second. first final -> second start.
   */
  private State concat(State first, State second) {
    second.startState = false;

    //Add an E jump from final of first to start of second
    State oldFin = fetchFinal(first);
    oldFin.finalState = false;
    oldFin.label = 'E';

    //Take care we don't overwrite preexisting transitions...
    if(oldFin.j != null) {
      oldFin.j2 = second;
    }
    else {
      oldFin.j = second;
    }
    return first;
  }

  /* Returns the start state of an NFA which accepts any amount
   * of repetitions of the NFA which starts on the state it was
   * handed.
   */
  private State kleeneStar(State oldStart) {
    oldStart.startState = false;
    //Create a new state which is the start and final state of our new shiny Kleene NFA
    State newStart = new State(0,'E',oldStart,null,true,true,false);
    assignState(newStart);

    //Now to make the old final state point to our new start state
   State oldFin = fetchFinal(oldStart);
   oldFin.finalState = false;
   oldFin.label = 'E';
   if(oldFin.j != null) {
     oldFin.j = newStart;
   }
   else {
     oldFin.j2 = newStart;
   }

   return newStart;

  }

  /*
   ******** Utility Functions *******
   **********************************
   *
   */


  /* Assigns a state number to a state using the
   global variable totalStates */
  public void assignState(State check) {
    if(check.counted == false) {
      check.state = totalStates;
      check.counted = true;
      totalStates++;
      return;
    }
  }

  /* Returns the final state of the NFA which starts at the given
   * state. First checks to see if the given is the final state,
   * otherwise recursively checks all possible paths through NFA
   */
  public State fetchFinal(State check) {
    if(check.finalState == true) {
      return check;
    }
    else {
      State temp = null;
      State temp2 = null;
      if(check.j != null) {
        temp = fetchFinal(check.j);
      }
      if(check.j2 != null) {
        temp2 = fetchFinal(check.j2);
      }
      if(temp.finalState == true) {
        return temp;
      }
      else {
        return temp2;
      }

    }
  }

  /* Helper function which makes sure the stack
     is not empty and exits gracefully if it is */
  public void validateStack(Stack StateStack) {
    if(StateStack.empty()) {
      System.out.println("Invalid expression!");
      System.exit(1);
    }
  }

  public void printNFA(String Expression, State NFA) {

  }

}
