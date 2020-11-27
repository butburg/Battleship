package exceptions;

/**
 * @author Edwin W (HTW) on Nov 2020
 * This is my current List of Error-Msg. Later it can be replaced by resources
 */
public interface ExceptionMsg {
    String ph_wrongPhase = "Wrong phase!";

    String bs_playerNameTaken = "Name already taken!";
    String bs_wrongPlayer = "Wrong player!";
    String bs_wrongTurn1 = "Not your turn! Its the second players!";
    String bs_wrongTurn2 = "Not your turn! Its the firsts players!";
    String bs_shipAllSet = "All ships are already set!";
    String bs_shipTypeAllSet = "All ships of this type are already set!";

    String oc_shipOutside = "Ship is not inside the field!";
    String oc_attackOutside = "You can't attack outside of the field!";
    String oc_shipCollidingPosition = "Ship-position is invalid! A another ship is placed there!";
    String oc_attackedAlready = "You attacked already here!";
    String oc_shipTouching = "The ships are not allowed to touch each other!";

    String sh_shipUnawareOfCoordinate = "The ship don't know the Coordinate! Shouldn't be possible!!!";
    String sh_shipFieldInvalid = "The ship dont have that field. Make sure the field is in the ships length and positive!";
    String sh_wrongLocate = "Ship is not proper located!";
}
