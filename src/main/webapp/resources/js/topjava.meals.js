const mealAjaxUrl = "profile/meals/";
let filterData = [];

const ctx = {
    ajaxUrl: mealAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

function filterMeal() {
    filterData = $('#filterForm').serialize();
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + "filter",
        data: filterData,
    }).done(function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
        successNoty("Filtered");
    });
}

function cleanFilter() {
    $('#filterForm').find(":input").val("");
    filterData = [];
    updateTable();
    successNoty("Clean filter");
}