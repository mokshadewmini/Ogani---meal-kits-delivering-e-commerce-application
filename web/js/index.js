async function checkSignIn() {

    const response = await fetch("CheckSignIn");

    if (response.ok) {

        const json = await response.json();

        const response_dto = json.response_dto;

        if (response_dto.success) {
            // signed in

            const user = response_dto.content;
           

            let st_quick_link = document.getElementById("st-quick-link");

            let st_quick_link_li_1 = document.getElementById("st-quick-link-li-1");
            let st_quick_link_li_2 = document.getElementById("st-quick-link-li-2");

            st_quick_link_li_1.remove();
            st_quick_link_li_2.remove();

            let new_li_tag1 = document.createElement("div");
            let new_li_a_tag1 = document.createElement("a");
            new_li_a_tag1.href = "#";
            new_li_a_tag1.innerHTML = user.first_name + " " + user.last_name;
            new_li_tag1.appendChild(new_li_a_tag1);
            // st_quick_link.appendChild(new_li_tag1);



        } else {
            //  not signed in
            console.log("not signed in");
        }

        //display last 3 products
        const productList = json.products;

        let i = 1;
        productList.forEach(product => {
            console.log(product.name);
            console.log(product.id);
//            consle.log(product.id);
           // document.getElementById("st-product-image-" + i).src = "product-images/" + product.id + "/2.jpg";
           // Get the element by its ID
var productImageElement = document.getElementById("st-product-image-" + i);

// Update the 'data-setbg' attribute with the new image path
productImageElement.setAttribute("data-setbg", "product-images/" + product.id + "/2.jpg");

// Apply the background image to the element using JavaScript (if not already handled by CSS)
productImageElement.style.backgroundImage = "url('product-images/" + product.id + "/2.jpg')";

            document.getElementById("st-product-title-" + i).innerHTML = product.name;
             document.getElementById("st-product-link-" + i).href = "single-product.html?pid=" + product.id;

            document.getElementById("st-product-price-" + i).innerHTML = new Intl.NumberFormat(
                    "en-LK",
                    {
                        style: "currency",
                        currency: "LKR",
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 4
                    }
            ).format((product.price));
    
            i++;
        });

  
       


    }
}

async function viewCart() {

    const response = await fetch("cart.html");

    if (response.ok) {
        const cartHtmlText = await response.text();
        //const parser = new DOMParser();
        //const cartHtml = parser.parseFromString(cartHtmlText, "text/html");
        //const cartMain = cartHtml.querySelector("#cart-main");
        //document.querySelector("#index-main").innerHTML = cartMain.innerHTML;
        //filters use karoth url eken yana eka nawaththanna puluwan
        document.querySelector("#index-main").innerHTML = cartHtmlText;
        loadCartItems();
    }

}