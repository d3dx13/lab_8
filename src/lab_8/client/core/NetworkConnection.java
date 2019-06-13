package lab_8.client.core;

import lab_8.Settings;
import lab_8.message.Crypted;
import lab_8.message.Message;
import lab_8.message.loggingIn.*;
import lab_8.message.registration.*;
import lab_8.crypto.ObjectCryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Arrays;

import static lab_8.Settings.*;
import static lab_8.client.ClientGUI.StartWindow.*;

/**
 * Класс для реализации сетевой коммуникации на стороне клиента.
 */
public class NetworkConnection {
    /**
     * Отправить команду на сервер и получить ответ на неё.
     * @param message Команда
     * @return Ответ
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws ClassNotFoundException
     */
    public static Message command(Message message) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException {
        Object response = objectSend(objectCryption.messageEncrypt(message));
        return objectCryption.messageDecrypt((Crypted)response);
    }
    /**
     * Настроить сетевое соединение: Установить Адрес сервера и порт.
     * @param hostname Адрес сервера
     * @param port Порт
     */
    public static void setServerAddress(String hostname, int port){
        serverAddress = new InetSocketAddress(hostname, port);
    }
    /**
     * @return Текущее соединение.
     */
    public static InetSocketAddress getServerAddress(){
        return serverAddress;
    }
    /**
     * Процесс регистрации.
     * @return Успешность
     */
    public static String signUp(String email) {
        StringBuffer resultString = new StringBuffer();
        try {
            RegistrationRequest registrationRequest = new RegistrationRequest();
            registrationRequest.login = objectCryption.getUserLogin();
            if (objectCryption.getUserLogin().length() < loginMinimalLength || objectCryption.getUserLogin().length() > loginMaximalLength) {
                resultString.append("!!! Login must be %d to %d characters !!!\n");
                return resultString.toString();
            }
            registrationRequest.email = email;
            resultString.append("Waiting for registration from the server\n");
            RegistrationResponse registrationResponse = registration(registrationRequest);
            resultString.append("Registration: \n");
            if (registrationResponse.confirm) {
                resultString.append(registrationResponse.message);
                return resultString.toString();
            } else
                resultString.append("failed\nReason: " + registrationResponse.message + "\n");
            return resultString.toString();
        }catch (UnresolvedAddressException ex){
            resultString.append("Address is incorrect\n");
            return resultString.toString();
        } catch (Exception ex){
            resultString.append(ex.getMessage() + "\n");
            return resultString.toString();
        }
    }
    /**
     * Процесс авторизации.
     * @return Успешность
     */
    public static String signIn(String password) {
        StringBuffer resultString = new StringBuffer();
        try {
            IdentificationRequest identificationRequest = new IdentificationRequest();
            identificationRequest.login = NetworkConnection.objectCryption.getUserLogin();
            IdentificationResponse identificationResponse = identification(identificationRequest);
            AuthenticationRequest authenticationRequest = new AuthenticationRequest();
            resultString.append("Logging in...\n");

            MessageDigest sha = MessageDigest.getInstance("SHA-224");
            SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(sha.digest(password.getBytes(Charset.forName("UTF-8"))), userAESKeySize), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] privateKey;
            resultString.append(identificationResponse.message);
            try {
                privateKey = cipher.doFinal(identificationResponse.privateKey);
            } catch (Exception ex) {
                resultString.append("Password incorrect\n");
                return resultString.toString();
            }
            Cipher cipher2 = Cipher.getInstance("RSA");
            cipher2.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKey)));
            try {
                authenticationRequest.random = cipher2.doFinal(identificationResponse.random);
            } catch (Exception e) {
                resultString.append("Password incorrect\n");
                return resultString.toString();
            }
            authenticationRequest.login = NetworkConnection.objectCryption.getUserLogin();
            AuthenticationResponse authenticationResponse = authentication(authenticationRequest);
            if (authenticationResponse.message.equals("success")) {
                byte[] secretKey;
                try {
                    secretKey = cipher2.doFinal(authenticationResponse.secretKey);
                } catch (Exception e) {
                    resultString.append("Password incorrect\n");
                    return resultString.toString();
                }
                objectCryption.setSecretKey(secretKey);
                return resultString.toString()+"\nsuccess\n";
            }
            resultString.append("Authentication failed: " + authenticationResponse.message + "\n");
            return resultString.toString();
        } catch (Exception ex){
            resultString.append(ex.getMessage() + "\n");
            return resultString.toString();
        }
    }
    /**
     * Отправить Object на сервер и получить Object в ответ.
     * @param message отправляемый Object
     * @return получаемый Object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static Object objectSend(Object message) throws IOException, ClassNotFoundException {
        SocketChannel server = SocketChannel.open(serverAddress);
        ByteBuffer outBuffer = ByteBuffer.wrap(objectCryption.messageSerialize(message));
        server.write(outBuffer);
        outBuffer.clear();
        ByteBuffer byteBuffer = ByteBuffer.allocate(clientReceiveBuffer);
        long time = Instant.now().getEpochSecond();
        while (server.read(byteBuffer) != -1 && (Instant.now().getEpochSecond() - time < clientReceiveTimeout)){ }
        Object response = objectCryption.messageDeserialize(byteBuffer.array());
        server.close();
        return response;
    }
    /**
     * Процесс идентификации пользователя.
     * @return Ответ сервера
     */
    private static IdentificationResponse identification (IdentificationRequest request) throws IOException, ClassNotFoundException {
        Object response = objectSend(request);
        return (IdentificationResponse)response;
    }
    /**
     * Процесс регистрации пользователя.
     * @return Ответ сервера
     */
    private static RegistrationResponse registration (RegistrationRequest request) throws IOException, ClassNotFoundException {
        Object response = objectSend(request);
        return (RegistrationResponse)response;
    }
    /**
     * Процесс аутентификации пользователя.
     * @return Ответ сервера
     */
    private static AuthenticationResponse authentication (AuthenticationRequest request) throws IOException, ClassNotFoundException {
        Object response = objectSend(request);
        return (AuthenticationResponse)response;
    }
    /**
     * Текущее сетевое соединение.
     */
    private static InetSocketAddress serverAddress = new InetSocketAddress(Settings.databaseHost, Settings.databasePort);
    /**
     * Экземпляр класса ObjectCryption для работы с шифрованием и сериализацией.
     */
    public static ObjectCryption objectCryption = new ObjectCryption();
}
