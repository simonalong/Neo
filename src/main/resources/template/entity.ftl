package ${entityPath};

import lombok.Data;

/**
 * @author robot
 */
@Data
public class ${tableName}${tableNamePost} {

    <#list fieldList! as field>
    private ${field.fieldType} ${field.fieldName};
    </#list>
}
