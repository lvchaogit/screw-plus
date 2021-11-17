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
 * 数据表、字段信息枚举
 *
 * @author lvchao
 * @date 2021-11-13
 */
@Getter
@AllArgsConstructor
public enum InfoElementTagEnum {
    /**
     * 数据表名称
     */
    TABLE_NAME_ELEMENT_TAG("tbName", "<w:tbName/>", "${t.tableName!''}"),

    /**
     * 数据表注释
     */
    TABLE_REMARKS_ELEMENT_TAG("tbRemarks", "<w:tbRemarks/>", "${t.remarks}"),

    /**
     * 字段序号标签
     */
    COLUMN_INDEX_ELEMENT_TAG("colIndex", "<w:colIndex/>", "${c?index+1}"),
    /**
     * 字段名称标签
     */
    COLUMN_NAME_ELEMENT_TAG("colName", "<w:colName/>", "${c.columnName!''}"),
    /**
     * 字段类型标签
     */
    COLUMN_TYPE_ELEMENT_TAG("colType", "<w:colType/>", "${c.typeName!''}"),
    /**
     * 字段长度标签
     */
    COLUMN_SIZE_ELEMENT_TAG("colSize", "<w:colSize/>", "${c.columnSize!''}"),
    /**
     * 字段小数位标签
     */
    COLUMN_DECIMAL_ELEMENT_TAG("colDecimal", "<w:colDecimal/>", "${c.decimalDigits!'0'}"),
    /**
     * 字段是否为空
     */
    COLUMN_NULL_FLAG_ELEMENT_TAG("colNullable", "<w:colNullable/>", "${c.nullable!''}"),

    /**
     * 字段是否主键
     */
    COLUMN_PRIMARY_FLAG_ELEMENT_TAG("colPrimary", "<w:colPrimary/>", "${c.primaryKey!''}"),
    /**
     * 字段默认值
     */
    COLUMN_DEF_FLAG_ELEMENT_TAG("colDef", "<w:colDef/>", "${c.columnDef!''}"),
    /**
     * 字段注释
     */
    COLUMN_REMARK_FLAG_ELEMENT_TAG("colRemark", "<w:colRemark/>", "${c.remarks!''}");

    private String code;
    private String text;
    /**
     * FTL标签
     */
    private String ftlTagStr;
}
