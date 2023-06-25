package com.mvp.delivery.utils

import com.mvp.delivery.application.security.AuthClientDeserializer
import com.mvp.delivery.domain.client.model.auth.IAuthDTO
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.apache.commons.logging.LogFactory
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object JWTUtils {

    private val logger = LogFactory.getLog(javaClass)
    private val secretPath: String = System.getenv().getOrDefault("SECRET_PATH", "/run/secrets/secret")
    private val secretKeyEnv = System.getenv().getOrDefault("SECRET_KEY", UUID.randomUUID().toString()).toByteArray(StandardCharsets.UTF_8)
    private val secretKey = Keys.hmacShaKeyFor(secretKeyEnv)

    private val applicationName = System.getenv().getOrDefault("APPLICATION_NAME", "no-application-name")
    private const val TOKEN_PREFIX = "Bearer"
    const val HEADER_STRING = "Authorization"

    fun createToken(authDTO: IAuthDTO): String {
        return Jwts.builder()
            .setClaims(authDTO.getClaims())
            .setSubject(authDTO.username.toString())
            .signWith(secretKey)
            .setIssuedAt(Date())
            .setAudience(applicationName)
            .setExpiration(
                Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
            )
            .compact()
    }

    fun verify(token: String): IAuthDTO? {
        return try {
            val user = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token.replace("$TOKEN_PREFIX ", ""))
            AuthClientDeserializer.getInstance(user.body)
        } catch (e: Exception){
            logger.error("Ocorreu um erro ao verificar o token")
            null
        }
    }
}