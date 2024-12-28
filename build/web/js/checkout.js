const popup = Notification();

// Payment completed. It can be a successful failure.
payhere.onCompleted = function onCompleted(orderId) {
    console.log("Payment completed. OrderID:" + orderId);
    // Note: validate the payment and show success or failure page to the customer
    
            Swal.fire({
            title: 'Success!',
            text: "Thank you, Payment completed!",
            icon: 'success',
            confirmButtonText: 'OK'
        });
    window.location = "index.html";
};

// Payment window closed
payhere.onDismissed = function onDismissed() {
    // Note: Prompt user to pay again or show an error page
     Swal.fire({
            title: 'Oops...',
            text: "Payment dismissed",
            icon: 'error',
            confirmButtonText: 'OK'
        });
    
};

// Error occurred
payhere.onError = function onError(error) {
    // Note: show an error page
    Swal.fire({
            title: 'Oops...',
            text: error,
            icon: 'error',
            confirmButtonText: 'OK'
        });
    
};


//**********************pah here end***************************//

async function loadData() {

    const response = await fetch("LoadCheckout");

    if (response.ok) {
        const json = await response.json();
        console.log(json);
        

        if (json.success) {
            //store response data
            const address = json.address;
            const cityList = json.cityList;
            const cartList = json.cartList;

            //load cities
            let citySelect = document.getElementById("city");
            citySelect.length = 1;

            cityList.forEach(city => {
                let cityOption = document.createElement("option");
                cityOption.value = city.id;
                cityOption.innerHTML = city.name;
                citySelect.appendChild(cityOption);
            });

            //load current address
            let currentAddressCheckbox = document.getElementById("checkbox1");
            currentAddressCheckbox.addEventListener("change", e => {

                let firstName = document.getElementById("first-name");
                let lastName = document.getElementById("last-name");
                let city = document.getElementById("city");
                let address1 = document.getElementById("address1");
                let address2 = document.getElementById("address2");
                let postalCode = document.getElementById("postal-code");
                let mobile = document.getElementById("mobile");

                if (currentAddressCheckbox.checked) {

                    firstName.value = address.first_name;
                    lastName.value = address.last_name;
                    city.value = address.city.id;
                    city.disabled = true;
                    city.dispatchEvent(new Event("change"));
                    address1.value = address.line1;
                    address2.value = address.line2;
                    postalCode.value = address.postal_code;
                    mobile.value = address.mobile;

                } else {

                    firstName.value = "";
                    lastName.value = "";
                    city.value = 0;
                    city.disabled = false;
                    city.dispatchEvent(new Event("change"));
                    address1.value = "";
                    address2.value = "";
                    postalCode.value = "";
                    mobile.value = "";

                }

            });

           

            let sub_total = 0;

// Get the container element
    const cartItemContainer = document.getElementById('itemlist');


    // Remove all existing product cards
    while (cartItemContainer.firstChild) {
        cartItemContainer.removeChild(cartItemContainer.firstChild);
    }

    // Loop through each product in the JSON data
    let total = 0;
    cartList.forEach(item => {
        // Create a new div for the product card
        const cartItem = document.createElement('li');
// Calculate total price
        const totalPrice = (item.product.price * item.qty);
        total = total + totalPrice;

        // Set the inner HTML for the product card
        cartItem.innerHTML = `
                
                           <h5>${item.product.name}<span>LKR.${item.product.price}</span></h5>     
                               
            `;

        // Append the product card to the container
        cartItemContainer.appendChild(cartItem);
    });

  document.getElementById("cartTotal").innerHTML = total;
  
  
            citySelect.addEventListener("change", e => {
                //update shipping charges

                //get cart item count
                let item_count = cartList.length;

                let shipping_amount = 0;

                if (citySelect.value === "1") {
                    //Colombo
                    shipping_amount = item_count * 1000;

                } else {
                    //out of Colombo
                    shipping_amount = item_count * 2500;
                }

                           });

            citySelect.dispatchEvent(new Event("change"));

        } else {
            window.location = "sign-in.html";
        }

    }

}

async function checkout() {

    //check address status
    let isCurrentAddress = document.getElementById("checkbox1").checked;

    //get address data
    let firstName = document.getElementById("first-name");
    let lastName = document.getElementById("last-name");
    let city = document.getElementById("city");
    let address1 = document.getElementById("address1");
    let address2 = document.getElementById("address2");
    let postalCode = document.getElementById("postal-code");
   let mobile = document.getElementById("mobile");

    //request data (json)
    const data = {
        isCurrentAddress: isCurrentAddress,
        firstName: firstName.value,
        lastName: lastName.value,
        cityId: city.value,
        address1: address1.value,
        address2: address2.value,
        postalCode: postalCode.value,
       mobile: mobile.value
    };
    
    console.log(data)

    const response = await fetch("Checkout", {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {
        const json = await response.json();
        console.log(json);

        if (json.success) {

            console.log(json.payhereJson);
            payhere.startPayment(json.payhereJson);
        } else {
             console.log(json);
          
        }

    } else {
       alert("Please try again later");
        
    }

}