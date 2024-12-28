async function loadCartItems() {

    const response = await fetch("LoadCartItems");



    if (response.ok) {
        const json = await response.json();

        if (json.length === 0) {
            Swal.fire({
            title: 'Oops...',
            text: "No items available in your cart",
            icon: 'error',
            confirmButtonText: 'OK'
        });
        window.location = "index.html";
         

        } else {
            cartItemView(json);

        }

    } else {
        Swal.fire({
            title: 'Oops...',
            text: "Unable to process your request",
            icon: 'error',
            confirmButtonText: 'OK'
        });
        
    }

}



function  cartItemView(json) {
    
    console.log(json);
    // Get the container element
    const cartItemContainer = document.getElementById('cart-container');


    // Remove all existing product cards
    while (cartItemContainer.firstChild) {
        cartItemContainer.removeChild(cartItemContainer.firstChild);
    }

    // Loop through each product in the JSON data
    let total = 0;
    json.forEach(item => {
        // Create a new div for the product card
        const cartItem = document.createElement('tr');
// Calculate total price
        const totalPrice = (item.product.price * item.qty);
        total = total + totalPrice;

        // Set the inner HTML for the product card
        cartItem.innerHTML = `
                
                                    <td class="shoping__cart__item">
                                        <a href="cartItemA"> <img id="cartItemImage" style="width: 90px; height: 90px;" src="product-images/${item.product.id}/2.jpg" alt=""><a/>
                                            <a href="#"> <h5 >${item.product.name}</h5></a>
                                    </td>
                                    <td id="cartItemPrice" class="shoping__cart__price">
                                        ${item.product.price}
                                    </td>
                                    <td class="shoping__cart__quantity">
                                        <div class="quantity">
                                            <div class="pro-qty">
                                                <input id="cartTotalQty" type="text" value="${item.qty}">
                                            </div>
                                        </div>
                                    </td>
                                    <td class="shoping__cart__total">
                                         ${totalPrice}
                                    </td>
                                    <td class="shoping__cart__item__close">
                                        <span class="icon_close"></span>
                                    </td>
                               
            `;

        // Append the product card to the container
        cartItemContainer.appendChild(cartItem);
    });

document.getElementById("cartTotal").innerHTML = total;

}