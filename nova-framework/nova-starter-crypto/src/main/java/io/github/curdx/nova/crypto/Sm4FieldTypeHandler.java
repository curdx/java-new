package io.github.curdx.nova.crypto;

import cn.hutool.crypto.symmetric.SM4;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 敏感字段 SM4 加密存储。实体字段标注：
 * {@code @Column(typeHandler = Sm4FieldTypeHandler.class)}
 *
 * <p>TypeHandler 由 MyBatis 实例化（非 Spring Bean），密钥经
 * {@link NovaCryptoAutoConfiguration} 静态注入。</p>
 */
public class Sm4FieldTypeHandler extends BaseTypeHandler<String> {

    private static volatile SM4 sm4;

    static void init(SM4 cipher) {
        sm4 = cipher;
    }

    private static SM4 cipher() {
        SM4 current = sm4;
        if (current == null) {
            throw new IllegalStateException("国密 SM4 未初始化：请配置 nova.crypto.sm4-key");
        }
        return current;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, cipher().encryptHex(parameter, StandardCharsets.UTF_8));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return decrypt(rs.getString(columnName));
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return decrypt(rs.getString(columnIndex));
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return decrypt(cs.getString(columnIndex));
    }

    private String decrypt(String cipherHex) {
        if (cipherHex == null || cipherHex.isBlank()) {
            return cipherHex;
        }
        return cipher().decryptStr(cipherHex, StandardCharsets.UTF_8);
    }
}
