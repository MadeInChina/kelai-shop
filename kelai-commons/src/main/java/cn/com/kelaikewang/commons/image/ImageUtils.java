package cn.com.kelaikewang.commons.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by xlabs on 3/14/2017.
 */
public class ImageUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);
    public static boolean merge(String bgImagePath,String imagePath,int x,int y,String savePath)
    {
        try
        {
            //读取第一张图片
            File fileOne = new File(bgImagePath);

            BufferedImage ImageOne = null; //ImageIO.read(fileOne);
            if (fileOne.exists()){
                ImageOne = ImageIO.read(fileOne);
            }else {
                ImageOne = ImageIO.read(ImageUtils.class.getResourceAsStream(bgImagePath));
            }
            int width = ImageOne.getWidth();//图片宽度
            int height = ImageOne.getHeight();//图片高度
            //从图片中读取RGB
            int[] ImageArrayOne = new int[width*height];
            ImageArrayOne = ImageOne.getRGB(0,0,width,height,ImageArrayOne,0,width);
            //对第二张图片做相同的处理
            File fileTwo = new File(imagePath);
            BufferedImage ImageTwo = null;//ImageIO.read(fileTwo);
            if (fileTwo.exists()){
                ImageTwo = ImageIO.read(fileTwo);
            }else {
                ImageTwo = ImageIO.read(ImageUtils.class.getResourceAsStream(imagePath));
            }
            int widthTwo = ImageTwo.getWidth();//图片宽度
            int heightTwo = ImageTwo.getHeight();//图片高度
            int[] ImageArrayTwo = new int[widthTwo*heightTwo];
            ImageArrayTwo = ImageTwo.getRGB(0,0,widthTwo,heightTwo,ImageArrayTwo,0,widthTwo);

            //生成新图片
            BufferedImage ImageNew = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
            ImageNew.setRGB(0,0,width,height,ImageArrayOne,0,width);//设置左半部分的RGB
            ImageNew.setRGB(x,y,widthTwo,heightTwo,ImageArrayTwo,0,widthTwo);//设置右半部分的RGB
            ImageNew.setRGB(x,y,widthTwo,heightTwo,ImageArrayTwo,0,widthTwo);//设置右半部分的RGB
            File outFile = new File(savePath);
            ImageIO.write(ImageNew, "png", outFile);//写图片

            return true;

        }
        catch(Exception e)
        {
            LOGGER.error("合并图片错误",e);
            e.printStackTrace();
            return false;
        }
    }

}
