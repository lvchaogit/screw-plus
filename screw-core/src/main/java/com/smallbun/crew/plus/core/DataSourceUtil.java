package com.smallbun.crew.plus.core;

import javax.sql.DataSource;

import cn.smallbun.screw.core.query.DatabaseType;
import cn.smallbun.screw.core.util.JdbcUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import static cn.smallbun.screw.core.constant.DefaultConstants.NAME;
import static cn.smallbun.screw.core.constant.DefaultConstants.ORACLE_REMARKS;
import static cn.smallbun.screw.core.constant.DefaultConstants.PHOENIX_NAMESPACE_MAPPING;
import static cn.smallbun.screw.core.constant.DefaultConstants.PHOENIX_SYS_NAMESPACE_MAPPING;
import static cn.smallbun.screw.core.constant.DefaultConstants.USE_INFORMATION_SCHEMA;

/**
 * 获取数据源
 *
 * @author lvchao
 * @date 2021-11-17
 */
public class DataSourceUtil {

    /**
     * 获取数据源
     *
     * @return {@link DataSource}
     */
    public static DataSource getDataSource(String driverClassName, String jdbcUrl, String userName, String passWord) {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName(NAME);
        //驱动名称
        hikariConfig.setDriverClassName(driverClassName.trim());
        //url
        hikariConfig.setJdbcUrl(jdbcUrl);
        //用户名
        hikariConfig.setUsername(userName);
        //密码
        hikariConfig.setPassword(passWord);
        //mysql oracle
        if (JdbcUtils.getDbType(jdbcUrl).equals(DatabaseType.MYSQL)
            || JdbcUtils.getDbType(jdbcUrl).equals(DatabaseType.MARIADB)) {
            hikariConfig.addDataSourceProperty(USE_INFORMATION_SCHEMA, "true");
        }
        //phoenix
        if (JdbcUtils.getDbType(jdbcUrl).equals(DatabaseType.PHOENIX)) {
            hikariConfig.addDataSourceProperty(PHOENIX_SYS_NAMESPACE_MAPPING, true);
            hikariConfig.addDataSourceProperty(PHOENIX_NAMESPACE_MAPPING, true);
        }
        //oracle
        if (JdbcUtils.getDbType(jdbcUrl).equals(DatabaseType.ORACLE)) {
            hikariConfig.addDataSourceProperty(ORACLE_REMARKS, "true");
        }
        return new HikariDataSource(hikariConfig);
    }
}
 