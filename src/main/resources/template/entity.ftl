package ${entityPath};

<#if importDate == 1>
import java.sql.Date;
</#if>
<#if importTime == 1>
import java.sql.Time;
</#if>
<#if importTimestamp == 1>
import java.sql.Timestamp;
</#if>
<#if importBigDecimal == 1>
import java.math.BigDecimal;
</#if>
<#if importBigInteger == 1>
import java.math.BigInteger;
</#if>
import com.simonalong.neo.annotation.Column;
import lombok.experimental.Accessors;
import lombok.Data;
<#if importInnerEnum == 1>
import lombok.AllArgsConstructor;
import lombok.Getter;
</#if>

/**
<#if tableRemark != "">
 * ${tableRemark}
</#if>
 * @author robot
 */
@Data
@Accessors(chain = true)
public class ${tableName}${tableNamePost} {

<#list fieldList! as field>
    <#if field.fieldRemark??>

    /**
     * ${field.fieldRemark}
     */
    </#if>
    @Column("${field.columnName}")
    private ${field.fieldType} ${field.fieldName};
</#list>
<#list innerEnumList! as inner>
    /**
<#if inner.enumRemark??>
    * ${inner.enumRemark}
</#if>
    */
    @Getter
    @AllArgsConstructor
    public enum ${inner.enumType} {
<#list inner.enumList! as enumMeta>
<#if enumMeta.desc??>
        /**
        * ${enumMeta.desc}
        */
</#if>
        ${enumMeta.enumData}("${enumMeta.enumData}"),
</#list>
        ;
        private String value;
    }
</#list>
}
