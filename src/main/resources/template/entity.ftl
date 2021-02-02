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
import com.simonalong.neo.annotation.Table;
import lombok.experimental.Accessors;
import lombok.Data;

/**
<#if tableRemark ??>
 * ${tableRemark}
</#if>
 * @author robot
 */
@Data
@Table("${tableName}")
@Accessors(chain = true)
public class ${TableName}${tableNamePost} {

<#list fieldList! as field>
    <#if field.fieldRemark??>

    /**
     * ${field.fieldRemark}
     */
    </#if>
    @Column("${field.columnName}")
    private ${field.fieldType} ${field.fieldName};
</#list>
}
