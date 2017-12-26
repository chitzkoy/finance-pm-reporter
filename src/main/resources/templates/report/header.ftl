<script>
    function toggleCategory(button) {
        var toggleSubtree = function(element, toggleSourceId) {
            var categoryId = $(element).attr("data-category-id");
            var toggledBy = $(element).attr("data-collapsed-by");

            if (!toggledBy) {
                $(element).attr("data-collapsed-by", toggleSourceId);
                $(element).toggleClass("hidden");
            } else {
                if (toggledBy === toggleSourceId) {
                    $(element).removeAttr("data-collapsed-by");
                    $(element).toggleClass("hidden");
                } else {
                    return
                }
            }

            $("tr[data-parent-category-id=\"" + categoryId + "\"]").each(function() {
                toggleSubtree(this, toggleSourceId);
            });
        };

        $(button).children().toggleClass("fa-minus-square-o");
        $(button).children().toggleClass("fa-plus-square-o");

        var categoryId = $(button).parents("tr")[0].attributes["data-category-id"].value;
        $("tr[data-parent-category-id=\"" + categoryId + "\"]").each(function () {
            toggleSubtree(this, categoryId);
        });
    }

    //todo добавить слушателя состояний всех кнопок для динамического изменения состояния
    function toggleTopLevelCategories(button) {
        var isCollapsingAction = $(button).children().hasClass("fa-minus-square-o");
        $(".level-0>button").filter(function () {
            return isCollapsingAction ?
                    $(this).children().hasClass("fa-minus-square-o") :
                    $(this).children().hasClass("fa-plus-square-o");
        }).click();
        $(button).children().toggleClass("fa-minus-square-o");
        $(button).children().toggleClass("fa-plus-square-o");
    }

</script>
<style>
    table {
        font-size: .85em;
    }
    .category-name.level-0 {
        padding-left: 5px;
    }
    .category-name.level-1 {
        padding-left: 27px;
    }
    .category-name.level-2 {
        padding-left: 51px;
    }
    .category-name.level-3 {
        padding-left: 75px;
    }
    button.toggler {
        position: relative;
        top: -2px;
        border: none;
        padding: 0 2px;
        line-height: 10px;
    }
    th button.toggler {
        left: -3px;
    }
    button.toggler i {
        margin: 0;
        padding: 1px 0 0;
    }
    .predictable,
    .zero {
        opacity: 0.5;
    }
    .current-month {
        background-image: linear-gradient(to bottom, rgba(126, 138, 142, 0.3), rgba(126, 138, 142, 0.3));
        background-attachment: fixed;
        background-repeat: no-repeat;
    }

    .table.report>thead>tr>th {
        vertical-align: middle;
    }

    .report .table>thead>tr>th {
        vertical-align: middle;
    }

    .report .panel-heading {
        display: flex;
        flex-flow: row;
        flex-direction: row;
        flex-wrap: nowrap;
        justify-content: space-between;
        align-content: flex-end;
    }

    .report nav {
        display: flex;
    }

    .report .pagination {
        margin: 0;
    }

    .report .summary {
        font-style: italic;
    }

    .report .summary.income {
        color: #40a030
    }

    .report .summary.expenses {
        color: #fa7d77;
    }

    .report .summary.balance {
        color: #337ab7;
        border-bottom: 3px solid #ddd;
    }
</style>