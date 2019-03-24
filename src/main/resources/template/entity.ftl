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
}
