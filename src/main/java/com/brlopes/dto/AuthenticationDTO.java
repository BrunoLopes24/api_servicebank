package com.brlopes.dto;

/**
 * The AuthenticationDTO class is a record class in Java.
 * It is used to store and retrieve the login and password details for authentication.
 * This class is typically used to transfer data between processes.
 * As a record, it has a concise syntax for declaring simple, immutable data carrier classes and
 * it automatically provides several methods such as equals, hashCode, toString, getters etc.
 *
 * @param login This is the login provided by the user for authentication.
 * @param password This is the password provided by the user for authentication.
 */
public record AuthenticationDTO (String login, String password) {

}