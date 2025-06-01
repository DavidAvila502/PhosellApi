package com.dev.phosell.session.domain.exception.session;

public class PermissionsSessionException extends RuntimeException {
    private String role;
    private String unallowedAction;

    /**
     * @param role unallowed role for this action - example USER
     * @param unallowedAction the action the user was trying to do - example 'cancel'
     * */
    public PermissionsSessionException(String role, String unallowedAction)
    {
        super(String.format("A user with role: %s can't %s a session",role,unallowedAction));
    }

    public String getRole() {
        return role;
    }

    public String getUnallowedAction() {
        return unallowedAction;
    }
}
