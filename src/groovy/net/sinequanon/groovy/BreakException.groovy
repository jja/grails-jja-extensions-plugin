package net.sinequanon.groovy

/**
 * throw this to short-circuit or break out of a closure
 */
class BreakException extends Exception { }

// if this begins to fail in jdk7
// due to va.lang.IncompatibleClassChangeError: the number of constructors during runtime and compile time for java.lang.Exception do not match
// then add constructors, esp no-arg ctor that call super:
// public BreakException() { super() }
// public BreakException(String message) { super(message) } 
