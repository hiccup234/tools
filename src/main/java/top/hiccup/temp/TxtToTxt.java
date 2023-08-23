package top.hiccup.temp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author ocean
 * @since 2023/8/23
 */
public class TxtToTxt {

    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = null;
        File fileOut = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            fileInputStream = new FileInputStream("/Users/ocean/Hiccup/Dev/approval/approval-web/src/test/resources/sql-0823.txt");
            br = new BufferedReader(new InputStreamReader(fileInputStream));

            fileOut = new File("/Users/ocean/Hiccup/Dev/approval/approval-web/src/test/sql-222.txt");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOut)));

            String lineStr = null;
            while ((lineStr = br.readLine()) != null) {
                ///////////////////

                String prex = "UPDATE `approval`.`user_base_info` SET `id`=";
                if (lineStr.startsWith(prex)) {
                    int begin = lineStr.indexOf("`id`=");
                    int end = lineStr.indexOf("`email");

                    String sub = lineStr.substring(begin + 5, end);
                    bw.write(sub);
                    bw.flush();
                } else {
                    System.out.println(lineStr);
                }

                ///////////////////
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
