package ${entityPath};

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
<#if enumRemark??>
 * ${enumRemark}
</#if>
 * @author robot
 */
@Getter
@AllArgsConstructor
public enum ${enumType} {

<#list enumList! as enumMeta>
    <#if enumMeta.desc??>
    /**
     * ${enumMeta.desc}
     */
    </#if>
    ${enumMeta.enumData}("${enumMeta.enumData}"),
</#list>;

    private String value;
}