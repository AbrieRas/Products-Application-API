const getDateNow = () => {
    return new Date().toLocaleString();
};

class Product {
    constructor(name, category, quantity, price, ingredients) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.ingredients = ingredients;
    }
}

class Ingredient {
    constructor(name, quantity, price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}

const getStatusMessage = (responseStatus) => {
    switch (responseStatus) {
        case 201:
            return `${responseStatus} Successfully created<br/>Product with ingredient(-s) has successfully been created`;
        case 400:
            return `${responseStatus} Bad Request<br/>Please double check the field values and try again`;
        default:
            return `${responseStatus} Unknown error occurred<br/>Please double check the field values, internet connection and try again`;
    }
};

export const addNewProductWithIngredients = (serverAddress) => {
    // Get DOMs
    const message = document.getElementById('message');
    const productName = document.getElementById('input-product-name').value;
    const productCategory = document.getElementById('item-list-product-category').value;
    const productQuantity = document.getElementById('input-product-quantity').value;
    const productPrice = document.getElementById('input-product-price').value;
    const generatedIngredientNames = document.querySelectorAll('.generated-input-name');
    const generatedIngredientQuantities = document.querySelectorAll('.generated-input-quantity');
    const generatedIngredientPrices = document.querySelectorAll('.generated-input-price');

    // Filter over DOMs and store into object prototype
    const productIngredients = [];
    for (let i=0; i<generatedIngredientNames.length; i++) {
        productIngredients.push(
            new Ingredient(
                generatedIngredientNames[i].value,
                generatedIngredientQuantities[i].value,
                generatedIngredientPrices[i].value
            )
        );
    }
    const productObj = new Product(
        productName,
        productCategory,
        productQuantity,
        productPrice,
        productIngredients
    );

    // Push info to database
    fetch(`${serverAddress}products/add`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(productObj)
    })
      .then(response => {
        message.innerHTML = getStatusMessage(response.status);
        if (!response.ok) {
            message.innerHTML = getStatusMessage(response.status);
        }
        return response.json()
      })
      .catch(error => {
        console.error('Error:', error);
      });
};

export const getAllProducts = (serverAddress) => {
    const reportOutput = document.getElementById('report-output');

    fetch(`${serverAddress}products`)
        .then(response => response.json())
        .then(data => {
            const jsonData = JSON.stringify(data, null, 2);
            reportOutput.innerHTML = `<pre>${getDateNow()}<br/>${jsonData}</pre>`;
        })
        .catch(error => {
            console.error('Error:', error);
            reportOutput.innerHTML = "An unexpected error occurred.";
        });
};

export const getSpecifiedProduct = (serverAddress) => {
    const reportOutput = document.getElementById('report-output');
    const productId = document.getElementById('search-product-id').value;
    const productName = document.getElementById('search-product-name').value;
    const productQuantity = document.getElementById('search-product-quantity').value;
    const productPrice = document.getElementById('search-product-price').value;
    const productCategory = document.getElementById('search-product-category').value;
    const productProductReference = document.getElementById('search-product-product-reference').value;

    const params = {};

    if (productId) {
        params.id = productId;
    }
    if (productName) {
        params.name = productName;
    }
    if (productQuantity) {
        params.quantity = productQuantity;
    }
    if (productPrice) {
        params.price = productPrice;
    }
    if (productCategory) {
        params.category = productCategory;
    }
    if (productProductReference) {
        params.productReference = productProductReference;
    }

    const queryString = new URLSearchParams(params).toString();
    console.log(`${serverAddress}products/search?${queryString}`);

    fetch(`${serverAddress}products/search?${queryString}`)
        .then(response => response.json())
        .then(data => {
            const jsonData = JSON.stringify(data, null, 2);
            reportOutput.innerHTML = `<pre>${getDateNow()}<br/>${jsonData}</pre>`;
        })
        .catch(error => {
            console.error('Error:', error);
            reportOutput.innerHTML = "An unexpected error occurred.";
        });
};

