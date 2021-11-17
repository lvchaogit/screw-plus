package com.smallbun.crew.plus.core.process;

/**
 * 生成模板处理
 *
 * @author lvchao
 * @date 2021-11-16
 */
public interface GenerateTemplateProcess {

    /**
     * 生成模板
     *
     * @param filePath 文件路径
     * @return 生成好的模板文件路径
     */
    String generateTemplate(String filePath) throws Exception;
}
 