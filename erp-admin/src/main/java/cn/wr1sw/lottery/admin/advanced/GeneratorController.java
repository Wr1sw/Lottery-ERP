package cn.wr1sw.lottery.admin.advanced;

import cn.wr1sw.lottery.common.base.BaseController;
import cn.wr1sw.lottery.common.base.BaseResult;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2021/12/29 18:50
 */
@Controller
@AllArgsConstructor
@RequestMapping("/system/develop")
public class GeneratorController extends BaseController {

    private DataSourceProperties dataSourceProperties;

    /**
     * 访问[代码生成]页面
     *
     * @return 页面跳转
     */
    @RequestMapping("generator")
    public String generatorIndex() {
        return "system/advanced/generator";
    }

    @GetMapping("/generator/list")
    @ResponseBody
    public BaseResult getTableList() {
        // 获取链接数据库的基本信息，并填入配置类
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setUrl(dataSourceProperties.getUrl())//
                .setSchemaName("public")     //数据库 schema name
                .setDbType(DbType.MYSQL)
                .setDriverName(dataSourceProperties.getDriverClassName())
                .setUsername(dataSourceProperties.getUsername())
                .setPassword(dataSourceProperties.getPassword());

        // 策略配置(数据库表配置)
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setNaming(NamingStrategy.underline_to_camel)           // 数据库表映射到实体的命名策略:下划线转驼峰
                .setColumnNaming(NamingStrategy.underline_to_camel)     // 数据库表字段映射到实体的命名策略:下划线转驼峰
                .setEntityLombokModel(true)                             // 实体是否为lombok模型（默认 false）
                .setControllerMappingHyphenStyle(true);                 // 驼峰转连字符

        // 获取所有的表
        ConfigBuilder configBuilder = new ConfigBuilder(null, dsConfig, strategyConfig, null, null);
        List<TableInfo> tableInfos = configBuilder.getTableInfoList();
        return success(200, tableInfos);
    }

