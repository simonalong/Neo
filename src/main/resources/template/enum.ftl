package ${entityPath};

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
<#if enumRemark != "">
 * ${enumRemark}
</#if>
 * @author robot
 */
@Getter
@AllArgsConstructor
public enum ${enumType} {

<#list enumList! as enumMeta>
    <#if enumMeta.remark != "">
    /**
     * ${enumMeta.remark}
     */
    </#if>
    ${enumMeta.enumName}("${enumMeta.enumName}"),
</#list>;

    private String value;
}