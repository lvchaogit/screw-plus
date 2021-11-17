/*
 * screw-plus-code - 简洁好用的数据库表结构文档生成工具
 * Copyright © 2021 SanLi (qinggang.zuo@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.smallbun.crew.plus.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据表，字段遍历标签
 *
 * @author lvchao
 */
@Getter
@AllArgsConstructor
public enum ElementTagEnum {

    /**
     * 数据表遍历开始标签
     */
    TABLE_START_ELEMENT_TAG("tbStart", "<w:tbStart/>", "<#list tables><#items as t>"),
    /**
     * 数据表遍历结束标签
     */
    TABLE_STOP_ELEMENT_TAG("tbStop", "<w:tbStop/>", "</#items></#list>"),

    /**
     * 字段遍历标签
     */
    COLUMN_START_ELEMENT_TAG("colStart", "<w:colStart/>", "<#list t.columns><#items as c>"),

    /**
     * 字段结束遍历标签
     */
    COLUMN_STOP_ELEMENT_TAG("colStop", "<w:colStop/>", "</#items></#list>");

    private String code;
    private String text;
    private String ftlTagStr;

}
