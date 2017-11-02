package space.util.keygen;

/**
 * thrown when a {@link IKey} is used which is not allowed to be used
 * common reasons:
 * - the key is not made by a specific generator
 * - the key is invalid or was released
 */
public class IllegalKeyException extends RuntimeException {
	
	public IllegalKeyException() {
	}
	
	public IllegalKeyException(String message) {
		super(message);
	}
	
	public IllegalKeyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalKeyException(Throwable cause) {
		super(cause);
	}
	
	public IllegalKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
