package exceptions;

//is a custom unchecked exception that extends RuntimeException,
//used as the base for all interpreter-specific errors.
public class MyException extends RuntimeException{
    public MyException(String message){
        super(message);
    }
}
