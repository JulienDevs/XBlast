package ch.epfl.xblast;

/**
 * Actions the player can perform. Used in the communication between the client
 * and the server.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public enum PlayerAction {

    JOIN_GAME, MOVE_N, MOVE_E, MOVE_S, MOVE_W, STOP, DROP_BOMB;

}
