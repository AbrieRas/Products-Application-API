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

document.addEventListener("DOMContentLoaded", function () {
    const desiredHTMLFile = "report.html";
    const currentURL = window.location.href;

    if (currentURL.endsWith(desiredHTMLFile)) {
        addTestEventListenersToButtons();
    }
});

const addTestEventListenersToButtons = () => {
    // ==> PRODUCTS
    // GET
    document.getElementById('products-get-all-button').addEventListener('click',() => {
        fetch('http://localhost:4001/products')
          .then(response => response.json())
          .then(data => {
            console.log(data);
          })
          .catch(error => {
            console.error('Error:', error);
          });
    });

    document.getElementById('products-get-specific-button').addEventListener('click',() => {
        fetch('http://localhost:4001/products/search?id=40')
          .then(response => response.json())
          .then(data => {
            console.log(data);
          })
          .catch(error => {
            console.error('Error:', error);
          });
    });

    // POST
    document.getElementById('products-post-button').addEventListener('click',() => {
        fetch('http://localhost:4001/products/add', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            name: 'Sample Product',
            quantity: '10',
            price: '25.99',
            category: 'ELECTRONICS',
            ingredients: [
              { name: 'Ingredient 1', quantity: '2', price: '5.99' },
              { name: 'Ingredient 2', quantity: '1', price: '3.49' }
            ]
          })
        })
          .then(response => response.json())
          .then(data => {
            console.log(data);
          })
          .catch(error => {
            console.error('Error:', error);
          });
    });

    // PUT
    document.getElementById('products-put-button').addEventListener('click',() => {
        fetch('http://localhost:4001/products/update/92', {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            quantity: '5'
          })
        })
          .then(response => response.json())
          .then(data => {
            console.log(data);
          })
          .catch(error => {
            console.error('Error:', error);
          });
    });

    // DELETE
    document.getElementById('products-delete-button').addEventListener('click',() => {
        fetch('http://localhost:4001/products/remove/92', {
          method: 'DELETE'
        })
          .then(response => response.json())
          .then(data => {
            console.log(data);
          })
          .catch(error => {
            console.error('Error:', error);
          });
    });

    // ==> INGREDIENTS
    // GET
    document.getElementById('ingredients-get-all-button').addEventListener('click',() => {
        fetch('http://localhost:4001/ingredients')
          .then(response => response.json())
          .then(data => {
            console.log(data);
          })
          .catch(error => {
            console.error('Error:', error);
          });
    });

    document.getElementById('ingredients-get-specific-button').addEventListener('click',() => {
        fetch('http://localhost:4001/ingredients/search?price=81.29')
          .then(response => response.json())
          .then(data => {
            console.log(data);
          })
          .catch(error => {
            console.error('Error:', error);
          });
    });

    // POST
    document.getElementById('ingredients-post-button').addEventListener('click',() => {
        fetch('http://localhost:4001/ingredients/add', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            productReference: 'abc',
            name: 'Ingredient 1',
            quantity: '2',
            price: '5.99'
          })
        })
          .then(response => response.json())
          .then(data => {
            console.log(data);
          })
          .catch(error => {
            console.error('Error:', error);
          });
    });

    // PUT
    document.getElementById('ingredients-put-button').addEventListener('click',() => {
        fetch('http://localhost:4001/ingredients/update/501', {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            quantity: '5',
            price: '4.95'
          })
        })
          .then(response => response.json())
          .then(data => {
            console.log(data);
          })
          .catch(error => {
            console.error('Error:', error);
          });
    });

    // DELETE
    document.getElementById('ingredients-delete-button').addEventListener('click',() => {
        fetch('http://localhost:4001/ingredients/remove/501', {
          method: 'DELETE'
        })
          .then(response => response.json())
          .then(data => {
            console.log(data);
          })
          .catch(error => {
            console.error('Error:', error);
          });
    });
};