    /**
     * 生成代码
     *
     * @param projectPack 项目路径
     * @param moduleName  模块名
     * @param parentPack  父包名
     * @param author      作者
     * @param tableName   表名
     * @return 结果
     */
    @PostMapping("/generator/create")
    @ResponseBody
    public BaseResult CodeGenerator(String projectPack, String moduleName, String parentPack, String author, String tableName) {
        // 代码生成器类
        AutoGenerator mpg = new AutoGenerator();

        // 项目根目录
        String projectPath = System.getProperty("user.dir");
        // D:\IdeaProjects\2021_ExciteCMS\ExciteCMS

        // 六类文件的保存路径设置，可以根据自己项目的实际情况修改
        String entityPath = projectPath + "/excite-" + moduleName + projectPack + moduleName + "/entity/";
        String servicePath = projectPath + "/excite-" + moduleName + projectPack + moduleName + "/service/";
        String serviceImplPath = projectPath + "/excite-" + moduleName + projectPack + moduleName + "/service/impl/";
        String mapperPath = projectPath + "/excite-" + moduleName + projectPack + moduleName + "/mapper";
        String mapperXmlPath = projectPath + "/excite-" + moduleName + "/src/main/resources/mapper/";
        String controllerPath = projectPath + "/excite-admin/src/main/java/com/zxdmy/excite/admin/controller/" + moduleName + "/";

        // 1. 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig
                //  代码生成的目录
//                .setOutputDir(projectPath + "/src/main/java")
                .setAuthor(author)          //  修改项目作者
                .setOpen(false)             // 是否打开输出目录
                .setIdType(IdType.AUTO)     // 主键策略
                .setFileOverride(false)     // 是否覆盖已有文件
                .setBaseResultMap(true)     // 是否开启 BaseResultMap（XML文件）
                .setBaseColumnList(true);   // 是否开启 baseColumnList（XML文件）
//                .setServiceName("");

        //把全局配置添加到代码生成器主类
        mpg.setGlobalConfig(globalConfig);

        // 2. 数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig
                // 数据库链接URL
                .setUrl(dataSourceProperties.getUrl())
                // .setSchemaName("public")     //数据库 schema name
                .setDbType(DbType.MYSQL)        // 数据库类型
                .setDriverName(dataSourceProperties.getDriverClassName())
                .setUsername(dataSourceProperties.getUsername())
                .setPassword(dataSourceProperties.getPassword());

        // 把数据源配置添加到代码生成器主类
        mpg.setDataSource(dsConfig);

        // 3. 包配置
        PackageConfig pkConfig = new PackageConfig();
        // 添加这个后 会以一个实体为一个模块 比如user实体会生成user模块 每个模块下都会生成三层
//        pkConfig.setModuleName(scanner("模块名（系统端建议system，客户端建议client）"));
        //  父包名。如果为空，将下面子包名必须写写完整的包路径。如果不为空，只需写子包名。
        pkConfig.setParent(parentPack)
                //  子包名
                .setEntity(moduleName + ".entity")                // 实体类 Entity包名
                .setXml("mapper")                                 // 持久层 Mapper.xml 包名
                .setMapper(moduleName + ".mapper")                // 持久层 Mapper.java 包名
                .setService(moduleName + ".service")              // 服务层 Service.java 包名
                .setServiceImpl(moduleName + ".service.impl")     // 服务层 ServiceImpl.java 包名
                .setController("admin.controller." + moduleName); // 控制层 Controller.java 包名

        //设置自定义输出目录（分布式项目使用）
        Map<String, String> pathInfo = new HashMap<>();

        pathInfo.put(ConstVal.ENTITY_PATH, entityPath);
        pathInfo.put(ConstVal.MAPPER_PATH, mapperPath);
        // pathInfo.put(ConstVal.XML_PATH, mapperXmlPath);
        pathInfo.put(ConstVal.SERVICE_PATH, servicePath);
        pathInfo.put(ConstVal.SERVICE_IMPL_PATH, serviceImplPath);
        pathInfo.put(ConstVal.CONTROLLER_PATH, controllerPath);

        pkConfig.setPathInfo(pathInfo);

        // 把包配置添加到代码生成器主类
        mpg.setPackageInfo(pkConfig);

        // 4. 自定义配置【这是MyBatisPlus 3.x 新增的】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名称，如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return mapperXmlPath + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 5. 策略配置(数据库表配置)
        StrategyConfig strategyConfig = new StrategyConfig();

        strategyConfig
                .setNaming(NamingStrategy.underline_to_camel)           // 数据库表映射到实体的命名策略:下划线转驼峰
                .setColumnNaming(NamingStrategy.underline_to_camel)     // 数据库表字段映射到实体的命名策略:下划线转驼峰
                .setEntityLombokModel(true)                             // 实体是否为lombok模型（默认 false）
                // .setSuperEntityClass("com.zxdmy.wx.mp.wechat.common.BaseEntity")                 // 设置实体类的父类（如果启用，实体类会增加BaseEntity的继承）
                .setSuperControllerClass("com.zxdmy.excite.common.base.BaseController")         // 设置控制类的父类（如果启用，控制类会增加BaseController的继承）
                // 提示输入生成的表名
                .setInclude(tableName)
                // .setExclude("***")                                   // 需要排除的表名，允许正则表达式
                // .setSuperEntityColumns("id");                        // 实体类主键名称设置（如果启用，生成的实体类没有此字段）
                // .setTablePrefix(pc.getModuleName() + "_")            // 表前缀符（如果启用，生成的所有文件名没有此前缀）
                .setControllerMappingHyphenStyle(true);                 // 驼峰转连字符

        // 把数据库配置添加到代码生成器主类
        mpg.setStrategy(strategyConfig);

        // 在代码生成器主类上配置模板引擎,选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        // 开始执行
        try {
            mpg.execute();
        } catch (Exception e) {
            return error(e.getMessage());
        }
        return success("表" + tableName + "的代码生成成功！");
    }

}
