<#macro printCategoryFull categoryInfo level >
<#assign category=categoryInfo.category/>
<tr class="${(category.type.id == 1)?then("success", "")}"
    <#if category.parent??>
    data-parent-category-id="${category.parent.id}"
    </#if>
    data-category-id="${category.id}">
    <th scope="row"
        class="category-name level-${level}">
        <#if (categoryInfo.childCategories?size > 0)>
            <button type="button" class="toggler btn btn-primary" onclick="toggleCategory(this)">
                <i class="fa fa-minus-square-o" aria-hidden="true"></i>
            </button>
        </#if>
    ${category.name}
    </th>
    <#list categoryInfo.totalsPerMonth as month, total>
        <td class="${report.isCurrentMonth(month)?then("current-month","")}">
                <span class="pull-right ${(total == 0)?then("zero", "")}" >
                ${total}
                </span>
        </td>
    </#list>
    <td>
        <span class="pull-right font-weight-bold text-danger">
        ${categoryInfo.avg!}
        </span>
    </td>
</tr>
    <#list categoryInfo.childCategories as childCategoryInfo>
        <@printCategoryFull childCategoryInfo level+1 />
    </#list>
</#macro>


<#macro pagination report>
<nav aria-label="Page navigation">
    <ul class="pagination">
        <li class="${report.getAvailableYears()?seq_contains(report.year - 1)?string("", "disabled")}">
            <a href="${report.getAvailableYears()?seq_contains(report.year - 1)?string("/${report.currency}/${report.currentYear(-1)}", "#")}" aria-label="Previous"
            <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
        <#list report.getPaginatorYears() as year>
            <li class="${(report.year == year)?string("active","")}">
                <a href="/${report.currency}/${report.currentYear(year - report.year)}">${report.currentYear(year - report.year)}</a>
            </li>
        </#list>

        <li class="${report.getAvailableYears()?seq_contains(report.year + 1)?string("", "disabled")}">
            <a href="${report.getAvailableYears()?seq_contains(report.year + 1)?string("/${report.currency}/${report.currentYear(1)}", "#")}" aria-label="Next"
            <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    </ul>
</nav>
</#macro>


<#macro currencySelector report>
<div class="dropdown pull-right">
    <button class="btn btn-info dropdown-toggle" type="button" id="currencyDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
        ${report.currency}
        <span class="caret"></span>
    </button>
    <#list report.availableCurrencies()>
    <ul class="dropdown-menu" aria-labelledby="currencyDropdown">
        <#items as cur>
            <li class="${(report.currency == cur)?string("disabled","")}">
                <a href="/${cur}/${report.currentYear(0)}">${cur}</a>
            </li>
        </#items>
    </ul>
    </#list>
</div>
</#macro>