export const updateProduct = (serverAddress) => {
    const reportOutput = document.getElementById('report-output');
    const productId = document.getElementById('update-product-id').value;
    const productName = document.getElementById('update-product-name').value;
    const productQuantity = document.getElementById('update-product-quantity').value;
    const productPrice = document.getElementById('update-product-price').value;
    const productCategory = document.getElementById('update-product-category').value;
    const productProductReference = document.getElementById('update-product-product-reference').value;

    const params = {};

    if (productName) {
        params.name = productName;
    }
    if (productQuantity) {
        params.quantity = productQuantity;
    }
    if (productPrice) {
        params.price = productPrice;
    }
    if (productCategory) {
        params.category = productCategory;
    }
    if (productProductReference) {
        params.productReference = productProductReference;
    }

    fetch(`${serverAddress}products/update/${productId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(params)
    })
        .then(response => response.json())
        .then(data => {
            const jsonData = JSON.stringify(data, null, 2);
            reportOutput.innerHTML = `<pre>${getDateNow()}<br/>${jsonData}</pre>`;
        })
        .catch(error => {
            console.error('Error:', error);
            reportOutput.innerHTML = "An unexpected error occurred.";
        });
};

export const deleteProduct = (serverAddress) => {
    const reportOutput = document.getElementById('report-output');
    const productId = document.getElementById('delete-product-id').value;

    fetch(`${serverAddress}products/remove/${productId}`, {
        method: 'DELETE'
    })
        .then(response => response.json())
        .then(data => {
            const jsonData = JSON.stringify(data, null, 2);
            reportOutput.innerHTML = `<pre>${getDateNow()}<br/>${jsonData}</pre>`;
        })
        .catch(error => {
            console.error('Error:', error);
            reportOutput.innerHTML = "An unexpected error occurred.";
        });
};

export const getAllIngredients = (serverAddress) => {
    const reportOutput = document.getElementById('report-output');

    fetch(`${serverAddress}ingredients`)
        .then(response => response.json())
        .then(data => {
            const jsonData = JSON.stringify(data, null, 2);
            reportOutput.innerHTML = `<pre>${getDateNow()}<br/>${jsonData}</pre>`;
        })
        .catch(error => {
            console.error('Error:', error);
            reportOutput.innerHTML = "An unexpected error occurred.";
        });
};

export const getSpecifiedIngredient = (serverAddress) => {
    const reportOutput = document.getElementById('report-output');
    const ingredientId = document.getElementById('search-ingredient-id').value;
    const ingredientProductReference = document.getElementById('search-ingredient-product-reference').value;
    const ingredientName = document.getElementById('search-ingredient-name').value;
    const ingredientQuantity = document.getElementById('search-ingredient-quantity').value;
    const ingredientPrice = document.getElementById('search-ingredient-price').value;

    const params = {};

    if (ingredientId) {
        params.id = ingredientId;
    }
    if (ingredientProductReference) {
        params.productReference = ingredientProductReference;
    }
    if (ingredientName) {
        params.name = ingredientName;
    }
    if (ingredientQuantity) {
        params.quantity = ingredientQuantity;
    }
    if (ingredientPrice) {
        params.price = ingredientPrice;
    }

    const queryString = new URLSearchParams(params).toString();

    fetch(`${serverAddress}ingredients/search?${queryString}`)
        .then(response => response.json())
        .then(data => {
            const jsonData = JSON.stringify(data, null, 2);
            reportOutput.innerHTML = `<pre>${getDateNow()}<br/>${jsonData}</pre>`;
        })
        .catch(error => {
            console.error('Error:', error);
            reportOutput.innerHTML = "An unexpected error occurred.";
        });
};

export const postIngredient = (serverAddress) => {
    const reportOutput = document.getElementById('report-output');
    const ingredientProductReference = document.getElementById('post-ingredient-product-reference').value;
    const ingredientName = document.getElementById('post-ingredient-name').value;
    const ingredientQuantity = document.getElementById('post-ingredient-quantity').value;
    const ingredientPrice = document.getElementById('post-ingredient-price').value;

    const newIngredient = {
        productReference: ingredientProductReference,
        name: ingredientName,
        quantity: ingredientQuantity,
        price: ingredientPrice
    };

    fetch(`${serverAddress}ingredients/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newIngredient)
    })
        .then(response => response.json())
        .then(data => {
            const jsonData = JSON.stringify(data, null, 2);
            reportOutput.innerHTML = `<pre>${getDateNow()}<br/>${jsonData}</pre>`;
        })
        .catch(error => {
            console.error('Error:', error);
            reportOutput.innerHTML = "An unexpected error occurred.";
        });
};

export const updateIngredient = (serverAddress) => {
    const reportOutput = document.getElementById('report-output');
    const ingredientId = document.getElementById('update-ingredient-id').value;
    const ingredientProductReference = document.getElementById('update-ingredient-product-reference').value;
    const ingredientName = document.getElementById('update-ingredient-name').value;
    const ingredientQuantity = document.getElementById('update-ingredient-quantity').value;
    const ingredientPrice = document.getElementById('update-ingredient-price').value;

    const params = {};

    if (ingredientId) {
        params.id = ingredientId;
    }
    if (ingredientProductReference) {
        params.productReference = ingredientProductReference;
    }
    if (ingredientName) {
        params.name = ingredientName;
    }
    if (ingredientQuantity) {
        params.quantity = ingredientQuantity;
    }
    if (ingredientPrice) {
        params.price = ingredientPrice;
    }

    fetch(`${serverAddress}ingredients/update/${ingredientId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(params)
    })
        .then(response => response.json())
        .then(data => {
            const jsonData = JSON.stringify(data, null, 2);
            reportOutput.innerHTML = `<pre>${getDateNow()}<br/>${jsonData}</pre>`;
        })
        .catch(error => {
            console.error('Error:', error);
            reportOutput.innerHTML = "An unexpected error occurred.";
        });
};

export const deleteIngredient = (serverAddress) => {
    const reportOutput = document.getElementById('report-output');
    const ingredientId = document.getElementById('delete-ingredient-id').value;

    fetch(`${serverAddress}ingredients/remove/${ingredientId}`, {
        method: 'DELETE'
    })
        .then(response => response.json())
        .then(data => {
            const jsonData = JSON.stringify(data, null, 2);
            reportOutput.innerHTML = `<pre>${getDateNow()}<br/>${jsonData}</pre>`;
        })
        .catch(error => {
            console.error('Error:', error);
            reportOutput.innerHTML = "An unexpected error occurred.";
        });
};
