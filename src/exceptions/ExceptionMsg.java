package exceptions;

/**
 * @author Edwin W (HTW) on Nov 2020
 * This is my current List of Error-Msg. Later it can be replaced by resources
 */
public interface ExceptionMsg {
    String playerNameTaken = "Name already taken!";
    String wrongPhase = "Wrong phase!";
    String wrongPlayer = "Wrong player!";
    String wrongTurn1 = "Not your turn! Its the second players!";
    String wrongTurn2 = "Not your turn! Its the firsts players!";
    String shipOutside = "Ship is not inside the field!";
    String shipAllSet = "All ships are already set!";
    String shipTypeAllSet = "All ships of this type are already set!";
    String attackOutside = "You can't attack outside of the field!";

    String shipCollidingPosition = "Ship-position is invalid! A another ship is placed there!";
    String shipUnawareOfCoordinate = "The ship don't know the Coordinate! Shouldn't be possible!!!";
    String shipFieldInvalid = "The ship dont have that field. Make sure the field is in the ships length and positive!";
}
