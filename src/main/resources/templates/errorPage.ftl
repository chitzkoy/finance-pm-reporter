<#import "template.ftl" as layout />
<@layout.mainLayout; section>
    <#if section == "content">
        <div class="alert alert-danger" role="alert">
            Что-то пошло не так =/
        </div>
    </#if>
</@layout.mainLayout>