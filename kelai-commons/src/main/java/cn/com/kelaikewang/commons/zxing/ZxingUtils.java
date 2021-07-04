package cn.com.kelaikewang.commons.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ZxingUtils {
    private ZxingUtils(){}
    private static final Map<EncodeHintType, Object> HINTS = new HashMap<>();
    static {
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//设置容错率默认为最高
        HINTS.put(EncodeHintType.CHARACTER_SET, "UTF-8");// 字符编码为UTF-8
        HINTS.put(EncodeHintType.MARGIN, 0);//二维码空白区域,最小为0也有白边,只是很小,最小是6%左右
    }
    public static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height,HINTS);

        Path path = FileSystems.getDefault().getPath(filePath);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }
    public static byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height,HINTS);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        pngOutputStream.close();
        return pngData;
    }
    public static void generateBarCodeImage(String saveAs,String content,int width,int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new Code128Writer().encode(content, BarcodeFormat.CODE_128, width, height, HINTS);
        MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(new File(saveAs)));
    }
    public static byte[] generateBarCodeImage(String content,int width,int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new Code128Writer().encode(content, BarcodeFormat.CODE_128, width, height, HINTS);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "png", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        pngOutputStream.close();
        return pngData;
    }
}
