<#import "template.ftl" as layout />
<@layout.mainLayout; section>
    <#if section == "content">
        <#include "upload/content.ftl"/>
    </#if>
    <#if section == "header">
        <#include "upload/header.ftl"/>
    </#if>
</@layout.mainLayout>