import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiIntegrationTest {

    @Test
    public void testInserirUsuario() throws Exception {
        URL randomUserApiUrl = new URL("https://randomuser.me/api/");
        HttpURLConnection randomUserConnection = (HttpURLConnection) randomUserApiUrl.openConnection();
        randomUserConnection.setRequestMethod("GET");

        int randomUserResponseCode = randomUserConnection.getResponseCode();
        assertEquals(200, randomUserResponseCode, "Falha na requisição para a API pública de usuários aleatórios");

        StringBuilder randomUserContent = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(randomUserConnection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                randomUserContent.append(line);
            }
        }
        randomUserConnection.disconnect();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userData = objectMapper.readTree(randomUserContent.toString());

        String nome = userData.at("/results/0/name/first").asText();
        String senha = userData.at("/results/0/login/password").asText();

        String apiUrl = "http://localhost:4567/usuarios";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = String.format("{\"nome\":\"%s\",\"senha\":\"%s\"}", nome, senha);

        connection.getOutputStream().write(jsonInputString.getBytes());

        int responseCode = connection.getResponseCode();

        connection.disconnect();

        assertEquals(201, responseCode, "Falha na requisição para inserir usuário");

    }
}