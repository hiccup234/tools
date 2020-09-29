package top.hiccup.temp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 操作文件
 *
 * @author wenhy
 * @date 2020/3/31
 */
public class FileOperate {

    public static void main(String[] args) {
        FileInputStream in = null;
        File fileOut = null;
        try {
            in = new FileInputStream("C:\\Users\\wenhaiyang3\\Desktop\\entrance.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray array = JSON.parseArray(sb.toString());


            fileOut = new File("C:\\Users\\wenhaiyang3\\Desktop\\entrance222.txt");
            final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOut)));

            Set<String> set = new HashSet(64);

            for (Object object : array) {
                JSONObject jsonObject = (JSONObject) object;
                String entarncId = jsonObject.getString("entrance_id");
                String name = jsonObject.getString("template_name");
                String type = jsonObject.getString("template_type");
                String channel = jsonObject.getString("channel_id");
                switch (type) {
                    case "01":
                        type = "广告位";
                        break;
                    case "02":
                        type = "自助服务";
                        break;
                    case "03":
                        type = "常见问题";
                        break;
                    case "04":
                        type = "首页浮标";
                        break;
                    case "05":
                        type = "首页按钮";
                        break;
                }
                set.add(entarncId + "\t\t" + channel + "\t\t" + type + "\t\t" + name);
            }
            set.forEach((s) -> {
                String resultLine = s;
                resultLine += "\r\n";
                try {
                    bw.write(resultLine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bw.flush();
            System.out.println("转换完毕");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
