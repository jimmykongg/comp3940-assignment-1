package com.quizapp.quizroom;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JwtUtil {

    // Define a secret key for signing the JWT. In a real application, store this securely.
    private static final String SECRET_KEY = "mySecretKey";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    // Token expiration time (e.g., 30 minutes)
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    // Create a JWT token
    public static String generateToken(String username, String role, String quizIndex) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withClaim("quizIndex", quizIndex)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    // Parse and validate the token
    public static DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); // Reusable verifier instance
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            // Invalid token
            return null;
        }
    }
}

