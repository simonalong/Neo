package ${entityPath};

import lombok.Data;

/**
 * <#if tableRemark == ""><#else >${tableRemark}</#if>
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
