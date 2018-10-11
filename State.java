/* Christian Svinth - CS 317
 * Project 1 - Postfix Regex to NFA
 *
 * This file contains the definition of the "State" class.
 *
 * State objects have the following attributes:
 *      state       -int representing the state number
 *      label       -the label for the edge to the next state.
 *      j           -the state transitioned to on input "label"
 *      j2          -Another state transitioned to on input "label"
 *      prev        -the previous state in the NFA
 *      startState  -Boolean indicating if this is a start state
 *      finalState  -Boolean indicating if this is a final state
 *      counted     -Boolean to see if we have counted this state
 *
 * j2 is primarily used for epsilon transitions, as that is the only
 * case where we would need to transition to more than one state on
 * a single input in this implementation.
 */

public class State {
  int state;
  char label;
  State j;
  State j2;
  boolean startState;
  boolean finalState;
  boolean counted;


  public State(int state, char label, State j, State j2, boolean startState, boolean finalState, boolean counted) {
    this.state = state;
    this.label = label;
    this.j = j;
    this.j2 = j2;
    this.startState = startState;
    this.finalState = finalState;
    this.counted = counted;
  }
}
