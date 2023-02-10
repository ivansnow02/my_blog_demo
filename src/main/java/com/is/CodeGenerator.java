package com.is;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {


        FastAutoGenerator.create("jdbc:mysql://localhost:3306/blog?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai", "root", "254940Sr")
                .globalConfig(builder -> {
                    builder.author("is") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\Users\\eone2\\IdeaProjects\\blog_demo\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.is") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D://1//")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("m_user") // 设置需要生成的表名
                            .addTablePrefix("m_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
