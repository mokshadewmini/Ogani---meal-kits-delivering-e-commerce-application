async function loadData() {
    const response = await fetch("LoadData");

    const popup = Notification();

    if (response.ok) {
        const json = await response.json();
        console.log(json);

        loadOption("category", json.categoryList, "name");
        loadOption("condition", json.conditionList, "name");
        loadOption("color", json.colorList, "name");
        loadOption("storage", json.storageList, "value");

        updateProductView(json);

    } else {
        Swal.fire({
        title: 'Error!',
        text: 'Please try again later!',
        icon: 'error',
        confirmButtonText: 'OK'
    });
    }

}


function loadOption(prefix, dataList, property) {
    let options = document.getElementById(prefix + "-options");
    let li = document.getElementById(prefix + "-li");
    options.innerHTML = "";

    dataList.forEach(data => {
        let li_clone = li.cloneNode(true);

        if (prefix == "color") {
            li_clone.style.borderColor = data[property];
            li_clone.querySelector("#" + prefix + "-a").style.backgroundColor = data[property];
        } else {
            li_clone.querySelector("#" + prefix + "-a").innerHTML = data[property];
        }

        options.appendChild(li_clone);
    });

    //from template js
    const all_li = document.querySelectorAll("#" + prefix + "-options li");
    all_li.forEach(x => {
        x.addEventListener('click', function () {
            all_li.forEach(y => y.classList.remove('chosen'));
            this.classList.add('chosen');
        });
    });
}


async function searchProducts() {

    let sort = document.getElementById("sort").value;
    let category = document.getElementById("cat").value;
    let minamount = document.getElementById("minamount").value;
    let maxamount = document.getElementById("maxamount").value;
    let search = document.getElementById("title_search").value;

    const data = {
        category_name: category,
        price_range_start: minamount,
        price_range_end: maxamount,
        sort_text: sort,
        search:search
    };

    console.log(data)

    const response = await fetch("SearchProducts", {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {
        const json = await response.json();
        updateProductView(json); // Update the product view with the new data
    } else {
         Swal.fire({
            title: 'Oops...',
            text: json.content,
            icon: 'error',
            confirmButtonText: 'OK'
        });
    }
}

function  updateProductView(json) {
    // Get the container element
    const productContainer = document.getElementById('product-container');
    const productCountElement = document.getElementById('product_count');

    // Remove all existing product cards
    while (productContainer.firstChild) {
        productContainer.removeChild(productContainer.firstChild);
    }

    // Loop through each product in the JSON data
    json.productList.forEach(product => {
        // Create a new div for the product card
        const productCard = document.createElement('div');
        productCard.className = 'col-lg-4 col-md-6 col-sm-6 product-item';

        // Set the inner HTML for the product card
        productCard.innerHTML = `
                <div class="product__item">
                    <div class="product__item__pic set-bg" style="background-image: url('product-images/${product.id}/2.jpg');"></div>
                    <div class="product__item__text">
                        <h6><a href="#">${product.name}</a></h6>
                        <h5>LKR${product.price.toFixed(2)}</h5>
                    </div>
                </div>
            `;

        // Append the product card to the container
        productContainer.appendChild(productCard);
    });


    // Update the product count
    productCountElement.textContent = jsonData.allProductCount;
}