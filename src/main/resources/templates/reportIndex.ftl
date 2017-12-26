<#import "template.ftl" as layout />
<@layout.mainLayout; section>
    <#if section == "content">
        <#include "report/content.ftl"/>
    </#if>
    <#if section == "header">
        <#include "report/header.ftl"/>
    </#if>
</@layout.mainLayout>
