package cn.stylefeng.guns.modular.app.controller.market;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;

/**
 *<p>Title: ZipUtil</p>
 *  @author libaoming
 *  @date 2019年1月4日
 *  @version 1.0
 */
public class ZipUtils {
    /**
     * 解压客户端发来的程序
     *
     * @param depressData
     * @return
     * @throws Exception
     */
    public static byte[] decompress(byte[] depressData) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(depressData);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        GZIPInputStream gis = new GZIPInputStream(is);

        int count;
        byte data[] = new byte[1024];
        while ((count = gis.read(data, 0, 1024)) != -1) {
            os.write(data, 0, count);
        }
        gis.close();
        depressData = os.toByteArray();
        os.flush();
        os.close();
        is.close();
        return depressData;
    }

}