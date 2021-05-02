package com.keisse.gtabot;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import winkeyboard.Keyboard;
import winkeyboard.ScanCode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MultiResolutionImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Random;

public class Mainapp {

    public static final String BASE_URI = "src/main/resources/";
    public static final String TESSDATA_URI = "C:/Program Files/Tesseract-OCR/tessdata";
    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final Rectangle CAPTURE_RECT = new Rectangle(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        Mainapp main = new Mainapp();
        main.run();
    }

    public void run() throws InterruptedException {
        File tmpFolder = LoadLibs.extractTessResources("win32-x86-64");
        System.setProperty("java.library.path", tmpFolder.getPath());
        System.out.println(tmpFolder.getAbsolutePath());
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("eng");
        tesseract.setOcrEngineMode(1);

        var tessDataDir = Paths.get(TESSDATA_URI);
        tesseract.setDatapath(tessDataDir.toAbsolutePath().toString());

        countDowntimer();
//        captureScreen(); // Get out of comment to test
        while (true) {
            Thread.sleep(getRandomBetween(1000, 2000));
            mainLoop(tesseract);
        }
    }

    private void mainLoop(Tesseract tesseract) throws InterruptedException {
        captureScreen();
        String result = ocrImage(new File(BASE_URI + "PartialScreenshot.jpg"), tesseract);

        System.out.println(result);

        if (result.contains("1")) {
            keyActions(ScanCode.DIK_1);
        } else if (result.contains("2")) {
            keyActions(ScanCode.DIK_2);
        } else if (result.contains("3")) {
            keyActions(ScanCode.DIK_3);
        } else if (result.contains("4")) {
            keyActions(ScanCode.DIK_4);
        } else if (result.contains("5")) {
            keyActions(ScanCode.DIK_5);
        } else if (result.contains("6")) {
            keyActions(ScanCode.DIK_6);
        } else if (result.contains("7")) {
            keyActions(ScanCode.DIK_7);
        } else if (result.contains("8")) {
            keyActions(ScanCode.DIK_8);
        }
    }

    private void countDowntimer() throws InterruptedException {
        for (int i = 4; i > 0; i--) {
            Thread.sleep(1000);
            System.out.println(i);
        }
    }

    public void keyActions(int scanCode) throws InterruptedException {
        Keyboard keyboard = new Keyboard();
        int randomKeypress = getRandomBetween(250, 1000);
        int keyPressRelease = getRandomBetween(100, 250);

        Thread.sleep(randomKeypress);
        keyboard.winKeyPress(scanCode);
        Thread.sleep(keyPressRelease);
        keyboard.winKeyRelease(scanCode);
    }

    private int getRandomBetween(int minimum, int maximum) {
        // Next int will always return between 0 and bound, if you want to be between range you have to add minimum
        // And subtract minimum from maximum
        return new Random().nextInt(maximum - minimum) + minimum;
    }

    private String ocrImage(File file, Tesseract tesseract) {
        BufferedImage image;
        String result = null;
        try {

            image = ImageIO.read(file.toURI().toURL());
            result = tesseract.doOCR(image);

        } catch (IOException | TesseractException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void captureScreen() {
        try {

            Robot robot = new Robot();
            String format = "jpg";
            String fileName = "PartialScreenshot." + format;
            String testFile = "testPressKey.png";              // Test Image

            MultiResolutionImage test = robot.createMultiResolutionScreenCapture(CAPTURE_RECT);
            BufferedImage correctRes = (BufferedImage) test.getResolutionVariants().get(1);

            BufferedImage crop = correctRes.getSubimage(17, 1037, 350, 70);
            ImageIO.write(crop, format, new File(BASE_URI + fileName));

        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }

    }

}

