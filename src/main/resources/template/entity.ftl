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
import lombok.Data;

/**
<#if tableRemark != "">
 * ${tableRemark}
</#if>
 * @author robot
 */
@Data
public class ${tableName}${tableNamePost} {

<#list fieldList! as field>
    <#if field.fieldRemark == "">
    <#else >
    /**
     * ${field.fieldRemark}
     */
    </#if>
    private ${field.fieldType} ${field.fieldName};
</#list>

<#list innerEnumList! as inner>
/**
<#if inner.enumRemark != "">
 * ${inner.enumRemark}
</#if>
 * @author robot
 */
@Getter
@AllArgsConstructor
public enum ${inner.enumType} {

<#list inner.enumList! as inner.enumMeta>
    <#if inner.enumMeta.remark != "">
    /**
     * ${inner.enumMeta.remark}
     */
    </#if>
    ${inner.enumMeta.enumName}("${inner.enumMeta.enumName}"),
</#list>;

    private String value;
}
</#list>
}
