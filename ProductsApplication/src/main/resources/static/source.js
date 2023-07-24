

const addEventListenersToButtons = () => {
    const insertButtonObj = {
        element: document.getElementById('insert-button'),
        url: "./index.html"
    };
    addRedirectListenersToButton(insertButtonObj);

    const reportsButtonObj = {
        element: document.getElementById('reports-button'),
        url: "./reports.html"
    };
    addRedirectListenersToButton(reportsButtonObj);

    const reportBugButtonObj = {
        element: document.getElementById('report-bug-button'),
        url: "mailto:example@example.com" /* This does not work && Replace with API */
    };
    addRedirectListenersToButton(reportBugButtonObj);
};

const addRedirectListenersToButton = (obj) => {
    obj.element.addEventListener('click',() => {
        const targetUrl = obj.url;
        window.location.href = targetUrl;
    });
};

// Execute on startup
addEventListenersToButtons();
