import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class RateLimiterTest {

    @Test
    void testRateLimitExceeded() {
        RateLimiter<Integer, Integer> limiter = new RateLimiter<>(2, 1); // Allow 2 calls within a 1 sec window

        Function<Integer, Integer> square = x -> x * x;
        Function<Integer, Integer> rateLimitedSquare = limiter.wrap(square);

        assertDoesNotThrow(() -> rateLimitedSquare.apply(1)); // First call should succeed
        assertDoesNotThrow(() -> rateLimitedSquare.apply(2)); // Second call should succeed
        assertThrows(RateLimiter.RateLimitExceededException.class, () -> rateLimitedSquare.apply(3)); // Third call should fail
    }

    @Test
    void testRateLimitNotExceeded() {
        RateLimiter<Integer, Integer> limiter = new RateLimiter<>(10, 1); // Allow 3 calls within a 1 sec window

        Function<Integer, Integer> square = x -> x * x;
        Function<Integer, Integer> rateLimitedSquare = limiter.wrap(square);

        for(int i = 1; i <= 10; i++) {
            assertEquals(i * i, (int) rateLimitedSquare.apply(i));
        }

        //assertEquals(1, (int) rateLimitedSquare.apply(1));
//        assertEquals(5 * 5, (int) rateLimitedSquare.apply(5));
//        //assertDoesNotThrow(() -> rateLimitedSquare.apply(1)); // First call should succeed
//        assertDoesNotThrow(() -> rateLimitedSquare.apply(2)); // Second call should succeed
//        assertDoesNotThrow(() -> rateLimitedSquare.apply(3)); // Third call should succeed
       // assertThrows(RateLimiter.RateLimitExceededException.class, () -> rateLimitedSquare.apply(4)); // Fourth call should fail
    }

    @Test
    void testDifferentInputs() {
        RateLimiter<Integer, Integer> limiter = new RateLimiter<>(2, 1); // Allow 2 calls within a 1 sec window

        Function<Integer, Integer> square = x -> x * x;
        Function<Integer, Integer> rateLimitedSquare = limiter.wrap(square);

        assertDoesNotThrow(() -> rateLimitedSquare.apply(1)); // First call should succeed
        assertDoesNotThrow(() -> rateLimitedSquare.apply(2)); // Second call should succeed
        assertThrows(RateLimiter.RateLimitExceededException.class, () -> rateLimitedSquare.apply(1)); // Call with same input should fail
    }
}
