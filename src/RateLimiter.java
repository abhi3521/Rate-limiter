import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RateLimiter<I, O> {

    private final int allowedRate;
    private final long window;
    private final Map<I, Long> callTimestamps = new HashMap<>();

    public RateLimiter(int allowedRate, long window) {
        this.allowedRate = allowedRate;
        this.window = window * 1000; // Convert seconds to milliseconds
    }

    public Function<I, O> wrap(Function<I, O> func) {
        return input -> {
            synchronized (callTimestamps) {
                long currentTime = System.currentTimeMillis();
                long windowStart = currentTime - window;
                callTimestamps.entrySet().removeIf(entry -> entry.getValue() < windowStart);
                if (callTimestamps.size() >= allowedRate) {
                    throw new RateLimitExceededException("Rate limit exceeded");
                }
                callTimestamps.put((I) input, currentTime);
            }
            return func.apply(input);
        };
    }

    // Exception class for rate limit exceeded
    public static class RateLimitExceededException extends RuntimeException {
        public RateLimitExceededException(String message) {
            super(message);
        }
    }
}
