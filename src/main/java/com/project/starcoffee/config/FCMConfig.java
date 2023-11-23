package com.project.starcoffee.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FCMConfig {

    @Value("${fcm.key.path}")
    private String googleApplicationCredentials;

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        ClassPathResource resource = new ClassPathResource(googleApplicationCredentials);
        InputStream refreshToken = resource.getInputStream();

        List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

        FirebaseApp firebaseApp = firebaseAppList.stream()
                .filter(app -> app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                .findFirst()
                .orElseGet(() -> {
                    FirebaseOptions options = null;
                    try {
                        options = FirebaseOptions.builder()
                                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return FirebaseApp.initializeApp(options);
                });

        return FirebaseMessaging.getInstance(firebaseApp);
// 출처 : https://kbwplace.tistory.com/179

    }
}
