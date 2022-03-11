package com.company;

import org.apache.commons.codec.binary.Base64OutputStream;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    //Chat_id с телеграмма
    private static final String chat_id = "177794633";

    public static void main(String[] args) {

        //Номер
        int current_excise = 696165;

        //Необходимое количество фотографий
        int photos_required = 1;

        for (int i = 1; i <= photos_required; i++) {
            try {
                Thread.sleep(121);
                generateImage(current_excise, i);
            } catch (Exception e) {
                System.out.print("Something went wrong while generation process");
            }
            current_excise++;
        }
    }

    public static void generateImage(int excise_counter, int photo_counter) throws Exception{

        BufferedImage image;

        String excise_info = String.valueOf(excise_counter);
        String complex_info = photo_counter + ":" + excise_counter;

        //Перечень всех доступных изображений
        File file1 = new File("/Users/vii/IdeaProjects/generator/src/original_images/pic2.jpg");

        //Выбор исходного изображения
        image = ImageIO.read(file1);

        Map<TextAttribute, Object> attributes = new HashMap<>();

        //0.1 - параметр, отвечающий за межбуквенный интервал (диапазон от 0 до 1, дробные числа)
        attributes.put(TextAttribute.TRACKING, 0.1);

        //Подключение кастомного шрифта из папки fonts и задание его размера (deriveFont(53f))
        Font customFont = Font.createFont(Font.TRUETYPE_FONT,
                        new File("/Users/vii/IdeaProjects/generator/src/fonts/PantherScratches.ttf"))
                .deriveFont(53f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font font2 = customFont.deriveFont(attributes);
        ge.registerFont(font2);

        Graphics2D g = image.createGraphics();

        //Код цвета в формате RGBA
        Color c = new Color(161, 68, 29, 255);
        g.setColor(c);
        g.setFont(font2);

        //Отрисовка номера в заданых координатах
        g.drawString(excise_info, 655, 704);

        //g.drawString("AABA", 685, 431);


        //g.drawString("ААБА", 645, 704);
        //Сохранение в заданую директорию
        ImageIO.write(image,
                "jpg", new File("/Users/vii/IdeaProjects/generator/src/custom_images",
                        complex_info +".jpg"));
        System.out.println("Фото №" + photo_counter + " Сохранено");
        System.out.println("Отправить фото в бот ? [1 - да, 0 - нет]");
        b64Encoder(image);
    }

    public static void b64Encoder(BufferedImage image) throws Exception{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStream b64 = new Base64OutputStream(os);
        ImageIO.write(image, "jpg", b64);
        sendRequestToServer(os.toString(StandardCharsets.UTF_8));
    }

    public static void dialog(BufferedImage image) throws Exception {
        System.out.print("Ввод : ");
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        switch (a) {
            case 1 -> b64Encoder(image);
            case 2 -> System.exit(0);
            default -> System.exit(1);
        }
        scanner.close();
    }

    public static void sendRequestToServer(String bigBoy) {

        String httpsURL = "https://bot.myglo.com.ua/api/image/receiveimagefromincust";
        String generalString = "\"data:image/jpeg;base64, " + bigBoy + "\"";
        generalString = generalString.replace("\n", "").
                replace("\r", "");

        URL myurl = null;
        try {
            myurl = new URL(httpsURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ArrayList photos = new ArrayList();
        photos.add(generalString);
        //photos.add(generalString);
        //photos.add(generalString);

        String json = "{\"qrcode\":"+photos+",\"status\":\"success\",\"lang\":\"ua\"," +
                "\"externalMask\":\"https://incust.net/images/mask.svg\",\"mode\":\"TakePhoto\"," +
                "\"externalMaskViewPort\":\"95\",\"url\":\"https://bot.myglo.com.ua/api/image/receiveimagefromincust\"," +
                "\"user_chat_id\":"+chat_id+",\"rW\":\"720\",\"countPhotos\":\"3\",\"delayPhotos\":\"1000\"}";


        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection)myurl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        con.setRequestProperty("Host","bot.myglo.com.ua");
        con.setRequestProperty("Connection","keep-alive");
        con.setRequestProperty("Content-Length", String.valueOf(json.length()));
        con.setRequestProperty("sec-ch-ua"," Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"");
        con.setRequestProperty("Authorization", "Basic YXBpOlNpdkVBVlh4WW1ENkxFUQ==");
        con.setRequestProperty("Content-Type","application/json; charset=utf-8");
        con.setRequestProperty("sec-ch-ua-mobile","?0");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/BuildID) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
        con.setRequestProperty("sec-ch-ua-platform","Android");
        con.setRequestProperty("Accept","*/*");
        con.setRequestProperty("Origin",".com");
        con.setRequestProperty("Sec-Fetch-Site","cross-site");
        con.setRequestProperty("Sec-Fetch-Mode","cors");
        con.setRequestProperty("Sec-Fetch-Dest","empty");
        con.setRequestProperty("Referer","https://qr.incust.com/");
        con.setRequestProperty("Accept-Encoding","gzip, deflate, br");
        con.setRequestProperty("Accept-Language","ru-UA,ru;q=0.9,en-US;q=0.8,en;q=0.7,uk-UA;q=0.6,uk;q=0.5,ru-RU;q=0.4");

        con.setDoOutput(true);
        con.setDoInput(true);


        DataOutputStream output = null;
        try {
            output = new DataOutputStream(con.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output.write(json.getBytes(Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Response Code : "+con.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}