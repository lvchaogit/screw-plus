# screw-plus

## 简介

&emsp;&emsp;在项目交付过程中，会经常写数据库设计文档，由于不同的项目，数据库设计文档格式可能也不同；而世面上开源的工具大多都是基于固定模板生成，如果要自定义模板则需要了解相关的技术及原理（主要freemarker），才可根据自定义模板生成文档，相对来讲有一定技术门槛，可能会相对复杂；<br/>
&emsp;&emsp;于是思考有没有一种办法能降低门槛，能尽可能方便的生根据自定义格式，生成数据库设计文档；经过研究比对，选择基于[screw](https://github.com/pingfangushi/screw) 进行扩展，提供一个可以根据自定义格式生成数据库设计文档工具；

## 实现原理
&emsp;&emsp;freemarker模板，本质上其实就是在Word的xml里添加ftl标签，然后通过解析ftl标签，填充数据，生成文档；<br/>
&emsp;&emsp;于是我通过在Word模板中添加“自定义标签占位符”，然后由程序去解析，替换成相应的ftl标签，生成模板，然后生成文档；通过该方式，尽量减低自定义模板的技术难度；使用者不必去关心freemarker相关技术（建议了解一下），从而提高文档编写效率；<br/>

## 基本流程图
![处理流程](https://user-images.githubusercontent.com/23471936/142209143-894f0237-1f53-4381-9952-572bdad2678a.png)

## 特点（基于screw扩展）

+ 简化Word自定义模板
+ 支持自定义检索数据表信息

## 数据库支持（与screw一致，可兼容升级）

- [x] MySQL 
- [x] MariaDB 
- [x] TIDB 
- [x] Oracle 
- [x] SqlServer 
- [x] PostgreSQL
- [x] Cache DB（2016）
- [ ] H2 （开发中）
- [ ] DB2  （开发中）
- [ ] HSQL  （开发中）
- [ ] SQLite（开发中）
- [ ] 瀚高（开发中）
- [ ] 达梦 （开发中）
- [ ] 虚谷  （开发中）
- [ ] 人大金仓（开发中）

## 文档生成支持

- [x] html
- [x] word（支持自定义模板）
- [x] markdown

## 文档截图（crew默认模板）

+ **html**

<p align="center">
   <img alt="HTML" src="https://images.gitee.com/uploads/images/2020/0622/161414_74cd0b68_1407605.png">
</p>
<p align="center">
   <img alt="screw-logo" src="https://images.gitee.com/uploads/images/2020/0622/161723_6da58c41_1407605.png">
</p>

+ **word**

<p align="center">
   <img alt="word" src="https://images.gitee.com/uploads/images/2020/0625/200946_1dc0717f_1407605.png">
</p>

+ **markdwon**

<p align="center">
   <img alt="markdwon" src="https://images.gitee.com/uploads/images/2020/0625/214749_7b15d8bd_1407605.png">
</p>
<p align="center">
   <img alt="markdwon" src="https://images.gitee.com/uploads/images/2020/0625/215006_3601e135_1407605.png">
</p>

## 使用方式
### 自定义文本（不区分大小写）
|  标签   | 释义  |
|  :-----  | :-----  |
| colStart  | 字段遍历开始标签 |
| colStop  |  字段遍历结束标签|
| tbName  |  数据表名称|
| tbRemarks  |  数据表注释|
| colIndex  |  字段序号|
| colName  |  字段名|
| colType  |  字段类型|
| colSize  |  字段长度|
| colDecimal  |  字段小数位数|
| colNullable  |  字段是否为空|
| colPrimary  |  字段是否主键|
| colDef  |  字段默认值|
| colRemark  |  字段注释|

### 普通方式

+ **引入依赖**

```xml
<dependency>
    <groupId>cn.smallbun.screw</groupId>
    <artifactId>screw-plus-core</artifactId>
    <version>${lastVersion}</version>
 </dependency>
```

+ **编写代码（默认模板）**

``` java
/**
 * 文档生成
 */
void documentGeneration() {
   //数据源
   DataSource dataSource = DataSourceUtil.getDataSource("com.mysql.cj.jdbc.Driver",
               "jdbc:mysql://127.0.0.1:3306/database",
               "root", "password");
   //生成配置
   EngineConfig engineConfig = EngineConfig.builder()
         //生成文件路径
         .fileOutputDir(fileOutputDir)
         //打开目录
         .openOutputDir(true)
         //文件类型
         .fileType(EngineFileType.HTML)
         //生成模板实现
         .produceType(EngineTemplateType.freemarker)
         //自定义文件名称
         .fileName("自定义文件名称").build();

   //忽略表
   ArrayList<String> ignoreTableName = new ArrayList<>();
   ignoreTableName.add("test_user");
   ignoreTableName.add("test_group");
   //忽略表前缀
   ArrayList<String> ignorePrefix = new ArrayList<>();
   ignorePrefix.add("test_");
   //忽略表后缀    
   ArrayList<String> ignoreSuffix = new ArrayList<>();
   ignoreSuffix.add("_test");
   ProcessConfig processConfig = ProcessConfig.builder()
         //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置	
		 //根据名称指定表生成
		 .designatedTableName(new ArrayList<>())
		 //根据表前缀生成
		 .designatedTablePrefix(new ArrayList<>())
		 //根据表后缀生成	
		 .designatedTableSuffix(new ArrayList<>())
         //忽略表名
         .ignoreTableName(ignoreTableName)
         //忽略表前缀
         .ignoreTablePrefix(ignorePrefix)
         //忽略表后缀
         .ignoreTableSuffix(ignoreSuffix).build();
   //配置
   Configuration config = Configuration.builder()
         //版本
         .version("1.0.0")
         //描述
         .description("数据库设计文档生成")
         //数据源
         .dataSource(dataSource)
         //生成配置
         .engineConfig(engineConfig)
         //生成配置
         .produceConfig(processConfig)
         .build();
   //执行生成
   new DocumentationExecute(config).execute();
}
```

+ **编写代码（自定义模板）**

``` java
/**
 * 文档生成
 */
void documentGeneration() {
   //数据源
   DataSource dataSource = DataSourceUtil.getDataSource("com.mysql.cj.jdbc.Driver",
            "jdbc:mysql://127.0.0.1:3306/database",
            "root", "password");

   //忽略表
   ArrayList<String> ignoreTableName = new ArrayList<>();
   ignoreTableName.add("test_user");
   ignoreTableName.add("test_group");
   //忽略表前缀
   ArrayList<String> ignorePrefix = new ArrayList<>();
   ignorePrefix.add("test_");
   //忽略表后缀    
   ArrayList<String> ignoreSuffix = new ArrayList<>();
   ignoreSuffix.add("_test");
   ProcessConfig processConfig = ProcessConfig.builder()
         //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置	
		 //根据名称指定表生成
		 .designatedTableName(new ArrayList<>())
		 //根据表前缀生成
		 .designatedTablePrefix(new ArrayList<>())
		 //根据表后缀生成	
		 .designatedTableSuffix(new ArrayList<>())
         //忽略表名
         .ignoreTableName(ignoreTableName)
         //忽略表前缀
         .ignoreTablePrefix(ignorePrefix)
         //忽略表后缀
         .ignoreTableSuffix(ignoreSuffix).build();
   //配置
   Configuration config = Configuration.builder()
         //版本
         .version("1.0.0")
         //描述
         .description("数据库设计文档生成")
         //数据源
         .dataSource(dataSource)
         //生成配置
         .engineConfig(IEngineConfig.getCustomTemplateConfig(自定义模板路径))
         //生成配置
         .produceConfig(processConfig)
         .build();
   //执行生成
   new DocumentationExecute(config).execute();
}
```
## 后续计划

- [ ] 提供自定义模板操作视频 
- [ ] 支持标签+文本组合展示 
- [ ] 保留自定义样式（字体，表格等）
- [ ] 提供web服务 

