package ${appPath}.entity;

import lombok.Data;

/**
 * @author robot
 */
@Data
public class ${tableName}Entity {

    <#list fieldList! as field>
    private ${field.class} ${field.name};
    </#list>
}
