package com.smallbun.crew.plus.core.engine;

import cn.hutool.core.io.FileUtil;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import com.smallbun.crew.plus.core.process.WordGenerateTemplateProcess;
import lombok.Builder;
import lombok.Data;

/**
 * @author l2503
 * @date 2021-11-16
 */
@Data
@Builder
public class IEngineConfig {

    public static EngineConfig getCustomTemplateConfig(String templateUrl) throws Exception {
        String parentPath = FileUtil.getParent(templateUrl, 1);
        return getCustomTemplateConfig(templateUrl, parentPath);
    }

    public static EngineConfig getCustomTemplateConfig(String templateUrl, String fileOutputDir) throws Exception {
        //根据word内容生成模板
        WordGenerateTemplateProcess process = new WordGenerateTemplateProcess();
        String ftlPath = process.generateTemplate(templateUrl);
        // 生成文件配置
        return EngineConfig.builder()
            // 生成文件路径，自己mac本地的地址，这里需要自己更换下路径
            .fileOutputDir(fileOutputDir)
            // 打开目录
            .openOutputDir(false)
            // 文件类型
            .fileType(EngineFileType.WORD)
            // 生成模板实现
            .produceType(EngineTemplateType.freemarker)
            .customTemplate(ftlPath).build();
    }

}
 