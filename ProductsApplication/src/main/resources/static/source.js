import { addNewProductWithIngredients, getAllProducts, getSpecifiedProduct, updateProduct,
    deleteProduct, getAllIngredients, getSpecifiedIngredient, postIngredient, updateIngredient,
    deleteIngredient } from './crud.js';

const ADMIN_CONFIG = {
    serverAddress: "http://localhost:4001/"
};

const addEventListenersToNavigationButtons = () => {
    const insertButtonObj = {
        element: document.getElementById('insert-button'),
        url: "./index.html"
    };
    addRedirectEventListenersToInputObject(insertButtonObj);

    const reportsButtonObj = {
        element: document.getElementById('reports-button'),
        url: "./reports.html"
    };
    addRedirectEventListenersToInputObject(reportsButtonObj);

    const reportBugButtonObj = {
        element: document.getElementById('report-bug-button'),
        url: "mailto:example@example.com" /* This does not work && Replace with API */
    };
    addRedirectEventListenersToInputObject(reportBugButtonObj);
};

const addRedirectEventListenersToInputObject = (buttonObj) => {
    buttonObj.element.addEventListener('click',() => {
        const targetUrl = buttonObj.url;
        window.location.href = targetUrl;
    });
};

const addEventListenersToReportButtons = () => {
    // ==> PRODUCTS
    // GET
    document.getElementById('products-get-all-button').addEventListener('click', () => getAllProducts(ADMIN_CONFIG.serverAddress));
    document.getElementById('products-get-specific-button').addEventListener('click', () => getSpecifiedProduct(ADMIN_CONFIG.serverAddress));

    // PUT
    document.getElementById('products-put-button').addEventListener('click', () => updateProduct(ADMIN_CONFIG.serverAddress));

    // DELETE
    document.getElementById('products-delete-button').addEventListener('click', () => deleteProduct(ADMIN_CONFIG.serverAddress));

    // ==> INGREDIENTS
    // GET
    document.getElementById('ingredients-get-all-button').addEventListener('click', () => getAllIngredients(ADMIN_CONFIG.serverAddress));
    document.getElementById('ingredients-get-specific-button').addEventListener('click', () => getSpecifiedIngredient(ADMIN_CONFIG.serverAddress));

    // POST
    document.getElementById('ingredients-post-button').addEventListener('click', () => postIngredient(ADMIN_CONFIG.serverAddress));

    // PUT
    document.getElementById('ingredients-put-button').addEventListener('click', () => updateIngredient(ADMIN_CONFIG.serverAddress));

    // DELETE
    document.getElementById('ingredients-delete-button').addEventListener('click', () => deleteIngredient(ADMIN_CONFIG.serverAddress));
};

const populateListbox = () => {
    const listbox = document.getElementById("listbox-ingredients");

    for (let i = 1; i <= 10; i++) {
        const option = document.createElement("option");
        option.text = i;
        option.value = i;
        listbox.add(option);
    }
};

const generateIngredientCode = () => {
    const selectedValue = document.getElementById("listbox-ingredients").value;
    const generateIngredientSection = document.getElementById("generate-ingredient-section");
    generateIngredientSection.innerHTML = "";

    for (let i = 0; i < selectedValue; i++) {
        generateIngredientSection.innerHTML += ingredientTemplate(i);
    }
};

const ingredientTemplate = (index) => {
    index++;
    return `<hr/><br/>
        <label for="input-ingredient-name-${index}">Ingredient ${index} name:</label>
        <input class="input-name generated-input-name" type="text" name="input-ingredient-name-${index}" id="input-ingredient-name-${index}"/>
        <br/><br/>
        <label for="input-ingredient-quantity-${index}">Quantity:</label>
        <input class="input-quantity generated-input-quantity" type="text" name="input-ingredient-quantity-${index}" id="input-ingredient-quantity-${index}"/>
        <label for="input-ingredient-price-${index}">Price:</label>
        <input class="input-price generated-input-price" type="text" name="input-ingredient-price-${index}" id="input-ingredient-price-${index}"/>
        <br/><br/>`;
};

document.addEventListener("DOMContentLoaded",() => {
    const indexPageURL = "index.html";
    const reportsPageURL = "reports.html";
    const currentURL = window.location.href;

    // Execute on startup
    addEventListenersToNavigationButtons();
    if (currentURL.endsWith(indexPageURL)) {
        populateListbox();
        document.getElementById("listbox-ingredients").addEventListener("change", generateIngredientCode);
        document.getElementById('submit-product-button').addEventListener('click', () => addNewProductWithIngredients(ADMIN_CONFIG.serverAddress));
    } else if (currentURL.endsWith(reportsPageURL)) {
        addEventListenersToReportButtons();
    }
});
