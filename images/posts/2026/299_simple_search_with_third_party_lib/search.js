$(document).ready(function () {
    let gridInstance = null;
    // Detect base-URL
    const currentHref = window.location.href;
    const baseUrl = currentHref.split("/html")[0];

    //START Helper functions
    function valueOrEmpty(val) {
        const v = (val || "").trim();
        return v === "" ? "@@EMPTY@@" : v;
    }

    function highlightText(text, term) {
        if (!term || term === "@@EMPTY@@") {
            return text || "";
        }

        const safeTerm = term.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
        const regex = new RegExp("(" + safeTerm + ")", "gi");

        return (text || "").replace(regex, '<span class="hl-search">$1</span>');
    }

    function buildRowsFromResponse(data, filters) {
        const rows = [];

        $.each(data, function (key, project) {
            if (key.includes("project") && typeof project === "object") {
                const rawItemId = project['project-id'].ItemId || "";
                const rawId = project['project-id'].Id || "";
                const rawName = project.prj_name || "";
                const rawSubject = project.prj_subject || "";
                const rawDescription = project.prj_description || "";
                const rawType = project.prj_type || "";
                const rawStartDate = project.prj_start_date || "";

                const name = highlightText(rawName, filters.name);
                const subject = highlightText(rawSubject, filters.subject);
                const description = highlightText(rawDescription, filters.description);
                const type = highlightText(rawType, filters.type);

                let clickableId = rawId;
                if (baseUrl && rawItemId) {
                    const detailUrl = baseUrl + "/app/start/web/item/" + rawItemId;
                    clickableId = `<a href="${detailUrl}" target="_blank">${rawId}</a>`;
                }

                rows.push([
                    clickableId,
                    name,
                    subject,
                    description,
                    type,
                    rawStartDate
                ]);
            }
        });

        return rows;
    }
    //END Helper functions

    // Render grid with filters
    function renderGrid({ targetElementId, filters }) {
        const gridElement = document.getElementById(targetElementId);

        // 1) Clean up the old grid
        if (gridInstance) {
            try {
                gridInstance.destroy();
            } catch (e) {
                console.warn("Can't destroy old grid:", e);
            }
            gridInstance = null;
        }

        // 2) Empty the container
        gridElement.innerHTML = "";

        // 3) Recall service
        $.cordys.ajax({
            method: "FindProjectsForSearch",
            namespace: "http://schemas/opa_tipsprj_generic/project/operations",
            parameters: {
                name: filters.name,
                subject: filters.subject,
                description: filters.description,
                type: filters.type
            },
            success: function (data) {
                console.log(JSON.stringify(data));
                let rows = buildRowsFromResponse(data, filters);
                gridInstance = new gridjs.Grid({
                    columns: [
                        { name: 'Id', formatter: (cell) => gridjs.html(cell) },
                        { name: 'Name', formatter: (cell) => gridjs.html(cell) },
                        { name: 'Subject', formatter: (cell) => gridjs.html(cell) },
                        { name: 'Description', formatter: (cell) => gridjs.html(cell) },
                        { name: 'Type', formatter: (cell) => gridjs.html(cell) },
                        { name: 'Start date', formatter: (cell) => gridjs.html(cell) }
                    ],
                    data: rows,
                    pagination: {
                        enabled: true,
                        limit: 15
                    },
                    search: false,
                    sort: true,
                    language: {
                        pagination: {
                            previous: "<",
                            next: ">"
                        }
                    }
                }).render(gridElement);
            },
            error: function (error) {
                console.error(error);
            }
        });
    }

    // Hit search → build filters → render grid
    $("#btn_search").on("click", function () {

        // If empty fields → "@@EMPTY@@"
        const filters = {
            name: valueOrEmpty($("#inp_name").val()),
            subject: valueOrEmpty($("#inp_subject").val()),
            description: valueOrEmpty($("#inp_description").val()),
            type: valueOrEmpty($("#inp_type").val())
        };

        renderGrid({
            targetElementId: "grid_search",
            filters: filters
        });
    });

    // Hit <Enter> to start search
    $(".inp_search").on("keypress", function (e) {
        if (e.key === "Enter") {
            $("#btn_search").click();
        }
    });
});