package com.smallbun.crew.plus.core.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.io.FileUtil;
import com.smallbun.crew.plus.core.enums.ElementTagEnum;
import com.smallbun.crew.plus.core.enums.InfoElementTagEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * 处理Word模板
 *
 * @author lvchao
 * @date 2021-11-16
 */
@Slf4j
public class WordGenerateTemplateProcess implements GenerateTemplateProcess {
    @Override
    public String generateTemplate(String filePath) throws Exception {
        String parentPath = FileUtil.getParent(filePath, 1);
        String fileName = FileUtil.getPrefix(filePath);
        String ftlFilePath = parentPath + File.separator + fileName + ".ftl";
        File ftlFile = FileUtil.newFile(ftlFilePath);
        String ftlXmlStr = this.parseXml(filePath);
        FileUtil.writeUtf8String(ftlXmlStr, ftlFile);
        return ftlFilePath;
    }

    /**
     * 解析xml
     *
     * @param path 文件路径
     * @throws Exception
     */
    public String parseXml(String path) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(path);
        //解析遍历表格，行标签
        Document newDocument = parseEachElement(document);
        log.info("解析遍历标签：{}", newDocument.asXML());
        //解析字段内容标签
        String xmlStr = parseColumnElement(newDocument);
        log.info("解析ftl内容：{}", xmlStr);
        return xmlStr;
    }

    /**
     * 解析列节点
     *
     * @param document 文档
     * @return 解析后的字符串
     */
    private String parseColumnElement(Document document) {
        List<InfoElementTagEnum> cacheElement = new ArrayList<>(InfoElementTagEnum.values().length);
        document.getRootElement().addNamespace("w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main");
        addTagByText(document, cacheElement);
        log.info("进准文本替换后：{}", document.asXML());
        addInfoTag(document, cacheElement);
        log.info("范围文本替换后：{}", document.asXML());
        document.getRootElement().remove(
            new Namespace("w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main"));
        return replaceTag2FtlTag(document.asXML(), cacheElement);
    }

    public Document parseEachElement(Document document) throws Exception {
        document.getRootElement().addNamespace("w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main");
        addTableTag(document);
        addColumnTag(document);
        document.getRootElement().remove(
            new Namespace("w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main"));
        return document;
    }

    public String replaceTag2FtlTag(String xml, List<InfoElementTagEnum> cacheElement) {
        for (ElementTagEnum tagEnum : ElementTagEnum.values()) {
            xml = xml.replace(tagEnum.getText(), tagEnum.getFtlTagStr());
        }
        for (InfoElementTagEnum tagEnum : InfoElementTagEnum.values()) {
            String ftlStr = tagEnum.getFtlTagStr();
            if (!cacheElement.contains(tagEnum)) {
                ftlStr = getFtlText(tagEnum.getFtlTagStr());
            }
            xml = xml.replace(tagEnum.getText(), ftlStr);
        }
        return xml;
    }

    public String getFtlText(String ftlTag) {

        String styleText =
            "<w:p w14:paraId=\"60A311BD\" w14:textId=\"77777777\" w:rsidR=\"005153C0\" w:rsidRPr=\"006012B2\"\n"
                + "                                 w:rsidRDefault=\"005153C0\">\n"
                + "                                <w:pPr>\n"
                + "                                    <w:jc w:val=\"center\"/>\n"
                + "                                    <w:rPr>\n"
                + "                                        <w:rFonts w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:cs=\"宋体\"/>\n"
                + "                                        <w:sz w:val=\"18\"/>\n"
                + "                                        <w:szCs w:val=\"18\"/>\n"
                + "                                    </w:rPr>\n"
                + "                                </w:pPr>\n"
                + "                                <w:r w:rsidRPr=\"006012B2\">\n"
                + "                                    <w:rPr>\n"
                + "                                        <w:rFonts w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:cs=\"宋体\" "
                + "w:hint=\"eastAsia\"/>\n"
                + "                                        <w:sz w:val=\"18\"/>\n"
                + "                                        <w:szCs w:val=\"18\"/>\n"
                + "                                    </w:rPr>\n"
                + "                                    <w:t>%s</w:t>\n"
                + "                                </w:r>\n"
                + "                            </w:p>";

        return String.format(styleText, ftlTag);
    }

    /**
     * 添加表遍历标签
     *
     * @param document xml对象
     */
    private void addTableTag(Document document) {
        Element pNode = (Element)document.selectNodes("//w:body").get(0);
        Element start = createElement(ElementTagEnum.TABLE_START_ELEMENT_TAG.getCode());
        Element stop = createElement(ElementTagEnum.TABLE_STOP_ELEMENT_TAG.getCode());
        pNode.elements().add(0, start);
        pNode.elements().add(stop);

    }

    /**
     * 添加字段遍历标签
     *
     * @param document xml对象
     * @throws Exception
     */
    private void addColumnTag(Document document) throws Exception {
        Element pNode = (Element)document.selectNodes("//w:tbl").get(0);
        List<Element> tmpList = new ArrayList<>(pNode.elements());
        int columnStartIndex = -1;
        int columnStopIndex = -1;
        for (int i = 0; i < tmpList.size(); i++) {
            Element e = tmpList.get(i);
            if (ElementTagEnum.COLUMN_START_ELEMENT_TAG.getCode().equals(e.getStringValue())) {
                columnStartIndex = i;
            } else if (ElementTagEnum.COLUMN_STOP_ELEMENT_TAG.getCode().equals(e.getStringValue())) {
                columnStopIndex = i;
            }

        }
        if (columnStartIndex == -1 || columnStopIndex == -1) {
            throw new Exception("遍历字段标签不完整");
        }
        Element start = createElement(ElementTagEnum.COLUMN_START_ELEMENT_TAG.getCode());
        Element stop = createElement(ElementTagEnum.COLUMN_STOP_ELEMENT_TAG.getCode());
        pNode.elements().set(columnStartIndex, start);
        pNode.elements().set(columnStopIndex, stop);

    }

    /**
     * 添加字段信息标签
     *
     * @param document 文档对象
     */
    private void addInfoTag(Document document, List<InfoElementTagEnum> cacheElement) {
        List<Element> pNode = (List)document.selectNodes("//w:p");
        for (Element node : pNode) {
            if (StringUtils.isNotEmpty(node.getStringValue())) {
                for (InfoElementTagEnum tagEnum : InfoElementTagEnum.values()) {
                    if (cacheElement.contains(tagEnum)) {
                        continue;
                    }
                    if (node.getStringValue().toLowerCase().contains(tagEnum.getCode().toLowerCase())) {
                        List<Node> elepar = node.getParent().content();
                        elepar.set(elepar.indexOf(node), createElement(tagEnum.getCode()));
                    }
                }
            }
        }
    }


    /**
     * 添加字段信息标签
     *
     * @param document 文档对象
     */
    private void addTagByText(Document document, List<InfoElementTagEnum> cacheElement) {
        List<Element> pNodes = (List)document.selectNodes("//w:p");
        StringBuffer text = new StringBuffer();
        List<Element> textNodes = new ArrayList<>();
        for (Element pNode : pNodes) {
            List<Element> eNodes = (List)pNode.selectNodes("child::*/child::w:t");
            for (Element eNode : eNodes) {
                text.append(eNode.getText());
                if (StringUtils.isNotEmpty(text)) {
                    for (InfoElementTagEnum tagEnum : InfoElementTagEnum.values()) {
                        if (text.toString().toLowerCase().contains(tagEnum.getCode().toLowerCase())) {
                            for (Element textNode : textNodes) {
                                textNode.getParent().remove(textNode);
                            }
                            eNode.setText("");
                            eNode.add(createElement(tagEnum.getCode()));
                            cacheElement.add(tagEnum);
                            text = new StringBuffer();
                            textNodes.clear();
                        }
                    }
                }
                if (text.length() > 0) {
                    textNodes.add(eNode);
                }
            }
            text = new StringBuffer();
            textNodes.clear();

        }
    }

    /**
     * 创建
     *
     * @param name 命名空间
     * @return 节点
     */
    private Element createElement(String name) {
        return DocumentHelper.createElement("w:" + name);
    }

}
 