<#include "macros.ftl"/>



<#if report.months?size == 0>
    <div class="report panel panel-warning">
        <div class="panel-heading">
            <h3 class="panel-title">Не найдено</h3>
        </div>
        <div class="panel-body">
            Отсуствует информация по заданному периоду
        </div>
    </div>
<#else>
<div class="report panel panel-primary">
    <div class="panel-heading">
        <@pagination report/>
        <@currencySelector report/>

    </div>
    <div class="panel-body">
        <table class="table table-condensed table-hover table-bordered table-responsive">
            <colgroup>
                <col width="200px">
                <col span="${report.months?size}" width="80px">
                <col span="${report.months?size}" width="100px">
            </colgroup>
            <thead>
                <tr>
                    <th scope="col">
                        <button type="button"
                                title="Свернуть все категории"
                                class="toggler btn btn-primary"
                                onclick="toggleTopLevelCategories(this)">
                            <i class="fa fa-minus-square-o" aria-hidden="true"></i>
                        </button>
                        Категория
                    </th>
                    <#list report.months as month>
                        <th scope="col" class="text-center ${report.isCurrentMonth(month)?then("current-month","")}">
                        ${report.localizeMonth(month, .locale)}
                        </th>
                    </#list>
                    <th scope="col" class="text-center" >
                        AVG<sub>${report.getAvgMonths()} месяцев</sub>
                    </th>
                </tr>
            </thead>
            <tbody>

                <#-- summary start  -->
                <tr class="summary info income">
                    <td>
                        Доходы за месяц
                    </td>
                    <#list report.months as month>
                        <td class="${report.isCurrentMonth(month)?then("current-month","")}">
                            <span class="pull-right">
                                ${report.income(month)}
                            </span>
                        </td>
                    </#list>
                    <td>
                        <span class="pull-right">
                            ${report.avgIncome()}
                        </span>
                    </td>
                </tr>
                <tr class="summary info text-danger">
                    <td>
                        Расходы за месяц
                    </td>
                    <#list report.months as month>
                        <td class="${report.isCurrentMonth(month)?then("current-month","")}">
                            <span class="pull-right">
                                ${report.expenses(month)}
                            </span>
                        </td>
                    </#list>
                    <td>
                        <span class="pull-right">
                            ${report.avgExpenses()}
                        </span>
                    </td>
                </tr>
                <tr class="summary info balance">
                    <td>
                        Баланс на конец месяца
                    </td>
                    <#list report.months as month>
                        <td class="${report.isCurrentMonth(month)?then("current-month","")}">
                            <span class="pull-right">
                                ${report.balance(month)}
                            </span>
                        </td>
                    </#list>
                    <td>
                        <span class="pull-right">
                            -
                        </span>
                    </td>
                </tr>
                <#-- summary end -->

                <#list report.categoryInfoList as categoryInfo>
                    <@printCategoryFull categoryInfo 0 />
                </#list>
            </tbody>
        </table>
    </div>

</#if>