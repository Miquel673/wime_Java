package com.example.Wime_java.service;

import java.sql.Date;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.example.Wime_java.Auth.OAuthSessionStore;
import com.example.Wime_java.config.GoogleOAuthConfig;
import com.example.Wime_java.model.Tarea;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import jakarta.servlet.http.HttpSession;

@Service
public class GoogleCalendarService {

    private final OAuthSessionStore sessionStore;
    private final GoogleOAuthConfig googleConfig;
    private final HttpSession httpSession;

    public GoogleCalendarService(
            OAuthSessionStore sessionStore,
            GoogleOAuthConfig googleConfig,
            HttpSession httpSession
    ) {
        this.sessionStore = sessionStore;
        this.googleConfig = googleConfig;
        this.httpSession = httpSession;
    }

    /**
     * Refresca el access token usando el refresh token guardado
     */
    private String refreshAccessToken(String refreshToken) throws Exception {

        var tokenRequest = new GoogleRefreshTokenRequest(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                refreshToken,
                googleConfig.getClientId(),
                googleConfig.getClientSecret()
        );

        var tokenResponse = tokenRequest.execute();
        return tokenResponse.getAccessToken();
    }

    /**
     * Crea un evento en Google Calendar con los datos de una Tarea
     */
public void crearEventoDeTarea(Tarea tarea) throws Exception {

    // 1️⃣ Obtener el access token actual de sesión
    String accessToken = sessionStore.getAccessToken(httpSession);

    // 2️⃣ Si no existe, intentar renovarlo con el refresh token
    if (accessToken == null) {

        String refresh = sessionStore.getRefreshToken(httpSession);

        if (refresh != null) {
            accessToken = refreshAccessToken(refresh);
            sessionStore.updateAccessToken(httpSession, accessToken);
        } else {
            System.out.println("❌ Usuario no autenticado con Google Calendar.");
            return;
        }
    }

    final String finalToken = accessToken;

    // 3️⃣ Crear cliente de Google Calendar
    var httpTransport = GoogleNetHttpTransport.newTrustedTransport();

    HttpRequestInitializer requestInitializer = request ->
            request.getHeaders().setAuthorization("Bearer " + finalToken);

    Calendar calendar = new Calendar.Builder(
            httpTransport,
            GsonFactory.getDefaultInstance(),
            requestInitializer
    )
    .setApplicationName("Wime")
    .build();

    // 4️⃣ Crear el evento con la información de la tarea
    Event event = new Event()
            .setSummary(tarea.getTitulo())
            .setDescription(tarea.getDescripcion());

    // 5️⃣ Convertir LocalDate a DateTime para Google Calendar
    var startDate = Date.from(
            tarea.getFechaLimite()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
    );

    var endDate = Date.from(
            tarea.getFechaLimite()
                .plusDays(1) // Google requiere esto para eventos de día completo
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
    );

    var start = new EventDateTime()
            .setDate(new com.google.api.client.util.DateTime(startDate));

    var end = new EventDateTime()
            .setDate(new com.google.api.client.util.DateTime(endDate));

    event.setStart(start);
    event.setEnd(end);

    // 6️⃣ Enviar el evento al calendario
    calendar.events()
            .insert("primary", event)
            .execute();

    System.out.println("✔️ Evento creado en Google Calendar: " + tarea.getTitulo());

    System.out.println("DEBUG → Se llamó crearEventoDeTarea()");
    System.out.println("Fecha límite recibida: " + tarea.getFechaLimite());
    System.out.println("Título recibido: " + tarea.getTitulo());

}
}