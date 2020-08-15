//package top.hiccup.util;
//
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
//import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
//import com.baomidou.mybatisplus.generator.config.rules.DbType;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//
///**
// * Created by wenhy on 2018/6/17.
// * mybatis_plus 代码生成器
// */
//public class MpGenerator {
//
//
//    private static String outPutPath = "C:\\Users\\haiyang.wen\\Desktop\\Temp";
//    private static String author = "wenhy";
//
//    private static String driver = "com.mysql.jdbc.Driver";
//    private static String url = "jdbc:mysql://47.106.155.243:3306/redcap?useUnicode=true&autoReconnect=true&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true";
//    private static String userName = "root";
//    private static String password = "234234";
//
//    public static void main(String[] args) {
//        AutoGenerator mpg = new AutoGenerator();
//        // 设置模板引擎，默认为Veloctiy
//        // mpg.setTemplateEngine(new FreemarkerTemplateEngine());
//
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        gc.setOutputDir(outPutPath);
//        gc.setFileOverride(true);
//        // 不需要ActiveRecord特性的请改为false
//        gc.setActiveRecord(false);
//        // XML 二级缓存
//        gc.setEnableCache(false);
//        // XML ResultMap
//        gc.setBaseResultMap(true);
//        // XML columList
//        gc.setBaseColumnList(false);
//        gc.setAuthor(author);
//
//        // 自定义文件命名，注意 %s 会自动填充表实体属性！
//        gc.setMapperName("%sDao");
//        mpg.setGlobalConfig(gc);
//
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setDbType(DbType.MYSQL);
//        dsc.setTypeConvert(new MySqlTypeConvert(){
//            // 自定义数据库表字段类型转换【可选】
//            @Override
//            public DbColumnType processTypeConvert(String fieldType) {
//                System.out.println("转换类型：" + fieldType);
//                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
//                return super.processTypeConvert(fieldType);
//            }
//        });
//        dsc.setDriverName(driver);
//        dsc.setUrl(url);
//        dsc.setUsername(userName);
//        dsc.setPassword(password);
//        mpg.setDataSource(dsc);
//
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        // 表名生成策略
//        strategy.setNaming(NamingStrategy.underline_to_camel);
//        // 需要生成的表
//        strategy.setInclude(new String[]{"delivery_order"});
//        mpg.setStrategy(strategy);
//
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//        pc.setParent("top.hiccup.redcap.bean");
//        pc.setModuleName("po");
//        mpg.setPackageInfo(pc);
//
//        // 关闭默认 xml 生成，调整生成 至 根目录
//        TemplateConfig tc = new TemplateConfig();
//        tc.setXml(null);
//        mpg.setTemplate(tc);
//
//        // 执行生成
//        mpg.execute();
//    }
//
//}