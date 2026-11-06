package exception;

public class DuplicateSubscriptionException extends Exception {
    public DuplicateSubscriptionException(String message) {
        super(message);
    }
}

//При попытке зарегистрировать пользователя с Email, который уже существует в HashMap